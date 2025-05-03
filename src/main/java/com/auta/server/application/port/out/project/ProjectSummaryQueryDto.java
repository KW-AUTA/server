package com.auta.server.application.port.out.project;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectSummaryQueryDto {
    private Long projectId;
    private String projectAdmin;
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;
    private ProjectStatus projectStatus;
    private Integer testRate;

    public static ProjectSummaryQueryDto from(Project project) {
        return ProjectSummaryQueryDto.builder()
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
