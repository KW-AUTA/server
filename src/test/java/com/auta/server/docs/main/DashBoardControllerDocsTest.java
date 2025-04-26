package com.auta.server.docs.main;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.api.controller.main.DashBoardController;
import com.auta.server.api.service.main.DashBoardService;
import com.auta.server.api.service.main.response.DashboardResponse;
import com.auta.server.docs.RestDocsSupport;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class DashBoardControllerDocsTest extends RestDocsSupport {

    private final DashBoardService dashBoardService = mock(DashBoardService.class);

    @Override
    protected Object initController() {
        return new DashBoardController(dashBoardService);
    }

    @DisplayName("대시보드 조회")
    @Test
    void getDashboardData() throws Exception {
        //given
        setMockSecurityContext();
        given(dashBoardService.getDashBoardData(anyString()))
                .willReturn(
                        DashboardResponse.builder()
                                .totalProjects(1)
                                .completedTests(1)
                                .incompleteTests(1)
                                .projects(List.of(
                                        DashboardResponse.ProjectInfo.builder()
                                                .projectId(1L)
                                                .projectName("UI 테스트 프로젝트")
                                                .administrator("example")
                                                .projectEnd(LocalDate.of(2024, 4, 4))
                                                .projectStatus(ProjectStatus.NOT_STARTED)
                                                .build()
                                ))
                                .tests(List.of(
                                        DashboardResponse.TestInfo.builder()
                                                .testId(11L)
                                                .projectName("UI 테스트 프로젝트")
                                                .pageName("메인페이지")
                                                .testType(TestType.INTERACTION)
                                                .testStatus(TestStatus.READY)
                                                .build()
                                ))
                                .build()

                );
        //when //then
        mockMvc.perform(
                        get("/api/v1/home")
                                .header("Authorization", "Bearer JwtToken")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("dashboard",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),

                                fieldWithPath("data.totalProjects").type(JsonFieldType.NUMBER)
                                        .description("전체 프로젝트 수"),
                                fieldWithPath("data.completedTests").type(JsonFieldType.NUMBER)
                                        .description("완료된 테스트 수"),
                                fieldWithPath("data.incompleteTests").type(JsonFieldType.NUMBER)
                                        .description("미완료 테스트 수"),

                                fieldWithPath("data.projects").type(JsonFieldType.ARRAY)
                                        .description("프로젝트 요약 목록"),
                                fieldWithPath("data.projects[].projectId").type(JsonFieldType.NUMBER)
                                        .description("프로젝트 ID"),
                                fieldWithPath("data.projects[].projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.projects[].administrator").type(JsonFieldType.STRING)
                                        .description("프로젝트 관리자 username"),
                                fieldWithPath("data.projects[].projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projects[].projectStatus").type(JsonFieldType.STRING)
                                        .description("프로젝트 상태"),

                                fieldWithPath("data.tests").type(JsonFieldType.ARRAY)
                                        .description("테스트 요약 목록"),
                                fieldWithPath("data.tests[].testId").type(JsonFieldType.NUMBER)
                                        .description("테스트 ID"),
                                fieldWithPath("data.tests[].projectName").type(JsonFieldType.STRING)
                                        .description("테스트가 속한 프로젝트 이름"),
                                fieldWithPath("data.tests[].pageName").type(JsonFieldType.STRING)
                                        .description("테스트가 속한 페이지 이름"),
                                fieldWithPath("data.tests[].testType").type(JsonFieldType.STRING)
                                        .description("테스트 타입 또는 수행 일시 (yyyy-MM-dd)"),
                                fieldWithPath("data.tests[].testStatus").type(JsonFieldType.STRING)
                                        .description("테스트 상태")
                        )
                ));
    }
}
