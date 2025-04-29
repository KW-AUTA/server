package com.auta.server.adapter.in.user;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.user.request.UserCreateRequest;
import com.auta.server.adapter.in.user.request.UserUpdateRequest;
import com.auta.server.adapter.in.user.response.UserResponse;
import com.auta.server.application.port.in.user.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/api/v1/users")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok("회원 가입이 완료되었습니다",
                UserResponse.from(userUseCase.createUser(request.toCommand())));
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserResponse> getUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApiResponse.ok("유저 조회가 완료되었습니다.", userUseCase.getUser(email));
    }

    @PutMapping("/api/v1/users")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApiResponse.ok("유저 수정이 완료되었습니다.", userUseCase.updateUser(request.toCommand(), email));
    }

    @DeleteMapping("/api/v1/users")
    public ApiResponse<Void> deleteUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userUseCase.deleteUser(email);
        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.");
    }
}
