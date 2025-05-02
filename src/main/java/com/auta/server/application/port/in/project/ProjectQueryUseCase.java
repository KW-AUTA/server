package com.auta.server.application.port.in.project;

import com.auta.server.adapter.in.project.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectSummariesResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;

public interface ProjectQueryUseCase {

    ProjectSummariesResponse getProjectSummaryList(String projectName, String sortBy, Integer cursor);

    ProjectDetailResponse getProjectDetail(Long projectId);

    ProjectTestSummariesResponse getProjectTestSummaryList(String projectName, String sortBy, Integer cursor);

    ProjectTestDetailResponse getProjectTestDetail(Long projectId);

    PageTestResponse getPageTestDetail(Long pageId);
}
