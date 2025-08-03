package com.auta.server.application.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auta.server.adapter.out.fastapi.response.MappingResponse;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.MappingInfo;
import com.auta.server.adapter.out.fastapi.response.MappingResponse.RoutingMappingInfo;
import com.auta.server.adapter.out.fastapi.response.UITestResponse;
import com.auta.server.adapter.out.fastapi.response.UITestResponse.Evaluation;
import com.auta.server.application.port.out.fastapi.FastApiPort;
import com.auta.server.application.port.out.persistence.project.ProjectPort;
import com.auta.server.application.service.project.ProjectResultService;
import com.auta.server.application.service.uitest.UITestSaver;
import com.auta.server.domain.project.Project;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TestExecutorTest {
    @Mock
    private ProjectPort projectPort;
    @Mock
    private FastApiPort fastApiPort;
    @Mock
    private TestSaver testSaver;
    @Mock
    private UITestSaver uiTestSaver;
    @Mock
    private ProjectResultService projectResultService;

    @InjectMocks
    private TestExecutor testExecutor;

    @DisplayName("프로젝트에 대해서 기능 Test가 실행되면 프로젝트 상태 변경 및 다른 기능이 동작하는 지 확인한다.\"")
    @Test
    void executeAsyncTest() {
        //given
        Long projectId = 1L;
        Project mockProject = mock(Project.class);

        when(projectPort.findById(projectId)).thenReturn(Optional.of(mockProject));

        MappingInfo mapping = RoutingMappingInfo.builder().componentName("componentId").isSuccess(true).build();
        when(fastApiPort.requestComponentMapping(any(), any(), any()))
                .thenReturn(Mono.just(MappingResponse.builder().mappings(List.of(mapping)).build()));
        //when
        testExecutor.executeAsyncTest(projectId);

        //then
        verify(testSaver, timeout(1000)).saveAll(anyList(), anyList());
        verify(projectResultService, timeout(1000)).applyTestResult(anyLong(), anyList());
    }

//    @DisplayName("프로젝트에 대해서 기능 Test가 FastApi 응답 실패시 프롲젝트 상태는 ERROR로 변경된다.")
//    @Test
//    void executeAsyncTest_whenFastApiFails_marksAsFailed() {
//        // given
//        Long projectId = 1L;
//
//        when(projectPort.findById(projectId)).thenReturn(Optional.of(mock(Project.class)));
//        when(fastApiPort.requestComponentMapping(any(), any(), any()))
//                .thenReturn(Mono.error(new RuntimeException("FastAPI Failure")));
//
//        // when
//        testExecutor.executeAsyncTest(projectId);
//
//        // then
//        await().atMost(Duration.ofSeconds(1)).untilAsserted(() -> {
//            verify(projectResultService).markTestAsFailed(projectId);
//        });
//    }

    @DisplayName("프로젝트에 대해서 UI Test가 실행되면 프로젝트 상태 변경 및 다른 기능이 동작하는 지 확인한다.")
    @Test
    void executeUITest() {
        //given
        Long projectId = 1L;
        Project mockProject = mock(Project.class);

        when(projectPort.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(mockProject.getFigmaJson()).thenReturn("figmaJson");

        UITestResponse response = UITestResponse.builder().evaluations(List.of(Evaluation.builder().build())).build();
        when(fastApiPort.requestUITest(anyString())).thenReturn(Mono.just(response));

        //when
        testExecutor.executeUITest(projectId);

        //then
        verify(uiTestSaver, timeout(1000)).saveAll(any(Project.class), anyList());
        verify(projectResultService, timeout(1000)).applyUITestResult(anyLong(), anyInt());
    }

    @DisplayName("프로젝트에 대해서 UI Test가 FastApi_응답_실패시 프롲젝트 상태는 ERROR로 변경된다.")
    @Test
    void executeUITestWithError() {
        //given
        Long projectId = 1L;
        Project mockProject = mock(Project.class);

        when(projectPort.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(mockProject.getFigmaJson()).thenReturn("figmaJson");

        when(fastApiPort.requestUITest(anyString()))
                .thenReturn(Mono.error(new RuntimeException("FastAPI 오류")));

        //when
        testExecutor.executeUITest(projectId);

        //then
        verify(projectResultService, timeout(1000)).markTestAsFailed(eq(projectId));

    }
}