package com.auta.server.application.service.project;

import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import com.auta.server.application.port.in.project.dto.ProjectDetailDto;
import com.auta.server.application.port.in.project.dto.ProjectSummaryDto;
import com.auta.server.application.port.in.project.dto.ProjectTestDetailDto;
import com.auta.server.application.port.in.project.dto.ProjectTestSummaryDto;
import com.auta.server.application.port.out.page.PagePort;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestCountSummary;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectQueryServiceImpl implements ProjectQueryUseCase {
    private static final int PAGE_SIZE = 10;
    private final ProjectPort projectPort;
    private final PagePort pagePort;
    private final TestPort testPort;

    @Override
    public List<ProjectSummaryDto> getProjectSummaryList(String projectName, String sortBy, Long cursor) {
        List<Project> projects = projectPort.findByProjectNameWithPaging(projectName, sortBy, cursor, PAGE_SIZE);
        return projects.stream().map(ProjectSummaryDto::from).toList();
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        List<Page> pages = pagePort.findAllByProjectId(projectId);
        List<Test> tests = testPort.findAllByProjectId(projectId);

        TestCountSummary testCountSummary = TestCountSummary.from(tests);

        return ProjectDetailDto.of(project, pages, testCountSummary);
    }

    @Override
    public List<ProjectTestSummaryDto> getProjectTestSummaryList(String projectName, String sortBy, Long cursor) {
        List<Project> projects = projectPort.findByProjectNameWithPaging(projectName, sortBy, cursor, PAGE_SIZE);
        return projects.stream()
                .map(project -> Map.entry(project, testPort.findAllByProjectId(project.getId())))
                .filter(entry -> entry.getValue().stream()
                        .allMatch(test -> test.getTestStatus().isCompleted()))
                .map(entry -> {
                    TestCountSummary testCountSummary = TestCountSummary.from(entry.getValue());
                    return ProjectTestSummaryDto.of(entry.getKey(), testCountSummary);
                })
                .toList();
    }

    @Override
    public ProjectTestDetailDto getProjectTestDetail(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        
        List<Page> pages = pagePort.findAllByProjectId(projectId);

        List<Test> tests = testPort.findAllByProjectId(projectId);
        TestCountSummary testCountSummary = TestCountSummary.from(tests);

        return ProjectTestDetailDto.from(project, pages, testCountSummary);
    }
}
