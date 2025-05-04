package com.auta.server.adapter.in.project;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.project.response.ProjectDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectSummariesResponse;
import com.auta.server.adapter.in.project.response.ProjectTestDetailResponse;
import com.auta.server.adapter.in.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.port.in.project.ProjectQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectQueryController {

    private final ProjectQueryUseCase projectQueryUseCase;

    @GetMapping("/api/v1/projects")
    public ApiResponse<ProjectSummariesResponse> getProjectSummaryList(
            @RequestParam String projectName,
            @RequestParam String sortBy,
            @RequestParam Long cursor) {
        return ApiResponse.ok("프로젝트 리스트 조회가 완료되었습니다.",
                ProjectSummariesResponse.from(projectQueryUseCase.getProjectSummaryList(projectName, sortBy, cursor)));
    }

    @GetMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectDetailResponse> getProjectDetail(
            @PathVariable Long projectId
    ) {
        return ApiResponse.ok("프로젝트 세부 조회가 완료되었습니다.",
                projectQueryUseCase.getProjectDetail(projectId));
    }

    @GetMapping("/api/v1/projects/tests")
    public ApiResponse<ProjectTestSummariesResponse> getProjectTestSummaryList(
            @RequestParam String projectName,
            @RequestParam String sortBy,
            @RequestParam Integer cursor
    ) {
        return ApiResponse.ok("프로젝트 테스트 리스트 조회가 완료되었습니다.",
                projectQueryUseCase.getProjectTestSummaryList(projectName, sortBy, cursor));
    }

    @GetMapping("/api/v1/projects/tests/{projectId}")
    public ApiResponse<ProjectTestDetailResponse> getProjectTestDetail(@PathVariable Long projectId) {
        return ApiResponse.ok("프로젝트 테스트 세부 조회가 완료되었습니다.",
                projectQueryUseCase.getProjectTestDetail(projectId));
    }
}