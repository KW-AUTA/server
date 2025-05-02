package com.auta.server.adapter.in.project.response;

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
