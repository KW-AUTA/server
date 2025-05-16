package com.auta.server.adapter.in.page.response;

import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestType;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageTestResponse {

    private RoutingTest routingTest;
    private MappingTest mappingTest;
    private InteractionTest interactionTest;

    public static PageTestResponse from(List<Test> tests) {

        return PageTestResponse.builder()
                .routingTest(RoutingTest.of(filterByType(tests, TestType.ROUTING)))
                .interactionTest(InteractionTest.of(filterByType(tests, TestType.INTERACTION)))
                .mappingTest(MappingTest.of(filterByType(tests, TestType.MAPPING)))
                .build();
    }

    private static List<Test> filterByType(List<Test> tests, TestType testType) {
        return tests.stream()
                .filter(test -> test.getTestType() == testType)
                .toList();
    }

    private static <T> List<T> filterAndMap(List<Test> tests, Predicate<Test> filter, Function<Test, T> mapper) {
        if (tests == null) {
            return Collections.emptyList();
        }
        return tests.stream()
                .filter(filter)
                .map(mapper)
                .toList();
    }

    @Getter
    @Builder
    public static class RoutingTest {
        private List<RoutingSuccess> success;
        private List<RoutingFail> fail;

        public static RoutingTest of(List<Test> routingTests) {
            return RoutingTest.builder()
                    .success(filterAndMap(routingTests, Test::isPassed, RoutingSuccess::from))
                    .fail(filterAndMap(routingTests, Test::isFailed, RoutingFail::from))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RoutingSuccess {
        private String triggerSelector;
        private String expectedDestination;
        private String actualDestination;

        public static RoutingSuccess from(Test test) {
            return RoutingSuccess.builder()
                    .triggerSelector(test.getTriggerSelector())
                    .expectedDestination(test.getExpectedDestination())
                    .actualDestination(test.getActualDestination())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RoutingFail {
        private String triggerSelector;
        private String expectedDestination;
        private String actualDestination;
        private String failReason;

        public static RoutingFail from(Test test) {
            return RoutingFail.builder()
                    .triggerSelector(test.getTriggerSelector())
                    .expectedDestination(test.getExpectedDestination())
                    .actualDestination(test.getActualDestination())
                    .failReason(test.getFailReason())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InteractionTest {
        private List<InteractionSuccess> success;
        private List<InteractionFail> fail;

        public static InteractionTest of(List<Test> interactionTests) {
            return InteractionTest.builder()
                    .success(filterAndMap(interactionTests, Test::isPassed, InteractionSuccess::from))
                    .fail(filterAndMap(interactionTests, Test::isFailed, InteractionFail::from))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InteractionSuccess {
        private String trigger;
        private String actualAction;

        public static InteractionSuccess from(Test test) {
            return InteractionSuccess.builder()
                    .trigger(test.getTrigger())
                    .actualAction(test.getActualAction())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InteractionFail {
        private String trigger;
        private String expectedAction;
        private String actualAction;
        private String failReason;

        public static InteractionFail from(Test test) {
            return InteractionFail.builder()
                    .trigger(test.getTrigger())
                    .expectedAction(test.getExpectedAction())
                    .actualAction(test.getActualAction())
                    .failReason(test.getFailReason())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MappingTest {
        private int matchedComponents;
        private List<ComponentName> componentNames;
        private List<FailComponent> failComponents;

        public static MappingTest of(List<Test> mappingTests) {
            return MappingTest.builder()
                    .matchedComponents((int) mappingTests.stream()
                            .filter(Test::isPassed)
                            .count())
                    .componentNames(filterAndMap(mappingTests, Test::isPassed,
                            ComponentName::from))
                    .failComponents(filterAndMap(mappingTests, Test::isFailed,
                            FailComponent::from))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ComponentName {
        private String componentName;

        public static ComponentName from(Test test) {
            return ComponentName.builder().componentName(test.getComponentName()).build();
        }
    }

    @Getter
    @Builder
    public static class FailComponent {
        private String componentName;
        private String failReason;

        public static FailComponent from(Test test) {
            return FailComponent.builder().componentName(test.getComponentName())
                    .failReason(test.getFailReason())
                    .build();
        }
    }
}
