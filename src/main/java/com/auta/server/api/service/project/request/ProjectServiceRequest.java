package com.auta.server.api.service.project.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectServiceRequest {
    private String projectName;
    private LocalDate projectEnd;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String rootFigmaPage;

    @Builder
    private ProjectServiceRequest(String projectName, LocalDate projectEnd, String description, String figmaUrl,
                                  String serviceUrl, String rootFigmaPage) {
        this.projectName = projectName;
        this.projectEnd = projectEnd;
        this.description = description;
        this.figmaUrl = figmaUrl;
        this.serviceUrl = serviceUrl;
        this.rootFigmaPage = rootFigmaPage;
    }
}
