package com.auta.server.adapter.in.project.response;

import com.auta.server.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long projectId;
    private String projectName;
    private String administrator;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String rootFigmaPage;

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder().projectId(project.getId())
                .projectName(project.getProjectName())
                .administrator(project.getUser().getUsername())
                .projectCreatedDate(project.getProjectCreatedDate())
                .projectEnd(project.getProjectEnd())
                .description(project.getDescription())
                .figmaUrl(project.getFigmaUrl())
                .serviceUrl(project.getServiceUrl())
                .rootFigmaPage(project.getRootFigmaPage())
                .build();
    }
}
