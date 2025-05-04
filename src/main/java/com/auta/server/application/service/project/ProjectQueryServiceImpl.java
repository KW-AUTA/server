package com.auta.server.application.service.project;

import com.auta.server.adapter.in.page.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import com.auta.server.domain.project.Project;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectQueryServiceImpl implements ProjectQueryUseCase {
    private static final int PAGE_SIZE = 10;
    private final ProjectPort projectPort;

    @Override
    public List<ProjectSummaryQueryDto> getProjectSummaryList(String projectName, String sortBy, Long cursor) {
        List<Project> projects = projectPort.findByProjectNameWithPaging(projectName, sortBy, cursor, PAGE_SIZE);
        return projects.stream().map(ProjectSummaryQueryDto::from).toList();
    }

    @Override
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        return null;
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
