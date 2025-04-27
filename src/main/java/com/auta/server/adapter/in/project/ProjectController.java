package com.auta.server.adapter.in.project;

import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.api.service.project.response.ProjectResponse;
import com.auta.server.application.service.ProjectService;
import com.auta.server.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/api/v1/projects")
    public ApiResponse<ProjectResponse> crateProject(@Valid @RequestBody ProjectRequest request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.", projectService.createProject(request.toServiceEntity(), email));
    }

    @PutMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long projectId,
                                                      @RequestBody ProjectRequest request) {
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.", projectService.updateProject(request.toServiceEntity(), projectId));
    }

    @DeleteMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ApiResponse.ok("프로젝트 삭제가 완료되었습니다.");
    }
}
