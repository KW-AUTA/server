package com.auta.server.adapter.out.fastapi.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UITestRequest {
    private String figmaJsonUrl;
}
