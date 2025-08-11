package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.Project;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ProjectStatusUseCase {
    SseEmitter stream(String email);

    void sendStatus(String email, List<Project> projects);
}
