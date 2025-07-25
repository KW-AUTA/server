package com.auta.server.application.service.project;

import com.auta.server.adapter.out.persistence.projectprogress.ProjectTestProgressEntity;
import com.auta.server.adapter.out.persistence.projectprogress.ProjectTestProgressRepository;
import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.application.service.test.TestExecutor;
import com.auta.server.domain.project.ProjectStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTestService {

    private final TestPort testPort;
    private final PagePort pagePort;
    private final ProjectResultService projectResultService;
    private final TestExecutor testExecutor;
    private final ProjectTestProgressRepository projectTestProgressRepository;

    public void runTestInternal(Long projectId) {
        // 1. 기존 테스트 데이터 정리
        log.info("초기화 시작");
        testPort.deleteAllByProjectId(projectId);
        pagePort.deleteAllByProjectId(projectId);
        log.info("초기화 끝");

        // 2. 상태 업데이트
        log.info("상태 in_progress");
        projectResultService.updateStatus(projectId, ProjectStatus.IN_PROGRESS);
        projectTestProgressRepository.save(
                new ProjectTestProgressEntity(projectId, false, false)
        );

        // 3. 테스트 실행 (FastAPI 호출, Async 로직 포함)
        testExecutor.executeUITest(projectId);
        testExecutor.executeAsyncTest(projectId);
    }
}

