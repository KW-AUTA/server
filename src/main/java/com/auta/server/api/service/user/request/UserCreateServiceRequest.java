package com.auta.server.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateServiceRequest {
    private String email;
    private String password;
    private String username;

    @Builder
    public UserCreateServiceRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
