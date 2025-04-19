package com.auta.server.application.port.in.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateCommand {
    private String email;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;
}