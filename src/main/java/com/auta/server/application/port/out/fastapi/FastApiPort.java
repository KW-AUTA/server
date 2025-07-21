package com.auta.server.application.port.out.fastapi;

import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;

public interface FastApiPort {

    MappingResponse requestComponentMapping(String currentUrl, String currentPage, String figmaJson);

    UITestResponse requestUITest(String figmaJson);
}
