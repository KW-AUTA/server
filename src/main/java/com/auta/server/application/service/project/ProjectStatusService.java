package com.auta.server.application.service.project;

import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import com.auta.server.domain.project.ProjectStatus;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class ProjectStatusService implements ProjectStatusUseCase {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter stream(Long projectId) {
        SseEmitter emitter = new SseEmitter(600_000L);
        emitters.put(projectId, emitter);

        emitter.onTimeout(() -> emitters.remove(projectId));
        emitter.onCompletion(() -> emitters.remove(projectId));
        return emitter;
    }

    @Override
    public void sendStatus(Long projectId, ProjectStatus projectStatus) {
        SseEmitter emitter = emitters.get(projectId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("projectStatus")
                        .data(projectStatus));
            } catch (IOException e) {
                emitters.remove(projectId);
            }
        }
    }
}
