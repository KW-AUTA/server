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
public class ProjectTestDetailDto {
    private Project project;
    private List<PageInfo> pages;
    private int totalSuccessTests;
    private int totalFailTests;
    private int routingSuccessCount;
    private int routingFailCount;
    private int interactionSuccessCount;
    private int interactionFailCount;
    private int mappingSuccessCount;
    private int mappingFailCount;

    public static ProjectTestDetailDto from(Project project, List<Page> pages, TestCountSummary testCountSummary) {
        return ProjectTestDetailDto.builder()
                .project(project)
                .pages(pages.stream()
                        .map(page ->
                                PageInfo.builder().pageId(page.getId()).pageName(page.getPageName())
                                        .build())
                        .toList())
                .totalSuccessTests(testCountSummary.totalPassed())
                .totalFailTests(testCountSummary.totalFail())
                .routingSuccessCount(testCountSummary.typePassed(TestType.ROUTING))
                .routingFailCount(testCountSummary.typeFailed(TestType.ROUTING))
                .interactionSuccessCount(testCountSummary.typePassed(TestType.INTERACTION))
                .interactionFailCount(testCountSummary.typeFailed(TestType.INTERACTION))
                .mappingSuccessCount(testCountSummary.typePassed(TestType.MAPPING))
                .mappingFailCount(testCountSummary.typeFailed(TestType.MAPPING))
                .build();
    }

    @Getter
    @Builder
    public static class PageInfo {
        private Long pageId;
        private String pageName;
    }
}
