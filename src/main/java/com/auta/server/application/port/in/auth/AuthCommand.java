package com.auta.server.application.port.in.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCommand {
    private String email;
    private String password;

    @Builder
    public AuthCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
