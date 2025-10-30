package com.auta.server.application.service.test;

import com.auta.server.adapter.out.fastapi.response.MappingResponse.InteractionMappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.RoutingMappingInfo;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TestCollector {
    private final FastApiPort fastApiPort;
    private final Project project;

    private final Set<String> visited = new HashSet<>();
    private final List<Page> pages = new ArrayList<>();
    private final List<Test> tests = new ArrayList<>();

    public Mono<Void> collect(String currentPage, String currentUrl) {
        if (visited.contains(currentPage)) {
            log.debug("이미 방문한 페이지 스킵 - Page: {}", currentPage);
            return Mono.empty();
        }
        visited.add(currentPage);

        Page page = Page.of(project, currentPage, currentUrl);
        pages.add(page);

        log.info("테스트 수집 시작 - Project: {}, Page: {}, URL: {}", project.getId(), currentPage, currentUrl);

        return fastApiPort.requestComponentMapping(currentUrl, currentPage, project.getFigmaJson())
                .flatMap(response -> {
                    List<MappingInfo> mappings = response.getMappings();
                    log.info("매핑 응답 수신 - Page: {}, 전체 매핑 수: {}", currentPage, mappings.size());

                    tests.addAll(
                            mappings.stream()
                                    .map(info -> Test.ofMappingResult(project, page, info))
                                    .toList()
                    );

                    List<InteractionMappingInfo> interactions = mappings.stream()
                            .filter(mapping -> mapping instanceof InteractionMappingInfo)
                            .map(mapping -> (InteractionMappingInfo) mapping)
                            .toList();

                    for (InteractionMappingInfo interaction : interactions) {
                        tests.add(Test.ofInteractionResult(project, page, interaction));
                    }

                    log.info("인터랙션 테스트 수집 완료 - Page: {}, 인터랙션 수: {}", currentPage, interactions.size());

                    List<RoutingMappingInfo> routings = mappings.stream()
                            .filter(mapping -> mapping instanceof RoutingMappingInfo)
                            .map(mapping -> (RoutingMappingInfo) mapping)
                            .toList();

                    for (RoutingMappingInfo routing : routings) {
                        tests.add(Test.ofRoutingResult(project, page, routing));
                    }

                    long successRoutings = routings.stream().filter(RoutingMappingInfo::isSuccess).count();
                    log.info("라우팅 테스트 수집 완료 - Page: {}, 전체 라우팅: {}, 성공: {}",
                            currentPage, routings.size(), successRoutings);

                    List<Mono<Void>> recursive = routings.stream()
                            .filter(RoutingMappingInfo::isSuccess)
                            .map(r -> {
                                log.info("재귀 수집 시작 - From: {} -> To: {}", currentPage, r.getDestinationFigmaPage());
                                return collect(r.getDestinationFigmaPage(), r.getDestinationUrl());
                            })
                            .toList();

                    return Mono.when(recursive);
                })
                .doOnSuccess(ignored -> log.info("테스트 수집 완료 - Page: {}, 현재까지 총 테스트 수: {}",
                        currentPage, tests.size()))
                .onErrorResume(e -> {
                    log.error("테스트 수집 중 FastAPI 오류 발생 - Page: {}, URL: {}", currentPage, currentUrl, e);
                    return Mono.empty();
                });
    }
}