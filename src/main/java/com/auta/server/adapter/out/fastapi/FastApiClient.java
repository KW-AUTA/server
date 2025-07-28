package com.auta.server.adapter.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.UITestRequest;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.GeneralMappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.InteractionMappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.RoutingMappingInfo;
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
        // 1. Routing 타입 mock
        MappingInfo routing = RoutingMappingInfo.builder()
                .componentName("로그인 버튼")
                .isSuccess(true)
                .failReason(null)
                .destinationFigmaPage("LoginPage")
                .destinationUrl("https://service.com/login")
                .actualUrl("https://service.com/login")
                .build();

        // 2. Interaction 타입 mock
        MappingInfo interaction = InteractionMappingInfo.builder()
                .componentName("제출 버튼")
                .isSuccess(false)
                .failReason("기대한 액션과 실제 액션 불일치")
                .expectedAction("팝업 표시")
                .actualAction("페이지 이동")
                .build();

        // 3. General 타입 mock
        MappingInfo general = GeneralMappingInfo.builder()
                .componentName("광고 배너")
                .isSuccess(false)
                .failReason("테스트 대상 아님")
                .build();

        // 4. 전체 Response 생성
        MappingResponse response = MappingResponse.builder()
                .mappings(List.of(routing, interaction, general))
                .build();

        // 5. 15초 후 리턴
        return Mono.just(response)
                .delayElement(Duration.ofSeconds(15));
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
