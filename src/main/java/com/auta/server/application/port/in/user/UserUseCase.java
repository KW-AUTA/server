package com.auta.server.application.port.in.user;

import com.auta.server.adapter.in.user.response.UserResponse;
import com.auta.server.domain.user.User;

public interface UserUseCase {
    User createUser(UserCreateCommand command);

    UserResponse getUser(String email);

    UserResponse updateUser(UserUpdateCommand command, String email);

    void deleteUser(String email);
}
