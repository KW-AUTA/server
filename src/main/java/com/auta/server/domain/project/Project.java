package com.auta.server.domain.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Project {
    private Long id;
    private User user;
    private List<Long> testIds;
    private List<Long> pageIds;
    private String figmaUrl;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private LocalDate projectCreatedDate;
    private LocalDate projectEnd;
    private ProjectStatus projectStatus;
    private Integer testExecuteTime;
    private Integer testRate;

    @Builder
    public Project(Long id, User user, String figmaUrl, String rootFigmaPage, String serviceUrl, String projectName,
                   String description, LocalDate projectCreatedDate, LocalDate projectEnd, ProjectStatus projectStatus,
                   Integer testExecuteTime, Integer testRate) {
        this.id = id;
        this.user = user;
        this.figmaUrl = figmaUrl;
        this.rootFigmaPage = rootFigmaPage;
        this.serviceUrl = serviceUrl;
        this.projectName = projectName;
        this.description = description;
        this.projectCreatedDate = projectCreatedDate;
        this.projectEnd = projectEnd;
        this.projectStatus = projectStatus;
        this.testExecuteTime = testExecuteTime;
        this.testRate = testRate;
    }

    public void update(ProjectCommand command) {
        this.projectName = command.getProjectName();
        this.projectEnd = command.getProjectEnd();
        this.description = command.getDescription();
        this.figmaUrl = command.getFigmaUrl();
        this.serviceUrl = command.getServiceUrl();
        this.rootFigmaPage = command.getRootFigmaPage();
    }
}
