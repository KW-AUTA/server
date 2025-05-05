package com.auta.server.application.port.in.project;

import com.auta.server.adapter.in.page.response.PageTestResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import java.util.List;

public interface ProjectQueryUseCase {

    List<ProjectSummaryQueryDto> getProjectSummaryList(String projectName, String sortBy, Long cursor);

    ProjectDetailDto getProjectDetail(Long projectId);

    ProjectTestSummariesResponse getProjectTestSummaryList(String projectName, String sortBy, Integer cursor);

    ProjectTestDetailResponse getProjectTestDetail(Long projectId);

    PageTestResponse getPageTestDetail(Long pageId);
}
