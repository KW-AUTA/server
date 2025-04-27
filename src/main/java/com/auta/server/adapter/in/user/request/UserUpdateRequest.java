package com.auta.server.adapter.in.user.request;

import com.auta.server.api.service.user.request.UserUpdateServiceRequest;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    private String username;

    private String address;

    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"
    )
    private String phoneNumber;

    @Builder
    public UserUpdateRequest(String email, String username, String address, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public UserUpdateCommand toCommand() {
        return UserUpdateCommand.builder()
                .email(email)
                .username(username)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }

    public UserUpdateServiceRequest toServiceRequest() {
        return UserUpdateServiceRequest.builder()
                .email(email)
                .username(username)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
