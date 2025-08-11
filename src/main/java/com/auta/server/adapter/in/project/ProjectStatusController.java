package com.auta.server.adapter.in.project;

import com.auta.server.adapter.in.security.SecurityUtil;
import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectStatusController {

    private final ProjectStatusUseCase projectStatusUseCase;

    @GetMapping("/api/v1/projects/status/stream")
    public SseEmitter streamStatus() {
        String email = SecurityUtil.getCurrentPrinciple();
        return projectStatusUseCase.stream(email);
    }
}
