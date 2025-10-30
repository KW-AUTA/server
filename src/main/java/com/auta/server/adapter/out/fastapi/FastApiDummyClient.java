package com.auta.server.adapter.out.fastapi;

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
        log.info("Dummy Client - 매핑 요청: currentUrl={}, currentPage={}", currentUrl, currentPage);

        try {
            ClassPathResource resource = new ClassPathResource("mock/mapping-response.json");
            InputStream inputStream = resource.getInputStream();
            MappingResponse response = objectMapper.readValue(inputStream, MappingResponse.class);

            log.info("Dummy Client - JSON 파일 로드 성공: 총 {} 개 매핑", response.getMappings().size());

            return Mono.just(response)
                    .delayElement(Duration.ofSeconds(3));
        } catch (IOException e) {
            log.error("Dummy Client - JSON 파일 로드 실패", e);
            return Mono.error(new RuntimeException("Mock 데이터 로드 실패", e));
        }
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
