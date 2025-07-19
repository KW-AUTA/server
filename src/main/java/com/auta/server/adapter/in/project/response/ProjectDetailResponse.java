package com.auta.server.adapter.in.project.response;

import com.auta.server.application.port.in.project.dto.ProjectDetailDto;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
    private ProjectInfo projectInfo;
    private UIInfo uiInfo;
    private TestSummary testSummary;
    private List<PageInfo> pages;
    private FigmaInfo figmaInfo;


    public static ProjectDetailResponse from(ProjectDetailDto projectDetailDto) {
        Project project = projectDetailDto.getProject();
        return ProjectDetailResponse.builder()
                .projectInfo(ProjectInfo.builder()
                        .projectName(project.getProjectName())
                        .projectAdmin(project.getUser().getUsername())
                        .projectStatus(project.getProjectStatus())
                        .projectCreatedDate(project.getProjectCreatedDate())
                        .projectEnd(project.getProjectEnd())
                        .description(project.getDescription())
                        .testExecutionTime(project.getTestExecuteTime())
                        .build())
                .figmaInfo(
                        FigmaInfo.builder()
                                .rootFigmaPage(project.getRootFigmaPage())
                                .fileName(project.getFileName())
                                .figmaUrl(project.getFigmaUrl())
                                .serviceUrl(project.getServiceUrl())
                                .build()
                )
                .uiInfo(UIInfo.builder()
                        .score(project.getScore())
                        .uiTests(project.getUiTests().stream().map(uiTest -> UITest.builder()
                                        .UIPageUrl(uiTest.getUIPageUrl()).UIDescription(uiTest.getUIDescription()).build())
                                .toList())
                        .build())
                .testSummary(TestSummary.builder()
                        .totalRoutingTest(projectDetailDto.getTotalRoutingTest())
                        .totalInteractionTest(projectDetailDto.getTotalInteractionTest())
                        .totalMappingTest(projectDetailDto.getTotalMappingTest())
                        .build())
                .pages(projectDetailDto.getPages().stream()
                        .map(p -> PageInfo.builder()
                                .pageName(p.getPageName())
                                .pageBaseUrl(p.getPageBaseUrl())
                                .build())
                        .toList())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfo {
        private String projectName;
        private String projectAdmin;
        private ProjectStatus projectStatus;
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectCreatedDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectEnd;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime testExecutionTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UIInfo {
        private Integer score;
        private List<UITest> uiTests;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UITest {
        private String UIPageUrl;
        private String UIDescription;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private String pageName;
        private String pageBaseUrl;

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
    public static class FigmaInfo {
        private String rootFigmaPage;
        private String fileName;
        private String figmaUrl;
        private String serviceUrl;
    }
}
