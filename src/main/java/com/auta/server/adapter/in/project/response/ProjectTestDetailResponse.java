package com.auta.server.adapter.in.project.response;

import com.auta.server.application.port.in.project.dto.ProjectTestDetailDto;
import com.auta.server.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTestDetailResponse {
    private String projectName;

    private String projectAdmin;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime testExecutionTime;

    private String description;
    private TestCountSummary testSummary;
    private List<PageInfo> pages;

    public static ProjectTestDetailResponse from(ProjectTestDetailDto projectTestDetail) {
        Project project = projectTestDetail.getProject();
        return ProjectTestDetailResponse.builder()
                .projectName(project.getProjectName())
                .projectAdmin(project.getUser().getUsername())
                .projectStart(project.getProjectCreatedDate())
                .projectEnd(project.getProjectEnd())
                .testExecutionTime(project.getTestExecuteTime())
                .description(project.getDescription())
                .testSummary(TestCountSummary.builder()
                        .totalSuccessTests(projectTestDetail.getTotalSuccessTests())
                        .totalFailTests(projectTestDetail.getTotalFailTests())
                        .routingSuccessCount(projectTestDetail.getRoutingSuccessCount())
                        .routingFailCount(projectTestDetail.getRoutingFailCount())
                        .interactionSuccessCount(projectTestDetail.getInteractionSuccessCount())
                        .interactionFailCount(projectTestDetail.getInteractionFailCount())
                        .mappingSuccessCount(projectTestDetail.getMappingSuccessCount())
                        .mappingFailCount(projectTestDetail.getMappingFailCount())
                        .build())
                .pages(projectTestDetail.getPages()
                        .stream()
                        .map(page -> PageInfo.builder()
                                .pageId(page.getPageId())
                                .pageName(page.getPageName())
                                .build())
                        .toList())
                .build();
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCountSummary {

        private int totalSuccessTests;
        private int totalFailTests;
        private int interactionSuccessCount;
        private int interactionFailCount;
        private int mappingSuccessCount;
        private int mappingFailCount;
        private int routingSuccessCount;
        private int routingFailCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Long pageId;
        private String pageName;
    }

}
