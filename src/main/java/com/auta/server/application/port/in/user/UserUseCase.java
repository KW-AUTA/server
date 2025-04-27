package com.auta.server.application.port.in.user;

import com.auta.server.api.service.user.response.UserResponse;

public interface UserUseCase {
    UserResponse createUser(UserCreateCommand command);

    UserResponse getUser(String email);

    UserResponse updateUser(UserUpdateCommand command, String email);

    void deleteUser(String email);
}
