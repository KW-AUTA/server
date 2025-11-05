package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse.Evaluation;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
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
    private final ObjectMapper objectMapper;

    @Override
    public Mono<MappingResponse> requestComponentMapping(String currentUrl, String currentPage, String figmaJson) {
        log.info("컴포넌트 매핑 요청 - URL: {}, Page: {}", currentUrl, currentPage);

        // Mock JSON 파일이 있는지 확인
        String mockFileName = "mock/" + currentPage + ".json";
        ClassPathResource mockResource = new ClassPathResource(mockFileName);

        if (mockResource.exists()) {
            log.info("Mock 파일 발견 - Page: {}, File: {}", currentPage, mockFileName);
            try {
                InputStream inputStream = mockResource.getInputStream();
                MappingResponse response = objectMapper.readValue(inputStream, MappingResponse.class);
                log.info("Mock 파일 로드 성공 - Page: {}, 매핑 수: {}", currentPage, response.getMappings().size());
                return Mono.just(response);
            } catch (IOException e) {
                log.warn("Mock 파일 로드 실패 - Page: {}, FastAPI 호출로 전환", currentPage, e);
            }
        } else {
            log.info("Mock 파일 없음 - Page: {}, FastAPI 호출", currentPage);
        }

        // Mock 파일이 없거나 로드 실패 시 실제 FastAPI 호출
        MappingRequest request = MappingRequest.builder().currentUrl(currentUrl)
                .currentPage(currentPage)
                .figmaUrl(figmaJson)
                .build();

        return webClient.post()
                .uri("/mapping")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MappingResponse.class)
                .doOnSuccess(response -> log.info("컴포넌트 매핑 응답 성공 - Page: {}, 매핑 수: {}",
                        currentPage, response.getMappings().size()))
                .doOnError(e -> log.error("컴포넌트 매핑 요청 실패 - URL: {}, Page: {}, Error: {}",
                        currentUrl, currentPage, e.getMessage(), e));
    }

    @Override
    public Mono<UITestResponse> requestUITest(String figmaJson) {
//        UITestRequest request = UITestRequest.builder().figmaJsonUrl(figmaJson).build();
//        log.info("uiux 테스트");
//        return webClient.post()
//                .uri("/evaluate-ui-with-highlight")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchangeToMono(response -> {
//                    log.info("Status: {}", response.statusCode());
//                    return response.bodyToMono(UITestResponse.class);
//                })
//                .doOnError(e -> log.error("요청 실패", e));

        UITestResponse response = UITestResponse.builder().usabilityScore(85)
                .evaluations(List.of(Evaluation.builder().frameSummary("요약").highlightImageUrl("www").build())).build();
        return Mono.just(response)
                .delayElement(Duration.ofSeconds(10));
    }
}
