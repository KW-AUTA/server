package com.auta.server.application.service.project;

import com.auta.server.adapter.in.page.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import com.auta.server.application.port.out.page.PagePort;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import java.util.List;
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
    public Project getProjectDetail(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        List<Page> pages = pagePort.findAllIdsByProjectId(projectId);

        for (Page page : pages) {
            List<Test> tests = testPort.findAllByPageId(page.getId());
            page.addTests(tests);
        }
        
        project.addPages(pages);
        return project;
    }

    @Override
    public ProjectTestSummariesResponse getProjectTestSummaryList(String projectName, String sortBy, Integer cursor) {
        return null;
    }

    @Override
    public ProjectTestDetailResponse getProjectTestDetail(Long projectId) {
        return null;
    }

    @Override
    public PageTestResponse getPageTestDetail(Long pageId) {
        return null;
    }
}
