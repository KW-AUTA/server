package com.auta.server.api.service.user.response;

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
}
