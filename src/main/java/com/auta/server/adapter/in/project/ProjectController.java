package com.auta.server.adapter.in.project;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.adapter.in.project.response.ProjectResponse;
import com.auta.server.application.port.in.project.ProjectUseCase;
import jakarta.validation.Valid;
import java.time.LocalDate;
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

    private final ProjectUseCase projectUseCase;

    @PostMapping("/api/v1/projects")
    public ApiResponse<ProjectResponse> crateProject(@Valid @RequestBody ProjectRequest request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate registeredDate = LocalDate.now();
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.",
                ProjectResponse.from(projectUseCase.createProject(request.toCommand(), email, registeredDate)));
    }

    @PutMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long projectId,
                                                      @RequestBody ProjectRequest request) {
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.",
                ProjectResponse.from(projectUseCase.updateProject(request.toCommand(), projectId)));
    }

    @DeleteMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> deleteProject(@PathVariable Long projectId) {
        projectUseCase.deleteProject(projectId);
        return ApiResponse.ok("프로젝트 삭제가 완료되었습니다.");
    }
}
