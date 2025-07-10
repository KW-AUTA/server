package com.auta.server.application.service.project;

import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProjectResultService {
    private final ProjectPort projectPort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyTestResult(Long projectId, List<Test> tests) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.updateTestRate(tests);
        project.changeStatus(ProjectStatus.COMPLETED);
        projectPort.update(project);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatus(Long projectId, ProjectStatus status) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeStatus(status);
        projectPort.save(project);
    }
}

