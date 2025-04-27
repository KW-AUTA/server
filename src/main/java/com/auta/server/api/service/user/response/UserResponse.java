package com.auta.server.api.service.user.response;

import com.auta.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String address;
    private String phoneNumber;

    @Builder
    public UserResponse(Long id, String email, String username, String address, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
