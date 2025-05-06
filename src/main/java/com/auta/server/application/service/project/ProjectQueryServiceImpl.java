package com.auta.server.application.service.project;

import com.auta.server.adapter.in.page.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.application.port.in.project.ProjectDetailDto;
import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import com.auta.server.application.port.in.project.ProjectTestSummaryDto;
import com.auta.server.application.port.out.page.PagePort;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    public List<ProjectSummaryQueryDto> getProjectSummaryList(String projectName, String sortBy, Long cursor) {
        List<Project> projects = projectPort.findByProjectNameWithPaging(projectName, sortBy, cursor, PAGE_SIZE);
        return projects.stream().map(ProjectSummaryQueryDto::from).toList();
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        List<Page> pages = pagePort.findAllIdsByProjectId(projectId);
        List<Test> tests = testPort.findAllByProjectId(projectId);

        Map<TestType, Long> testCounts = extractTestCounts(tests);

        return ProjectDetailDto.of(project, pages, testCounts);
    }

    @Override
    public List<ProjectTestSummaryDto> getProjectTestSummaryList(String projectName, String sortBy, Long cursor) {
        List<Project> projects = projectPort.findByProjectNameWithPaging(projectName, sortBy, cursor, PAGE_SIZE);
        return projects.stream().map(project -> {
            List<Test> tests = testPort.findAllByProjectId(project.getId());
            Map<TestType, Map<Boolean, Long>> testCountsGroupedByTypeAndStatus = tests.stream()
                    .collect(Collectors.groupingBy(Test::getTestType, Collectors.groupingBy(
                            Test::isPassed,
                            Collectors.counting()
                    )));
            return ProjectTestSummaryDto.of(project, testCountsGroupedByTypeAndStatus);
        }).toList();
    }

    @Override
    public ProjectTestDetailResponse getProjectTestDetail(Long projectId) {
        return null;
    }

    @Override
    public PageTestResponse getPageTestDetail(Long pageId) {
        return null;
    }

    private Map<TestType, Long> extractTestCounts(List<Test> tests) {
        return tests.stream()
                .collect(Collectors.groupingBy(Test::getTestType, Collectors.counting()));
    }
}
