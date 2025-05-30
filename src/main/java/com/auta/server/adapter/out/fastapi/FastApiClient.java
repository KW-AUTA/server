package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastApiClient implements FastApiPort {

    private final WebClient webClient;

    @Override
    public MappingResponse requestComponentMapping(String currentUrl, String currentPage, Long projectId) {
        MappingRequest request = MappingRequest.builder().currentUrl(currentUrl)
                .currentPage(currentPage)
                .projectId(projectId)
                .build();

        return webClient.post()
                .uri("/mapping")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MappingResponse.class)
                .block();
    }
}
