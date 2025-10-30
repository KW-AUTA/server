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
        log.info("비동기 기능 테스트 시작 - Project ID: {}", projectId);

        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        log.info("프로젝트 조회 완료 - Project ID: {}, Root Page: {}, Service URL: {}",
                projectId, project.getRootFigmaPage(), project.getServiceUrl());

        TestCollector collector = new TestCollector(fastApiPort, project);
        collector.collect(project.getRootFigmaPage(), project.getServiceUrl())
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(ignored -> {
                    List<Page> pages = collector.getPages();
                    List<Test> tests = collector.getTests();

                    log.info("테스트 수집 완료 - Project ID: {}, 페이지 수: {}, 테스트 수: {}",
                            projectId, pages.size(), tests.size());

                    testSaver.saveAll(pages, tests);
                    log.info("테스트 저장 완료 - Project ID: {}", projectId);

                    projectResultService.applyTestResult(projectId, tests);
                    log.info("기능 테스트 성공 - Project ID: {}", projectId);
                })
                .doOnError(e -> {
                    projectResultService.markTestAsFailed(projectId);
                    log.error("기능 테스트 실패 - Project ID: {}", projectId, e);
                })
                .subscribe();
    }

    public void executeUITest(Long projectId) {
        log.info("UI/UX 테스트 시작 - Project ID: {}", projectId);

        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        log.info("프로젝트 조회 완료 - Project ID: {}, Figma JSON: {}", projectId, project.getFigmaJson());

        fastApiPort.requestUITest(project.getFigmaJson())
                .subscribe(response -> {
                    log.info("UI/UX 테스트 응답 수신 - Project ID: {}, Usability Score: {}, 평가 항목 수: {}",
                            projectId, response.getUsabilityScore(), response.getEvaluations().size());

                    Project latest = projectPort.findById(projectId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

                    uiTestSaver.saveAll(latest, response.getEvaluations());
                    log.info("UI 테스트 결과 저장 완료 - Project ID: {}", projectId);

                    projectResultService.applyUITestResult(projectId, response.getUsabilityScore());
                    log.info("UI/UX 테스트 성공 - Project ID: {}", projectId);
                }, error -> {
                    projectResultService.markTestAsFailed(projectId);
                    log.error("UI/UX 테스트 실패 - Project ID: {}, Error: {}", projectId, error.getMessage(), error);
                });
    }
}

