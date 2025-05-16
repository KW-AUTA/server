package com.auta.server.application.port.in.project.dto;

import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestCountSummary;
import com.auta.server.domain.test.TestType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailDto {
    private Project project;
    private List<PageInfo> pages;
    private int totalRoutingTest;
    private int totalInteractionTest;
    private int totalMappingTest;

    public static ProjectDetailDto of(Project project, List<Page> pages, TestCountSummary testCountSummary) {
        return ProjectDetailDto.builder()
                .project(project)
                .pages(pages.stream().map(page -> ProjectDetailDto.PageInfo.builder().pageName(page.getPageName())
                        .pageBaseUrl(page.getPageBaseUrl()).build()).toList())
                .totalRoutingTest(testCountSummary.typeTotal(TestType.ROUTING))
                .totalInteractionTest(testCountSummary.typeTotal(TestType.INTERACTION))
                .totalMappingTest(testCountSummary.typeTotal(TestType.MAPPING))
                .build();
    }

    @Getter
    @Builder
    public static class PageInfo {
        private String pageName;
        private String pageBaseUrl;
    }
}
