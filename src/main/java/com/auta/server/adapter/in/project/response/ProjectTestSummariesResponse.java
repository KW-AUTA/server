package com.auta.server.adapter.in.project.response;

import com.auta.server.application.port.in.project.dto.ProjectTestSummaryDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ProjectTestSummariesResponse {
    private List<ProjectTestSummary> tests;

    public static ProjectTestSummariesResponse from(List<ProjectTestSummaryDto> projectTestSummaryList) {
        return ProjectTestSummariesResponse.builder()
                .tests(
                        projectTestSummaryList.stream()
                                .map(
                                        projectTestSummaryDto ->
                                                ProjectTestSummary.builder()
                                                        .projectId(projectTestSummaryDto.getProjectId())
                                                        .projectName(projectTestSummaryDto.getProjectName())
                                                        .projectCreatedDate(
                                                                projectTestSummaryDto.getProjectCreatedDate())
                                                        .totalRoutingTest(projectTestSummaryDto.getTotalRoutingTest())
                                                        .successRoutingTest(
                                                                projectTestSummaryDto.getSuccessRoutingTest())
                                                        .totalInteractionTest(
                                                                projectTestSummaryDto.getTotalInteractionTest())
                                                        .successRoutingTest(
                                                                projectTestSummaryDto.getSuccessInteractionTest())
                                                        .totalMappingTest(projectTestSummaryDto.getTotalMappingTest())
                                                        .successMappingTest(
                                                                projectTestSummaryDto.getSuccessMappingTest())
                                                        .build()
                                ).toList()
                )
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectTestSummary {
        private Long projectId;
        private String projectName;

        private int totalRoutingTest;
        private int successRoutingTest;
        private int totalInteractionTest;
        private int successInteractionTest;
        private int totalMappingTest;
        private int successMappingTest;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate projectCreatedDate;
    }
}
