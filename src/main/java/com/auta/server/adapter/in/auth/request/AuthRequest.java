package com.auta.server.adapter.in.auth.request;

import com.auta.server.application.port.in.auth.AuthCommand;
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

    public AuthCommand toCommand() {
        return AuthCommand.builder().email(email).password(password).build();
    }
}
