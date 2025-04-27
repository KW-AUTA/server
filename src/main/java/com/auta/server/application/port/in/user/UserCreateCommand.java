package com.auta.server.application.port.in.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final String username;
}
