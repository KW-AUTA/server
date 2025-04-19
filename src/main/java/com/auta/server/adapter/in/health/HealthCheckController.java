package com.auta.server.adapter.in.health;

import com.auta.server.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/v1/health")
    public ApiResponse<Void> checkHealth() {
        return ApiResponse.ok("서버 운영 중입니다!");
    }
}
