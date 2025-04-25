package com.auta.server.api.controller.health;

import com.auta.server.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/v1/health")
    public ApiResponse<Void> checkHealth() {
        return ApiResponse.ok("서버 운영 중입니다!");
    }
}
