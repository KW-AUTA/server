package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.UITestRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class FastApiClient implements FastApiPort {

    private final WebClient webClient;

//    @Override
//    public Mono<MappingResponse> requestComponentMapping(String currentUrl, String currentPage, String figmaJson) {
//        MappingRequest request = MappingRequest.builder().currentUrl(currentUrl)
//                .currentPage(currentPage)
//                .figmaUrl(figmaJson)
//                .build();
//
//        return webClient.post()
//                .uri("/mapping")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(MappingResponse.class);
//    }

    @Override
    public Mono<MappingResponse> requestComponentMapping(String currentUrl, String currentPage, String figmaJson) {
        MappingResponse response = MappingResponse.builder()
                .mappings(List.of(MappingInfo.builder().build()))
                .build();
        // 더미 응답 리턴
        return Mono.just(response)
                .delayElement(Duration.ofSeconds(15));
    }

    @Override
    public Mono<UITestResponse> requestUITest(String figmaJson) {
        UITestRequest request = UITestRequest.builder().figmaJsonUrl(figmaJson).build();

        return webClient.post()
                .uri("/evaluate-ui-with-highlight")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UITestResponse.class);
    }
}
