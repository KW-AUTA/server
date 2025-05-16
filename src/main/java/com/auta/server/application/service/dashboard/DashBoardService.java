package com.auta.server.application.service.dashboard;

import com.auta.server.adapter.in.dashboard.reponse.DashboardResponse;
import com.auta.server.application.port.in.dashboard.DashboardUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardService implements DashboardUserCase {

    @Override
    public DashboardResponse getDashBoardData(String email) {
        return null;
    }
}
