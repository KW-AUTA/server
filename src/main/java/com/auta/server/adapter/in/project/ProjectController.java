package com.auta.server.adapter.in.project;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.adapter.in.project.response.ProjectResponse;
import com.auta.server.adapter.in.security.SecurityUtil;
import com.auta.server.application.port.in.project.ProjectUseCase;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;

    @PostMapping("/api/v1/projects/{projectId}/run-test")
    public ApiResponse<String> executeTest(@PathVariable Long projectId) {
        projectUseCase.executeTest(projectId);
        return ApiResponse.ok("프로젝트 테스트가 완료 되었습니다.");
    }

    @PostMapping("/api/v1/projects")
    public ApiResponse<ProjectResponse> crateProject(@Valid @RequestPart(value = "request") ProjectRequest request,
                                                     @RequestPart(value = "file") MultipartFile multipartFile) {
        String email = SecurityUtil.getCurrentPrinciple();
        LocalDate registeredDate = LocalDate.now();
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.",
                ProjectResponse.from(
                        projectUseCase.createProject(request.toCommand(), multipartFile, email, registeredDate)));
    }

    @PutMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long projectId,
                                                      @RequestPart(value = "request") ProjectRequest request,
                                                      @RequestPart(value = "file") MultipartFile multipartFile) {
        return ApiResponse.ok("프로젝트 생성이 완료되었습니다.",
                ProjectResponse.from(projectUseCase.updateProject(request.toCommand(), multipartFile, projectId)));
    }

    @DeleteMapping("/api/v1/projects/{projectId}")
    public ApiResponse<ProjectResponse> deleteProject(@PathVariable Long projectId) {
        projectUseCase.deleteProject(projectId);
        return ApiResponse.ok("프로젝트 삭제가 완료되었습니다.");
    }
}
