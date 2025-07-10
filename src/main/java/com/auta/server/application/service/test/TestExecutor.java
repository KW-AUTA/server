package com.auta.server.application.service.test;

import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.application.service.project.ProjectResultService;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.Test;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@RequiredArgsConstructor
@Component
public class TestExecutor {

    private final ProjectPort projectPort;
    private final FastApiPort fastApiPort;
    private final PagePort pagePort;
    private final TestPort testPort;
    private final ProjectResultService projectResultService;

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

            projectResultService.applyTestResult(projectId, tests);
        } catch (Exception e) {
            projectResultService.updateStatus(projectId, ProjectStatus.ERROR);
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
}

