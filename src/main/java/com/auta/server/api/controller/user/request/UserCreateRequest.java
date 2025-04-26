package com.auta.server.api.controller.user.request;

import com.auta.server.api.service.user.request.UserCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    private String username;

    @Builder
    private UserCreateRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserCreateServiceRequest toServiceRequest() {
        return UserCreateServiceRequest.builder().email(email).password(password).username(username).build();
    }
}
