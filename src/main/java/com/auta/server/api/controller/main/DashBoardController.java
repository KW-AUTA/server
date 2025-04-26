package com.auta.server.api.controller.main;

import com.auta.server.api.ApiResponse;
import com.auta.server.api.service.main.DashBoardService;
import com.auta.server.api.service.main.response.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping("/api/v1/home")
    public ApiResponse<DashboardResponse> getDashBoardData() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApiResponse.ok("메인페이지 대시보드 조회가 완료되었습니다.", dashBoardService.getDashBoardData(email));
    }
}
