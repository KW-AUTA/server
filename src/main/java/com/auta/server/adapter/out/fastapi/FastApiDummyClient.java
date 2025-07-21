package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.UITestRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Profile({"local", "test"})
@Component
@RequiredArgsConstructor
public class FastApiDummyClient implements FastApiPort {
    private final WebClient webClient;

    @Override
    public MappingResponse requestComponentMapping(String currentUrl, String currentPage, String figmaJson) {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드 상태 복구
        }
        // 더미 응답 리턴
        return MappingResponse.builder()
                .mappings(List.of(MappingInfo.builder().build()))
                .build();
    }

    @Override
    public UITestResponse requestUITest(String figmaJson) {
        UITestRequest request = UITestRequest.builder().figmaJsonUrl(figmaJson).build();

        return webClient.post()
                .uri("/evaluate-ui-with-highlight")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UITestResponse.class)
                .block();
    }
}

