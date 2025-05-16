package com.auta.server.application.port.in.dashboard;

import com.auta.server.adapter.in.dashboard.reponse.DashboardResponse;

public interface DashboardUserCase {
    DashboardResponse getDashBoardData(String email);
}
