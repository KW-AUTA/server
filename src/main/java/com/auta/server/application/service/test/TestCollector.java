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
            return Mono.empty();
        }
        visited.add(currentPage);

        Page page = Page.of(project, currentPage, currentUrl);
        pages.add(page);

        return fastApiPort.requestComponentMapping(currentUrl, currentPage, project.getFigmaJson())
                .flatMap(response -> {
                    List<MappingInfo> mappings = response.getMappings();

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

                    List<RoutingMappingInfo> routings = mappings.stream()
                            .filter(mapping -> mapping instanceof RoutingMappingInfo)
                            .map(mapping -> (RoutingMappingInfo) mapping)
                            .toList();
                    
                    for (RoutingMappingInfo routing : routings) {
                        tests.add(Test.ofRoutingResult(project, page, routing));
                    }

                    List<Mono<Void>> recursive = routings.stream()
                            .filter(RoutingMappingInfo::isSuccess)
                            .map(r -> collect(r.getDestinationFigmaPage(), r.getDestinationUrl()))
                            .toList();

                    return Mono.when(recursive);
                })
                .onErrorResume(e -> {
                    log.error("FastAPI 오류", e);
                    return Mono.empty();
                });
    }
}