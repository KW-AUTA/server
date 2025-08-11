package com.auta.server.application.service.project;

import com.auta.server.adapter.out.persistence.projectprogress.ProjectTestProgressEntity;
import com.auta.server.adapter.out.persistence.projectprogress.ProjectTestProgressRepository;
import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectResultService {
    private final ProjectPort projectPort;
    private final ProjectTestProgressRepository projectTestProgressRepository;
    private final ProjectStatusUseCase projectStatusUseCase;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyTestResult(Long projectId, List<Test> tests) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        project.updateTestRate(tests);
        projectPort.update(project);

        ProjectTestProgressEntity progress = projectTestProgressRepository.findById(projectId)
                .orElseGet(() -> projectTestProgressRepository.save(new ProjectTestProgressEntity(projectId)));
        progress.updateTest(true);

        if (progress.isUiDone()) {
            updateStatus(projectId, ProjectStatus.COMPLETED);
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyUITestResult(Long projectId, int score) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        project.updateScore(score);
        projectPort.update(project);
        ;

        ProjectTestProgressEntity progress = projectTestProgressRepository.findById(projectId)
                .orElseGet(() -> projectTestProgressRepository.save(new ProjectTestProgressEntity(projectId)));
        progress.updateUITest(true);

        if (progress.isTestDone()) {
            updateStatus(projectId, ProjectStatus.COMPLETED);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markTestAsFailed(Long projectId) {
        updateStatus(projectId, ProjectStatus.ERROR);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markTestAsProgress(Long projectId) {
        updateStatus(projectId, ProjectStatus.IN_PROGRESS);
    }

    private void updateStatus(Long projectId, ProjectStatus status) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeStatus(status);
        projectPort.updateProjectStatus(project);
        User user = project.getUser();
        List<Project> allBySameUser = projectPort.findAllByUserId(user.getId());
        projectStatusUseCase.sendStatus(user.getEmail(), allBySameUser);
    }
}

