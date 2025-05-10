package com.auta.server.application.service.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectUseCase {

    private final ProjectPort projectPort;
    private final UserPort userPort;
    private final TestPort testPort;

    @Override
    public Project createProject(ProjectCommand command, String email, LocalDate registeredDate) {
        User user = userPort.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        Project project = createProjectDomain(command, registeredDate, user);

        return projectPort.save(project);
    }

    @Override
    public Project updateProject(ProjectCommand command, Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        project.update(command);

        return projectPort.update(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        testPort.deleteAllByProjectId(project.getId());

        projectPort.delete(project);
    }

    @Override
    public void executeTest(Long projectId) {
        
    }

    private Project createProjectDomain(ProjectCommand command, LocalDate registeredDate, User user) {
        return Project.builder()
                .user(user)
                .figmaUrl(command.getFigmaUrl())
                .rootFigmaPage(command.getRootFigmaPage())
                .serviceUrl(command.getServiceUrl())
                .projectName(command.getProjectName())
                .description(command.getDescription())
                .projectCreatedDate(registeredDate)
                .expectedTestExecution(command.getExpectedTestExecution())
                .projectEnd(command.getProjectEnd())
                .projectStatus(ProjectStatus.NOT_STARTED)
                .testExecuteTime(LocalDateTime.of(2024, 4, 25, 12, 11))
                .build();
    }
}
