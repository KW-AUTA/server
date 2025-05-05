package com.auta.server.application.port.in.project;

import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestType;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailDto {
    private Project project;
    private List<PageInfo> pages;
    private Map<TestType, Long> testCounts;

    public static ProjectDetailDto of(Project project, List<Page> pages, Map<TestType, Long> testCounts) {
        return ProjectDetailDto.builder()
                .project(project)
                .pages(pages.stream().map(page -> ProjectDetailDto.PageInfo.builder().pageName(page.getPageName())
                        .pageBaseUrl(page.getPageBaseUrl()).build()).toList())
                .testCounts(testCounts)
                .build();
    }

    @Getter
    @Builder
    public static class PageInfo {
        private String pageName;
        private String pageBaseUrl;
    }
}
