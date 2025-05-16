package com.auta.server.adapter.in.dashboard;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.dashboard.reponse.DashboardResponse;
import com.auta.server.adapter.in.security.SecurityUtil;
import com.auta.server.application.port.in.dashboard.DashboardUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardUserCase dashboardUserCase;

    @GetMapping("/api/v1/home")
    public ApiResponse<DashboardResponse> getDashBoardData() {
        String email = SecurityUtil.getCurrentPrinciple();
        return ApiResponse.ok("메인페이지 대시보드 조회가 완료되었습니다.", dashboardUserCase.getDashBoardData(email));
    }
}
