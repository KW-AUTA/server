package com.auta.server.domain.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.user.User;
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
public class Project {
    private Long id;
    private User user;
    private List<Page> pages;
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

    public void update(ProjectCommand command) {
        this.projectName = command.getProjectName();
        this.projectEnd = command.getProjectEnd();
        this.description = command.getDescription();
        this.figmaUrl = command.getFigmaUrl();
        this.serviceUrl = command.getServiceUrl();
        this.rootFigmaPage = command.getRootFigmaPage();
    }
}
