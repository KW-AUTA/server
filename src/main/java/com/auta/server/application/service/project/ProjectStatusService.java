package com.auta.server.application.service.project;

import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectStatusService {
    private final ProjectPort projectPort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatus(Long projectId, ProjectStatus status) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeStatus(status);
        projectPort.save(project);
    }
}
