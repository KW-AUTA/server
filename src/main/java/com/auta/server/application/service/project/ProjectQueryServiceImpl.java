package com.auta.server.application.service.project;

import com.auta.server.adapter.in.project.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectSummariesResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import org.springframework.stereotype.Service;

@Service
public class ProjectQueryServiceImpl implements ProjectQueryUseCase {
    @Override
    public ProjectSummariesResponse getProjectSummaryList(String projectName, String sortBy, Integer cursor) {
        return null;
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
