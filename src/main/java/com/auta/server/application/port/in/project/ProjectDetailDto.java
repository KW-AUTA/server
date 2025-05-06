package com.auta.server.application.port.in.project;

import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestCountSummary;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailDto {
    private Project project;
    private List<PageInfo> pages;
    private TestCountSummary testCountSummary;

    public static ProjectDetailDto of(Project project, List<Page> pages, TestCountSummary testCountSummary) {
        return ProjectDetailDto.builder()
                .project(project)
                .pages(pages.stream().map(page -> ProjectDetailDto.PageInfo.builder().pageName(page.getPageName())
                        .pageBaseUrl(page.getPageBaseUrl()).build()).toList())
                .testCountSummary(testCountSummary)
                .build();
    }

    @Getter
    @Builder
    public static class PageInfo {
        private String pageName;
        private String pageBaseUrl;
    }
}
