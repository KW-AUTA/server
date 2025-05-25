package com.auta.server.adapter.out.fastapi.response;

import java.util.List;
import lombok.Getter;

@Getter
public class ComponentMappingResponse {
    private List<MappingInfo> mappings;

    @Getter
    public static class MappingInfo {
        private String componentName;
        private String destinationFigmaPage;
        private String destinationUrl;
        private String actualUrl;
        private String failReason;
        private boolean isSuccess;
        private boolean isRouting;
    }
}
