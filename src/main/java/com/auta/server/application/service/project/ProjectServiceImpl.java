package com.auta.server.application.service.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.s2.S3Port;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectUseCase {

    private final ProjectPort projectPort;
    private final UserPort userPort;
    private final TestPort testPort;
    private final S3Port s3Port;

    @Override
    public Project createProject(ProjectCommand command, MultipartFile jsonFile, String email,
                                 LocalDate registeredDate) {
        User user = userPort.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
        String jsonUrl = s3Port.upload(jsonFile);
        Project project = createProjectDomain(command, registeredDate, user, jsonUrl);

        return projectPort.save(project);
    }

    @Override
    public Project updateProject(ProjectCommand command, MultipartFile jsonFile, Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        String oldFigmaJsonUrl = project.getFigmaJson();
        s3Port.delete(oldFigmaJsonUrl);
        String newFigmaJsonUrl = s3Port.upload(jsonFile);
        project.update(command, newFigmaJsonUrl);

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

    @Override
    public List<Project> findAllByUserId(Long userId) {
        return projectPort.findAllByUserId(userId);
    }

    private Project createProjectDomain(ProjectCommand command, LocalDate registeredDate, User user, String jsonUrl) {
        return Project.builder()
                .user(user)
                .figmaUrl(command.getFigmaUrl())
                .figmaJson(jsonUrl)
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
