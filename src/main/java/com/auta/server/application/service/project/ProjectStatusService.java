package com.auta.server.application.service.project;

import com.auta.server.adapter.in.project.response.ProjectStatusSSEResponse;
import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import com.auta.server.domain.project.Project;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectStatusService implements ProjectStatusUseCase {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> heartbeats = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private static final long HEARTBEAT_SEC = 30;

    @Override
    public SseEmitter stream(String email) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(email, emitter);

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            SseEmitter current = emitters.get(email);
            if (current == null) {
                return;
            }
            try {
                current.send(SseEmitter.event()
                        .name("heartbeat")
                        .id(String.valueOf(System.currentTimeMillis()))
                        .reconnectTime(5000)
                        .data("ping", MediaType.TEXT_PLAIN));
            } catch (IOException ex) {
                cleanup(email, current, ex);
            }
        }, HEARTBEAT_SEC, HEARTBEAT_SEC, TimeUnit.SECONDS);
        heartbeats.put(email, future);

        emitter.onTimeout(() -> cleanup(email, emitter, null));
        emitter.onCompletion(() -> cleanup(email, emitter, null));
        emitter.onError(ex -> cleanup(email, emitter, ex));
        return emitter;
    }

    @Override
    public void sendStatus(String email, List<Project> projects) {
        SseEmitter emitter = emitters.get(email);
        log.info("sendStatus SSE");
        if (emitter == null) {
            log.info("emitter not found");
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

    private void cleanup(String email, SseEmitter emitter, Throwable cause) {
        if (cause != null) {
            log.debug("SSE cleanup {} due to {}", email, cause.toString());
        }
        emitters.remove(email, emitter);
        ScheduledFuture<?> f = heartbeats.remove(email);
        if (f != null) {
            f.cancel(true);
        }
        try {
            emitter.complete();
        } catch (Exception ignore) {
        }
    }
}
