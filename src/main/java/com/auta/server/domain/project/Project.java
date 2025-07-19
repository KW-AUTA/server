package com.auta.server.domain.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.ui.UITest;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private User user;
    private List<Page> pages;
    private List<UITest> uiTests;
    private String figmaUrl;
    private String fileName;
    private String figmaJson;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private LocalDate projectCreatedDate;
    private LocalDate expectedTestExecution;
    private LocalDate projectEnd;
    private ProjectStatus projectStatus;
    private LocalDateTime testExecuteTime;
    private Integer testRate;
    private Integer score;

    public void update(ProjectCommand command, String fileName, String figmaJson) {
        updateWithoutJson(command);
        this.fileName = fileName;
        this.figmaJson = figmaJson;
    }

    public void updateWithoutJson(ProjectCommand command) {
        this.projectName = command.getProjectName();
        this.expectedTestExecution = command.getExpectedTestExecution();
        this.projectEnd = command.getProjectEnd();
        this.description = command.getDescription();
        this.figmaUrl = command.getFigmaUrl();
        this.serviceUrl = command.getServiceUrl();
        this.rootFigmaPage = command.getRootFigmaPage();
    }

    public void changeStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void updateTestRate(List<Test> tests) {
        int total = tests.size();
        int passed = Math.toIntExact(tests.stream().filter(Test::isPassed).count());

        if (total > 0) {
            this.testRate = (int) Math.round((passed * 100.0) / total);
        } else {
            this.testRate = 0;
        }
    }
}
