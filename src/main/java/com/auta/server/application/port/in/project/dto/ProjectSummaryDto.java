package com.auta.server.application.port.in.project.dto;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectSummaryDto {
    private Long projectId;
    private String projectAdmin;
    private String projectName;
    private LocalDate projectEnd;
    private LocalDate projectCreatedDate;
    private ProjectStatus projectStatus;
    private Integer testRate;

    public static ProjectSummaryDto from(Project project) {
        return ProjectSummaryDto.builder()
                .projectId(project.getId())
                .projectAdmin(project.getUser().getUsername())
                .projectName(project.getProjectName())
                .projectCreatedDate(project.getProjectCreatedDate())
                .projectEnd(project.getProjectEnd())
                .projectStatus(project.getProjectStatus())
                .testRate(project.getTestRate())
                .build();
    }
}
