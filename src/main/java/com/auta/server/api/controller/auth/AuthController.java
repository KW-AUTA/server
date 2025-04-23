package com.auta.server.api.controller.auth;

import com.auta.server.api.ApiResponse;
import com.auta.server.api.controller.auth.request.AuthRequest;
import com.auta.server.api.service.auth.AuthService;
import com.auta.server.api.service.auth.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        return ApiResponse.ok(authService.login(request.toServiceRequest()));
    }
}
