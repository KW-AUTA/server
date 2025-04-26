package com.auta.server.api.controller.auth.request;

import com.auta.server.api.service.auth.request.AuthServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Builder
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthServiceRequest toServiceRequest() {
        return AuthServiceRequest.builder().email(email).password(password).build();
    }
}
