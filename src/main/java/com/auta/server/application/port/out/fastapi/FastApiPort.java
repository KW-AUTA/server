package com.auta.server.application.port.out.fastapi;

import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import reactor.core.publisher.Mono;

public interface FastApiPort {

    Mono<MappingResponse> requestComponentMapping(String currentUrl, String currentPage, String figmaJson);

    Mono<UITestResponse> requestUITest(String figmaJson);
}
