package com.auta.server.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private String email;
    private String username;
}
