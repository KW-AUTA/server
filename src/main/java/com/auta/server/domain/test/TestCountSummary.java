package com.auta.server.domain.test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;

public class TestCountSummary {

    private final Map<TestType, Long> testCountMap;

    @Builder
    private TestCountSummary(Map<TestType, Long> testCountMap) {
        this.testCountMap = testCountMap;
    }

    public static TestCountSummary from(List<Test> tests) {
        Map<TestType, Long> map = tests.stream()
                .collect(Collectors.groupingBy(Test::getTestType, () -> new EnumMap<>(TestType.class),
                        Collectors.counting()));
        return new TestCountSummary(map);
    }

    public int get(TestType testType) {
        return Math.toIntExact(testCountMap.getOrDefault(testType, 0L));
    }
}
