package com.auta.server.application.service.project;

import com.auta.server.adapter.in.project.response.ProjectStatusSSEResponse;
import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import com.auta.server.domain.project.Project;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectStatusService implements ProjectStatusUseCase {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter stream(String email) {
        SseEmitter emitter = new SseEmitter(600_000L);
        emitters.put(email, emitter);

        emitter.onTimeout(() -> emitters.remove(email));
        emitter.onCompletion(() -> emitters.remove(email));
        return emitter;
    }

    @Override
    public void sendStatus(String email, List<Project> projects) {
        SseEmitter emitter = emitters.get(email);
        log.info("log: sendStatus SSE");

        if (emitter == null) {
            log.info("log: emitter None SSE");
            return;
        }
        List<ProjectStatusSSEResponse> payload = projects.stream()
                .map(ProjectStatusSSEResponse::from)
                .toList();

        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .name("projectStatus")
                .id(String.valueOf(System.currentTimeMillis()))
                .reconnectTime(5000)
                .data(payload);

        try {
            emitter.send(event);
        } catch (IOException e) {
            emitters.remove(email);
            emitter.completeWithError(e);
        }
    }
}
