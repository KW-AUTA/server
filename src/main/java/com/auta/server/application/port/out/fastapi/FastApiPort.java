package com.auta.server.application.port.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.InitRequest;
import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.request.RoutingRequest;
import com.auta.server.adapter.out.fastapi.response.InitResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.RoutingResponse;
import java.util.List;
import java.util.Map;

public interface FastApiPort {

    InitResponse init(InitRequest request);

    RoutingResponse callRouting(RoutingRequest request);

    MappingResponse callMapping(MappingRequest request);

    Map<String, List<String>> getGraph(String s);
}
