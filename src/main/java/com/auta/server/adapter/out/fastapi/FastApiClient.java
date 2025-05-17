package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.GraphRequest;
import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.request.RoutingRequest;
import com.auta.server.adapter.out.fastapi.response.GraphResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.RoutingResponse;
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

    @Override
    public GraphResponse callGraph(GraphRequest request) {
        return webClient.post()
                .uri("/graph")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GraphResponse.class)
                .block();
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
