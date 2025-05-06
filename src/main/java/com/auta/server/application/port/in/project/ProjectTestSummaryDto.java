package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.TestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Map;
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
                                           Map<TestType, Map<Boolean, Long>> testCountsGroupedByTypeAndStatus) {
        Map<Boolean, Long> routingTestCounts = testCountsGroupedByTypeAndStatus.get(TestType.ROUTING);

        int totalRoutingTest = Math.toIntExact(routingTestCounts.values().stream().mapToLong(Long::longValue).sum());
        int successRoutingTest = Math.toIntExact(routingTestCounts.getOrDefault(true, 0L));

        Map<Boolean, Long> interactionTestCounts = testCountsGroupedByTypeAndStatus.get(TestType.INTERACTION);

        int totalInteractionTest = Math.toIntExact(
                interactionTestCounts.values().stream().mapToLong(Long::longValue).sum());
        int successInteractionTest = Math.toIntExact(interactionTestCounts.getOrDefault(true, 0L));

        Map<Boolean, Long> mappingTestCounts = testCountsGroupedByTypeAndStatus.get(TestType.MAPPING);

        int totalMappingTest = Math.toIntExact(mappingTestCounts.values().stream().mapToLong(Long::longValue).sum());
        int successMappingTest = Math.toIntExact(mappingTestCounts.getOrDefault(true, 0L));

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
