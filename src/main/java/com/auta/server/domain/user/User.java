package com.auta.server.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;

    public void update(String email, String username, String password, String address, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
