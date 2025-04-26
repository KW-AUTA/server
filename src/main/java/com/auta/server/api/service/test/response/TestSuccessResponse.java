package com.auta.server.api.service.test.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuccessResponse {

    private List<RoutingTestResult> routingTest;
    private MappingTestResult mappingTest;
    private List<InteractionTestResult> interactionTest;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutingTestResult {
        private String triggerSelector;
        private String expectedDestination;
        private String actualDestination;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MappingTestResult {
        private int matchedComponents;
        private List<ComponentName> componentNames;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ComponentName {
            private String componentName;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InteractionTestResult {
        private String trigger;   // ex: "while_hovering"
        private String action;    // ex: "change to"
    }
}
