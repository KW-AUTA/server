package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.request.UITestRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Profile({"test", "local"})
@Component
@RequiredArgsConstructor
public class FastApiDummyClient implements FastApiPort {

    private final WebClient webClient;

    @Override
    public Mono<MappingResponse> requestComponentMapping(String currentUrl, String currentPage, String figmaJson) {
        MappingRequest request = MappingRequest.builder().currentUrl(currentUrl)
                .currentPage(currentPage)
                .figmaUrl(figmaJson)
                .build();

        return webClient.post()
                .uri("/mapping")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MappingResponse.class);
    }

    @Override
    public Mono<UITestResponse> requestUITest(String figmaJson) {
        UITestRequest request = UITestRequest.builder().figmaJsonUrl(figmaJson).build();
        log.info("uiux 테스트");
        return webClient.post()
                .uri("/evaluate-ui-with-highlight")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(response -> {
                    log.info("Status: {}", response.statusCode());
                    return response.bodyToMono(UITestResponse.class);
                })
                .doOnError(e -> log.error("요청 실패", e));
    }
}
