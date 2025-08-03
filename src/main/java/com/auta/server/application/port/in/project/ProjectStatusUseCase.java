package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.ProjectStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ProjectStatusUseCase {
    SseEmitter stream(Long projectId);

    void sendStatus(Long projectId, ProjectStatus projectStatus);
}
