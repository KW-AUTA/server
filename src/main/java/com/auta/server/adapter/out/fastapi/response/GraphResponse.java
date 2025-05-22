package com.auta.server.adapter.out.fastapi.response;

import java.util.List;
import java.util.Map;

public record GraphResponse(Map<String, List<String>> graph) {
}