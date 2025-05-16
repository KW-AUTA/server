package com.auta.server.application.port.in.project;

import com.auta.server.application.port.in.project.dto.ProjectDetailDto;
import com.auta.server.application.port.in.project.dto.ProjectSummaryDto;
import com.auta.server.application.port.in.project.dto.ProjectTestDetailDto;
import com.auta.server.application.port.in.project.dto.ProjectTestSummaryDto;
import java.util.List;

public interface ProjectQueryUseCase {

    List<ProjectSummaryDto> getProjectSummaryList(String email, String projectName, String sortBy, Long cursor);

    ProjectDetailDto getProjectDetail(Long projectId);

    List<ProjectTestSummaryDto> getProjectTestSummaryList(String email, String projectName, String sortBy, Long cursor);

    ProjectTestDetailDto getProjectTestDetail(Long projectId);
}
