package com.auta.server.adapter.in.project.response;

import com.auta.server.application.port.in.project.ProjectDetailDto;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
    private String projectName;
    private String projectAdmin;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime testExecutionTime;

    private String rootFigmaPage;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String reportSummary;

    private TestSummary testSummary;

    private List<PageInfo> pages;

    public static ProjectDetailResponse from(ProjectDetailDto projectDetailDto) {
        Project project = projectDetailDto.getProject();
        Map<TestType, Long> testCounts = projectDetailDto.getTestCounts();
        return ProjectDetailResponse.builder()
                .projectName(project.getProjectName())
                .projectAdmin(project.getUser().getUsername())
                .projectCreatedDate(project.getProjectCreatedDate())
                .projectEnd(project.getProjectEnd())
                .testExecutionTime(project.getTestExecuteTime())
                .rootFigmaPage(project.getRootFigmaPage())
                .description(project.getDescription())
                .figmaUrl(project.getFigmaUrl())
                .serviceUrl(project.getServiceUrl())
                .reportSummary(null)
                .testSummary(TestSummary.builder()
                        .totalRoutingTest(
                                Math.toIntExact(Optional.ofNullable(testCounts.get(TestType.ROUTING)).orElse(0L)))
                        .totalInteractionTest(
                                Math.toIntExact(Optional.ofNullable(testCounts.get(TestType.INTERACTION)).orElse(0L)))
                        .totalMappingTest(
                                Math.toIntExact(Optional.ofNullable(testCounts.get(TestType.COMPONENT)).orElse(0L)))
                        .build())
                .pages(projectDetailDto.getPages().stream()
                        .map(p -> PageInfo.builder()
                                .pageName(p.getPageName())
                                .pageBaseUrl(p.getPageBaseUrl())
                                .build())
                        .toList())
                .build();
    }

    private static int getTestCount(Map<TestType, Long> map, TestType type) {
        return Math.toIntExact(map.getOrDefault(type, 0L));
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestSummary {
        private int totalRoutingTest;
        private int totalInteractionTest;
        private int totalMappingTest;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private String pageName;
        private String pageBaseUrl;
    }
}
