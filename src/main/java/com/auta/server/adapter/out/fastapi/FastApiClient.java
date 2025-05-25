package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.response.ComponentMappingResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastApiClient implements FastApiPort {

    private final WebClient webClient;

//    @Override
//    public FigmaGraphResponse requestFigmaGraphAnalysis(String figmaJson, String rootFigmaPage, String serviceUrl) {
//        FigmaParseRequest request = FigmaParseRequest.of(figmaJson, rootFigmaPage, serviceUrl);
//
//        return webClient.post()
//                .uri("/figma/parse")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(FigmaGraphResponse.class)
//                .block();
//    }

    @Override
    public ComponentMappingResponse requestComponentMapping(String currentUrl, String currentPage, Long projectId) {
        return null;
    }

    @Override
    public String requestRouting(String selector, String currentUrl) {
        return null;
    }
}
