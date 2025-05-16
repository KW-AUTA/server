package com.auta.server.application.port.in.project;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectCommand {
    private String projectName;
    private LocalDate expectedTestExecution;
    private LocalDate projectEnd;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String rootFigmaPage;

    @Builder
    private ProjectCommand(String projectName, LocalDate expectedTestExecution, LocalDate projectEnd,
                           String description,
                           String figmaUrl,
                           String serviceUrl, String rootFigmaPage) {
        this.projectName = projectName;
        this.expectedTestExecution = expectedTestExecution;
        this.projectEnd = projectEnd;
        this.description = description;
        this.figmaUrl = figmaUrl;
        this.serviceUrl = serviceUrl;
        this.rootFigmaPage = rootFigmaPage;
    }
}
