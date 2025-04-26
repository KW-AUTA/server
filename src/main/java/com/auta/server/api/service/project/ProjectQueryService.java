package com.auta.server.api.service.project;

import com.auta.server.api.service.project.response.PageTestResponse;
import com.auta.server.api.service.project.response.ProjectDetailResponse;
import com.auta.server.api.service.project.response.ProjectSummariesResponse;
import com.auta.server.api.service.project.response.ProjectTestDetailResponse;
import com.auta.server.api.service.project.response.ProjectTestSummariesResponse;
import org.springframework.stereotype.Service;

@Service
public class ProjectQueryService {

    public ProjectSummariesResponse getProjectSummaryList(String projectName, String sortBy, Integer cursor) {
        return null;
    }

    public ProjectDetailResponse getProjectDetail(Long projectId) {
        return null;
    }

    public ProjectTestSummariesResponse getProjectTestSummaryList(String projectName, String sortBy, Integer cursor) {
        return null;
    }

    public ProjectTestDetailResponse getProjectTestDetail(Long projectId) {
        return null;
    }

    public PageTestResponse getPageTestDetail(Long pageId) {
        return null;
    }
}
