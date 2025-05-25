package com.auta.server.application.port.out.fastapi;

import com.auta.server.adapter.out.fastapi.response.ComponentMappingResponse;

public interface FastApiPort {

//    FigmaGraphResponse requestFigmaGraphAnalysis(String figmaJson, String rootFigmaPage, String serviceUrl);

    ComponentMappingResponse requestComponentMapping(String currentUrl, String currentPage, Long projectId);

    String requestRouting(String selector, String currentUrl);
}
