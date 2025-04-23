package com.auta.server.api.service.user;

import com.auta.server.api.service.user.request.UserCreateServiceRequest;
import com.auta.server.api.service.user.request.UserUpdateServiceRequest;
import com.auta.server.api.service.user.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserResponse createUser(UserCreateServiceRequest request) {
        return null;
    }

    public UserResponse updateUser(UserUpdateServiceRequest request, String user) {
        return null;
    }

    public UserResponse getUser(String user) {
        return null;
    }

    public void deleteUser(String user) {
    }
}
