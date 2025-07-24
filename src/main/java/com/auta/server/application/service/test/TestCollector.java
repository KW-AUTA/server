package com.auta.server.application.service.test;

import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
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

@Slf4j
@Getter
@RequiredArgsConstructor
public class TestCollector {
    private final FastApiPort fastApiPort;
    private final Project project;

    private final Set<String> visited = new HashSet<>();
    private final List<Page> pages = new ArrayList<>();
    private final List<Test> tests = new ArrayList<>();

    public void collect(String currentPage, String currentUrl) {
        if (visited.contains(currentPage)) {
            return;
        }
        visited.add(currentPage);

        Page page = Page.of(project, currentPage, currentUrl);
        pages.add(page);

        fastApiPort.requestComponentMapping(currentUrl, currentPage, project.getFigmaJson())
                .subscribe(response -> {
                    List<MappingInfo> mappings = response.getMappings();

                    tests.addAll(mappings.stream()
                            .map(info -> Test.ofMappingResult(project, page, info))
                            .toList());

                    for (MappingInfo routing : mappings.stream().filter(MappingInfo::isRouting).toList()) {
                        tests.add(Test.ofRoutingResult(project, page, routing));

                        if (routing.isSuccess()) {
                            collect(routing.getDestinationFigmaPage(), routing.getDestinationUrl());
                        }
                    }
                }, error -> {
                    log.error("FastAPI 매핑 오류", error);
                });
    }

}