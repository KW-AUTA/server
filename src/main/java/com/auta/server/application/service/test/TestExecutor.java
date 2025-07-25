package com.auta.server.application.service.test;

import com.auta.server.adapter.out.s3.S3Adapter;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.application.port.out.persistence.ui.UITestPort;
import com.auta.server.application.service.project.ProjectResultService;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.ui.UITest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestExecutor {

    private final ProjectPort projectPort;
    private final FastApiPort fastApiPort;
    private final PagePort pagePort;
    private final TestPort testPort;
    private final UITestPort uiTestPort;
    private final ProjectResultService projectResultService;
    private final S3Adapter s3Adapter;

    public void executeAsyncTest(Long projectId) {
        try {
            Project project = projectPort.findById(projectId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
            TestCollector collector = new TestCollector(fastApiPort, project);
            collector.collect(project.getRootFigmaPage(), project.getServiceUrl());

            List<Page> savedPages = pagePort.saveAll(collector.getPages());
            List<Test> tests = collector.getTests();
            reassignPages(tests, savedPages);
            testPort.saveAll(tests);

            project.updateTestRate(tests);
            projectPort.update(project);

            projectResultService.applyTestResult(projectId);
        } catch (Exception e) {
            projectResultService.updateStatus(projectId, ProjectStatus.ERROR);
            log.info("기능 테스트 오류");
        }
    }

    private void reassignPages(List<Test> tests, List<Page> savedPages) {
        Map<String, Page> pageMap = savedPages.stream()
                .collect(Collectors.toMap(Page::getPageName, Function.identity()));

        for (Test test : tests) {
            Page original = test.getPage();
            Page saved = pageMap.get(original.getPageName());
            test.reassignPage(saved);
        }
    }

    public void executeUITest(Long projectId) {
        Project project = projectPort.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        fastApiPort.requestUITest(project.getFigmaJson())
                .subscribe(response -> {
                    List<UITest> uiTests = response.getEvaluations().stream()
                            .map(dto -> UITest.builder()
                                    .UIPageUrl(s3Adapter.upload(dto.getHighlightImageUrl()))
                                    .UIDescription(dto.getFrameSummary())
                                    .project(project)
                                    .build())
                            .toList();

                    uiTestPort.saveAll(uiTests);

                    project.updateScore(response.getUsabilityScore());
                    projectPort.update(project);
                    projectResultService.applyUITestResult(projectId);
                }, error -> {
                    projectResultService.updateStatus(projectId, ProjectStatus.ERROR);
                    log.error("UI/UX 테스트 중 오류 발생: {}", error.getMessage(), error);

                });
    }
}

