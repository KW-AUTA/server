package com.auta.server.domain.test;

import com.auta.server.domain.page.Page;
import com.auta.server.domain.project.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Test {
    private Long id;
    private Project project;
    private Page page;
    private TestType testType;
    private TestStatus testStatus;
    private String failReason;

    private String triggerSelector;
    private String expectedDestination;
    private String actualDestination;

    private String trigger;
    private String expectedAction;
    private String actualAction;

    private String componentName;

    public boolean isPassed() {
        return testStatus.equals(TestStatus.PASSED);
    }

    public boolean isFailed() {
        return testStatus.equals(TestStatus.FAILED);
    }

}
