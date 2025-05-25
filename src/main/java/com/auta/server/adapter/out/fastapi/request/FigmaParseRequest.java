package com.auta.server.adapter.out.fastapi.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FigmaParseRequest {
    private String figmaJson;
    private String rootFigmaPage;
    private String serviceUrl;

    public static FigmaParseRequest of(String figmaJson, String rootFigmaPage, String serviceUrl) {
        return FigmaParseRequest.builder().figmaJson(figmaJson)
                .rootFigmaPage(rootFigmaPage)
                .serviceUrl(serviceUrl).build();
    }
}