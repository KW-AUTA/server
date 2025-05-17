package com.auta.server.application.port.out.fastapi;

import com.auta.server.adapter.out.fastapi.request.GraphRequest;
import com.auta.server.adapter.out.fastapi.request.MappingRequest;
import com.auta.server.adapter.out.fastapi.request.RoutingRequest;
import com.auta.server.adapter.out.fastapi.response.GraphResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.RoutingResponse;

public interface FastApiPort {

    GraphResponse callGraph(GraphRequest request);

    RoutingResponse callRouting(RoutingRequest request);

    MappingResponse callMapping(MappingRequest request);

}
