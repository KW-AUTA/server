package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.InitRequest;
import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.request.RoutingRequest;
import com.auta.server.adapter.out.fastapi.response.GraphResponse;
import com.auta.server.adapter.out.fastapi.response.InitResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.RoutingResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastApiClient implements FastApiPort {

    private final WebClient webClient;

    @Override
    public InitResponse init(InitRequest request) {
        return webClient.post()
                .uri("/init")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InitResponse.class)
                .block();
    }

    @Override
    public Map<String, List<String>> getGraph(String fileKey) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/graph")
                        .queryParam("file_key", fileKey)
                        .build())
                .retrieve()
                .bodyToMono(GraphResponse.class)
                .map(GraphResponse::graph)
                .block();  // 동기 방식
    }

    @Override
    public RoutingResponse callRouting(RoutingRequest request) {
        return null;
    }

    @Override
    public MappingResponse callMapping(MappingRequest request) {
        return null;
    }
}
