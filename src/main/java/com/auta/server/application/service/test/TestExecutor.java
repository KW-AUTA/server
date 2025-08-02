package com.auta.server.application.service.test;

import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.service.project.ProjectResultService;
import com.auta.server.application.service.uitest.UITestSaver;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestExecutor {

    private final ProjectPort projectPort;
    private final FastApiPort fastApiPort;

    private final ProjectResultService projectResultService;
    private final TestSaver testSaver;
    private final UITestSaver uiTestSaver;

    public void executeAsyncTest(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        TestCollector collector = new TestCollector(fastApiPort, project);
        collector.collect(project.getRootFigmaPage(), project.getServiceUrl())
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(ignored -> {
                    List<Page> pages = collector.getPages();
                    List<Test> tests = collector.getTests();
                    testSaver.saveAll(pages, tests);
                    projectResultService.applyTestResult(projectId, tests);
                })
                .doOnError(e -> {
                    projectResultService.markTestAsFailed(projectId);
                    log.error("기능 테스트 실패", e);
                })
                .subscribe();
    }

    public void executeUITest(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        fastApiPort.requestUITest(project.getFigmaJson())
                .subscribe(response -> {
                    Project latest = projectPort.findById(projectId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
                    uiTestSaver.saveAll(latest, response.getEvaluations());
                    projectResultService.applyUITestResult(projectId, response.getUsabilityScore());
                }, error -> {
                    projectResultService.markTestAsFailed(projectId);
                    log.error("UI/UX 테스트 중 오류 발생: {}", error.getMessage(), error);
                });
    }
}

