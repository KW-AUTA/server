package com.auta.server.application.port.in.user;

import com.auta.server.domain.user.User;

public interface UserUseCase {
    User createUser(UserCreateCommand command);

    User getUser(String email);

    User updateUser(UserUpdateCommand command, String email);

    void deleteUser(String email);
}
