package com.auta.server.adapter.in.dashboard.reponse;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestCountSummary;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private int totalProjects;
    private int completedTests;
    private int incompleteTests;
    private List<ProjectInfo> projects;
    private List<TestInfo> tests;

    public static DashboardResponse from(List<Project> projects, List<Test> tests, TestCountSummary testCountSummary) {
        return DashboardResponse.builder()
                .totalProjects(projects.size())
                .completedTests(testCountSummary.totalCompleted())
                .incompleteTests(testCountSummary.totalInCompleted())
                .projects(projects.stream().map(project ->
                        ProjectInfo.builder()
                                .projectId(project.getId())
                                .projectName(project.getProjectName())
                                .administrator(project.getUser().getUsername())
                                .projectEnd(project.getProjectEnd())
                                .projectStatus(project.getProjectStatus())
                                .build()).toList())
                .tests(tests.stream().map(test ->
                        TestInfo.builder()
                                .testId(test.getId())
                                .projectName(test.getProject().getProjectName())
                                .pageName(test.getPage().getPageName())
                                .testStatus(test.getTestStatus())
                                .testType(test.getTestType())
                                .build()).toList())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfo {
        private Long projectId;
        private String projectName;
        private String administrator;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectEnd;
        private ProjectStatus projectStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestInfo {
        private Long testId;
        private String projectName;
        private String pageName;
        private TestType testType;
        private TestStatus testStatus;
    }
}
