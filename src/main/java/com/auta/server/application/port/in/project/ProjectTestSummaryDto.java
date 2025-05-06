package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestCountSummary;
import com.auta.server.domain.test.TestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTestSummaryDto {
    private Long projectId;
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;

    private int totalRoutingTest;
    private int successRoutingTest;
    private int totalInteractionTest;
    private int successInteractionTest;
    private int totalMappingTest;
    private int successMappingTest;


    public static ProjectTestSummaryDto of(Project project,
                                           TestCountSummary testCountSummary) {

        int totalRoutingTest = testCountSummary.total(TestType.ROUTING);
        int successRoutingTest = testCountSummary.passed(TestType.ROUTING);

        int totalInteractionTest = testCountSummary.total(TestType.INTERACTION);
        int successInteractionTest = testCountSummary.passed(TestType.INTERACTION);

        int totalMappingTest = testCountSummary.total(TestType.MAPPING);
        int successMappingTest = testCountSummary.passed(TestType.MAPPING);

        return ProjectTestSummaryDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .projectCreatedDate(project.getProjectCreatedDate())
                .totalRoutingTest(totalRoutingTest)
                .successRoutingTest(successRoutingTest)
                .totalInteractionTest(totalInteractionTest)
                .successInteractionTest(successInteractionTest)
                .totalMappingTest(totalMappingTest)
                .successMappingTest(successMappingTest)
                .build();
    }
}
