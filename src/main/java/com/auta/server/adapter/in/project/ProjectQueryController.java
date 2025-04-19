package com.auta.server.adapter.in.project;

import com.auta.server.api.service.project.response.PageTestResponse;
import com.auta.server.api.service.project.response.ProjectDetailResponse;
import com.auta.server.api.service.project.response.ProjectSummariesResponse;
import com.auta.server.api.service.project.response.ProjectTestDetailResponse;
import com.auta.server.api.service.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.service.ProjectQueryService;
import com.auta.server.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectQueryController {

    private final ProjectQueryService projectQueryService;

    @GetMapping("/api/v1/projects")
    public ApiResponse<ProjectSummariesResponse> getProjectSummaryList(
            @RequestParam String projectName,
            @RequestParam String sortBy,
            @RequestParam Integer cursor) {
        return ApiResponse.ok("프로젝트 리스트 조회가 완료되었습니다.",
                projectQueryService.getProjectSummaryList(projectName, sortBy, cursor));
    }

    @GetMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectDetailResponse> getProjectDetail(
            @PathVariable Long projectId
    ) {
        return ApiResponse.ok("프로젝트 세부 조회가 완료되었습니다.",
                projectQueryService.getProjectDetail(projectId));
    }

    @GetMapping("/api/v1/projects/tests")
    public ApiResponse<ProjectTestSummariesResponse> getProjectTestSummaryList(
            @RequestParam String projectName,
            @RequestParam String sortBy,
            @RequestParam Integer cursor
    ) {
        return ApiResponse.ok("프로젝트 테스트 리스트 조회가 완료되었습니다.",
                projectQueryService.getProjectTestSummaryList(projectName, sortBy, cursor));
    }

    @GetMapping("/api/v1/projects/tests/{projectId}")
    public ApiResponse<ProjectTestDetailResponse> getProjectTestDetail(@PathVariable Long projectId) {
        return ApiResponse.ok("프로젝트 테스트 세부 조회가 완료되었습니다.",
                projectQueryService.getProjectTestDetail(projectId));
    }

    @GetMapping("/api/v1/pages/{pageId}")
    public ApiResponse<PageTestResponse> getPageTestDetail(@PathVariable Long pageId) {
        return ApiResponse.ok("프로젝트 페이지 테스트 세부 조회가 완료되었습니다.",
                projectQueryService.getPageTestDetail(pageId));
    }


}