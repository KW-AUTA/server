package com.auta.server.adapter.in.project.response;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectStatusSSEResponse {
    private Long projectId;
    private String projectName;
    private ProjectStatus projectStatus;
    private LocalDateTime modifiedDateTime;

    public static ProjectStatusSSEResponse from(Project project) {
        return ProjectStatusSSEResponse.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .projectStatus(project.getProjectStatus())
                .modifiedDateTime(project.getModifiedDateTime())
                .build();
    }
}
