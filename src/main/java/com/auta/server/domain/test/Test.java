package com.auta.server.domain.test;

import com.auta.server.adapter.out.fastapi.response.MappingResponse.InteractionMappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.RoutingMappingInfo;
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
    private String detailInfo;

    private String expectedDestination;
    private String actualDestination;

    private String expectedAction;
    private String actualAction;

    private String componentName;

    public boolean isPassed() {
        return testStatus.equals(TestStatus.PASSED);
    }

    public boolean isFailed() {
        return testStatus.equals(TestStatus.FAILED);
    }

    public static Test ofInteractionResult(Project project, Page page, InteractionMappingInfo info) {
        return Test.builder()
                .project(project)
                .page(page)
                .testType(TestType.INTERACTION)
                .testStatus(info.isSuccess() ? TestStatus.PASSED : TestStatus.FAILED)
                .failReason(info.getFailReason())
                .detailInfo((info.getDetailInfo()))
                .componentName(info.getComponentName())
                .expectedAction(info.getExpectedAction())
                .actualAction(info.getActualAction())
                .build();
    }

    public static Test ofMappingResult(Project project, Page page, MappingInfo info) {
        return Test.builder()
                .project(project)
                .page(page)
                .testType(TestType.MAPPING)
                .testStatus(info.isSuccess() ? TestStatus.PASSED : TestStatus.FAILED)
                .failReason(info.getFailReason())
                .detailInfo((info.getDetailInfo()))
                .componentName(info.getComponentName())
                .build();
    }

    public static Test ofRoutingResult(Project project, Page page, RoutingMappingInfo info) {
        return Test.builder()
                .project(project)
                .page(page)
                .testType(TestType.ROUTING)
                .testStatus(info.isSuccess() ? TestStatus.PASSED : TestStatus.FAILED)
                .failReason(info.getFailReason())
                .detailInfo((info.getDetailInfo()))
                .componentName(info.getComponentName())
                .expectedDestination(info.getDestinationUrl())
                .actualDestination(info.getActualUrl())
                .build();
    }

    public void reassignPage(Page newPage) {
        this.page = newPage;
    }
}
