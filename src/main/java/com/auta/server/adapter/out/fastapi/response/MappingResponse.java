package com.auta.server.adapter.out.fastapi.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MappingResponse {
    private List<MappingInfo> mappings;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RoutingMappingInfo.class, name = "ROUTING"),
            @JsonSubTypes.Type(value = InteractionMappingInfo.class, name = "INTERACTION")
    })
    @Getter
    public static abstract class MappingInfo {
        private final String componentName;
        private final boolean isSuccess;
        private final String failReason;
        
        protected MappingInfo(String componentName, boolean isSuccess, String failReason) {
            this.componentName = componentName;
            this.isSuccess = isSuccess;
            this.failReason = failReason;
        }
    }

    @Getter
    public static class RoutingMappingInfo extends MappingInfo {
        private final String destinationFigmaPage;
        private final String destinationUrl;
        private final String actualUrl;

        @Builder
        public RoutingMappingInfo(String componentName, boolean isSuccess, String failReason,
                                  String destinationFigmaPage, String destinationUrl, String actualUrl) {
            super(componentName, isSuccess, failReason);
            this.destinationFigmaPage = destinationFigmaPage;
            this.destinationUrl = destinationUrl;
            this.actualUrl = actualUrl;
        }
    }

    @Getter
    public static class InteractionMappingInfo extends MappingInfo {
        private final String expectedAction;
        private final String actualAction;

        @Builder
        public InteractionMappingInfo(String componentName, boolean isSuccess, String failReason,
                                      String expectedAction, String actualAction) {
            super(componentName, isSuccess, failReason);
            this.expectedAction = expectedAction;
            this.actualAction = actualAction;
        }
    }

    @Getter
    public static class GeneralMappingInfo extends MappingInfo {

        @Builder
        public GeneralMappingInfo(String componentName, boolean isSuccess, String failReason) {
            super(componentName, isSuccess, failReason);
        }
    }
}
