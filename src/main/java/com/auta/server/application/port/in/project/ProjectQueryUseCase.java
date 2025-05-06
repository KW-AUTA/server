package com.auta.server.application.port.in.project;

import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import java.util.List;

public interface ProjectQueryUseCase {

    List<ProjectSummaryQueryDto> getProjectSummaryList(String projectName, String sortBy, Long cursor);

    ProjectDetailDto getProjectDetail(Long projectId);

    List<ProjectTestSummaryDto> getProjectTestSummaryList(String projectName, String sortBy, Long cursor);

    ProjectTestDetailResponse getProjectTestDetail(Long projectId);
}
