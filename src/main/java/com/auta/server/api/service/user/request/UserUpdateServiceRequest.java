package com.auta.server.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateServiceRequest {
    private String email;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;

    @Builder
    public UserUpdateServiceRequest(String email, String password, String username, String address,
                                    String phoneNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
