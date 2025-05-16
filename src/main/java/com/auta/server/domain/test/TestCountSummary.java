package com.auta.server.domain.test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TestCountSummary {

    private final Map<TestType, Map<TestStatus, Long>> groupedTestCountMap;

    private TestCountSummary(Map<TestType, Map<TestStatus, Long>> groupedTestCountMap) {
        this.groupedTestCountMap = groupedTestCountMap;
    }

    public static TestCountSummary from(List<Test> tests) {
        Map<TestType, Map<TestStatus, Long>> groupedTestCountMap = tests.stream()
                .collect(Collectors.groupingBy(Test::getTestType,
                        Collectors.groupingBy(Test::getTestStatus, Collectors.counting())));
        return new TestCountSummary(groupedTestCountMap);
    }

    public int totalPassed() {
        return Math.toIntExact(groupedTestCountMap.values().stream()
                .mapToLong(statusMap -> statusMap.getOrDefault(TestStatus.PASSED, 0L))
                .sum());
    }

    public int totalFail() {
        return Math.toIntExact(groupedTestCountMap.values().stream()
                .mapToLong(statusMap -> statusMap.getOrDefault(TestStatus.FAILED, 0L))
                .sum());
    }

    public int typeTotal(TestType type) {
        Map<TestStatus, Long> statusMap = groupedTestCountMap.getOrDefault(type, Map.of());
        return Math.toIntExact(statusMap.entrySet().stream()
                .filter(entry -> entry.getKey().isCompleted())
                .mapToLong(Entry::getValue)
                .sum());
    }


    public int typePassed(TestType type) {
        return Math.toIntExact(
                groupedTestCountMap
                        .getOrDefault(type, Map.of())
                        .getOrDefault(TestStatus.PASSED, 0L)
        );
    }

    public int typeFailed(TestType type) {
        return Math.toIntExact(
                groupedTestCountMap
                        .getOrDefault(type, Map.of())
                        .getOrDefault(TestStatus.FAILED, 0L)
        );
    }
}
