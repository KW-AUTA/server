package com.auta.server.application.port.in.project.dto;

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

        int totalRoutingTest = testCountSummary.typeTotal(TestType.ROUTING);
        int successRoutingTest = testCountSummary.typePassed(TestType.ROUTING);

        int totalInteractionTest = testCountSummary.typeTotal(TestType.INTERACTION);
        int successInteractionTest = testCountSummary.typePassed(TestType.INTERACTION);

        int totalMappingTest = testCountSummary.typeTotal(TestType.MAPPING);
        int successMappingTest = testCountSummary.typePassed(TestType.MAPPING);

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
