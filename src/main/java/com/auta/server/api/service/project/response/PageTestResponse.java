package com.auta.server.api.service.project.response;

import java.util.List;
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

    @Getter
    @Builder
    public static class RoutingTest {
        private List<RoutingSuccess> success;
        private List<RoutingFail> fail;
    }

    @Getter
    @Builder
    public static class RoutingSuccess {
        private String triggerSelector;
        private String expectedDestination;
        private String actualDestination;
    }

    @Getter
    @Builder
    public static class RoutingFail {
        private String triggerSelector;
        private String expectedDestination;
        private String actualDestination;
        private String failReason;
    }

    @Getter
    @Builder
    public static class MappingTest {
        private int matchedComponents;
        private List<ComponentName> componentNames;
        private List<FailComponent> failComponents;
    }

    @Getter
    @Builder
    public static class ComponentName {
        private String componentName;
    }

    @Getter
    @Builder
    public static class FailComponent {
        private String componentName;
        private String failReason;
    }

    @Getter
    @Builder
    public static class InteractionTest {
        private List<InteractionSuccess> success;
        private List<InteractionFail> fail;
    }

    @Getter
    @Builder
    public static class InteractionSuccess {
        private String trigger;
        private String action;
    }

    @Getter
    @Builder
    public static class InteractionFail {
        private String trigger;
        private String expectedAction;
        private String actualAction;
        private String failReason;
    }
}
