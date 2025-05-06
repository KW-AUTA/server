package com.auta.server.domain.test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Builder;

public class TestCountSummary {

    private final Map<TestType, Map<TestStatus, Long>> groupedTestCountMap;

    @Builder
    private TestCountSummary(Map<TestType, Map<TestStatus, Long>> groupedTestCountMap) {
        this.groupedTestCountMap = groupedTestCountMap;
    }

    public static TestCountSummary from(List<Test> tests) {
        Map<TestType, Map<TestStatus, Long>> groupedTestCountMap = tests.stream()
                .collect(Collectors.groupingBy(Test::getTestType,
                        Collectors.groupingBy(Test::getTestStatus, Collectors.counting())));
        return new TestCountSummary(groupedTestCountMap);
    }

    public int total(TestType type) {
        Map<TestStatus, Long> statusMap = groupedTestCountMap.getOrDefault(type, Map.of());
        return Math.toIntExact(statusMap.entrySet().stream()
                .filter(entry -> entry.getKey().isCompleted())
                .mapToLong(Entry::getValue)
                .map(Math::toIntExact)
                .sum());
    }

    public int passed(TestType type) {
        return Math.toIntExact(
                groupedTestCountMap
                        .getOrDefault(type, Map.of())
                        .getOrDefault(TestStatus.PASSED, 0L)
        );
    }
}
