package com.auta.server.adapter.out.fastapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MappingResponse {
    private List<MappingInfo> mappings;

    @Getter
    @Builder
    public static class MappingInfo {
        private String componentName;
        private String destinationFigmaPage;
        private String destinationUrl;
        private String actualUrl;
        private String failReason;

        @JsonProperty("isSuccess")
        private boolean isSuccess;
        @JsonProperty("isRouting")
        private boolean isRouting;
    }
}
