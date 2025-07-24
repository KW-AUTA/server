package com.auta.server.application.service.project;

import com.auta.server.domain.project.ProjectTestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectTestEventListener {
    private final ProjectTestService projectTestService;

    @Async
    @EventListener
    public void handle(ProjectTestEvent event) {
        projectTestService.runTestInternal(event.getProjectId());
    }
}
