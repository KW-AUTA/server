package com.auta.server.application.service.dashboard;

import com.auta.server.adapter.in.dashboard.reponse.DashboardResponse;
import com.auta.server.application.port.in.dashboard.DashboardUserCase;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestCountSummary;
import com.auta.server.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardService implements DashboardUserCase {
    private final UserPort userPort;
    private final ProjectPort projectPort;
    private final TestPort testPort;

    @Override
    public DashboardResponse getDashBoardData(String email) {
        User user = userPort.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<Project> projects = projectPort.findAllByUserId(user.getId());
        List<Long> projectIds = projects.stream().map(Project::getId).toList();
        List<Test> tests = testPort.findAllByProjectIdInOrderByCreationTimeDesc(projectIds);
        TestCountSummary testCountSummary = TestCountSummary.from(tests);
        return DashboardResponse.from(projects, tests, testCountSummary);
    }
}
