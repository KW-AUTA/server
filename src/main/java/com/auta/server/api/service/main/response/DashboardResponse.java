package com.auta.server.api.service.main.response;

import com.auta.server.domain.project.ProjectStatus;
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
