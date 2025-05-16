package com.auta.server.adapter.in.project.response;

import com.auta.server.application.port.in.project.dto.ProjectSummaryDto;
import com.auta.server.domain.project.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectSummariesResponse {

    private List<ProjectSummary> projectSummaries;

    public static ProjectSummariesResponse from(List<ProjectSummaryDto> projectSummaryDTOS) {
        return ProjectSummariesResponse.builder().projectSummaries(projectSummaryDTOS.stream()
                        .map(ProjectSummary::from).toList())
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProjectSummary {
        private Long projectId;
        private String projectAdmin;
        private String projectName;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectEnd;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectCreatedDate;
        private ProjectStatus projectStatus;
        private Integer testRate;

        public static ProjectSummary from(ProjectSummaryDto projectSummaryDto) {
            return ProjectSummary.builder().projectId(projectSummaryDto.getProjectId())
                    .projectAdmin(projectSummaryDto.getProjectAdmin())
                    .projectName(projectSummaryDto.getProjectName())
                    .projectEnd(projectSummaryDto.getProjectEnd())
                    .projectCreatedDate(projectSummaryDto.getProjectCreatedDate())
                    .projectStatus(projectSummaryDto.getProjectStatus())
                    .testRate(projectSummaryDto.getTestRate())
                    .build();
        }
    }
}
