package com.auta.server.application.service.project;

import com.auta.server.adapter.out.fastapi.request.InitRequest;
import com.auta.server.adapter.out.fastapi.response.InitResponse;
import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.application.port.out.persistence.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectUseCase {

    private final ProjectPort projectPort;
    private final UserPort userPort;
    private final TestPort testPort;
    private final FastApiPort fastApiPort;

    @Async
    @Override
    public void executeTest(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        InitResponse initResponse = fastApiPort.init(
                new InitRequest(
                        "https://www.figma.com/design/kn3tdCcgeCNnMwugoAiRc1/Untitled?node-id=0-1&p=f&t=yRbnqumbqbcSwdDK-0"));
        System.out.println(initResponse.fileKey());
        Map<String, List<String>> graph = fastApiPort.getGraph(initResponse.fileKey());
        System.out.println(graph);
    }

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
    public List<Project> findAllByUserId(Long userId) {
        return projectPort.findAllByUserId(userId);
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
                .build();
    }
}
