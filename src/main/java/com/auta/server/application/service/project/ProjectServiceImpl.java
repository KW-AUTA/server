package com.auta.server.application.service.project;

import com.auta.server.adapter.out.fastapi.response.ComponentMappingResponse;
import com.auta.server.adapter.out.fastapi.response.ComponentMappingResponse.MappingInfo;
import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.application.port.out.persistence.user.UserPort;
import com.auta.server.application.port.out.s3.S3Port;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    private final PagePort pagePort;
    private final S3Port s3Port;
    private final FastApiPort fastApiPort;

    @Async
    @Override
    @Transactional
    public void executeTest(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeStatus(ProjectStatus.IN_PROGRESS);
        dfs(project.getRootFigmaPage(), project.getServiceUrl(), new HashSet<>(), project);
    }

    private void dfs(String currentPage, String currentUrl,
                     Set<String> visited, Project project) {

        if (visited.contains(currentPage)) {
            return;
        }

        visited.add(currentPage);
        Page page = pagePort.save(Page.of(project, currentPage, currentUrl));

        ComponentMappingResponse response = fastApiPort.requestComponentMapping(currentUrl, currentPage,
                project.getId());
        List<MappingInfo> mappings = response.getMappings();
        List<Test> tests = mappings.stream()
                .map(mappingInfo -> Test.builder()
                        .project(project)
                        .page(page)
                        .testType(TestType.MAPPING)
                        .testStatus(mappingInfo.isSuccess() ? TestStatus.PASSED : TestStatus.FAILED)
                        .failReason(mappingInfo.getFailReason())
                        .componentName(mappingInfo.getComponentName())
                        .build()
                ).toList();
        List<Test> savedTests = testPort.saveAll(tests);
        List<MappingInfo> routingInfos = mappings.stream().filter(MappingInfo::isRouting)
                .toList();

        for (MappingInfo routing : routingInfos) {
            String triggerSelector = routing.getComponentName();
            String destinationPage = routing.getDestinationFigmaPage();
            String destinationUrl = routing.getDestinationUrl();
            if (!destinationUrl.isBlank()) {
                dfs(destinationPage, destinationUrl, visited, project);
            }
        }
    }

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
