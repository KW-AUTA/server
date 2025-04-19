package com.auta.server.docs.project;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.auta.server.adapter.in.project.ProjectQueryController;
import com.auta.server.api.service.project.response.PageTestResponse;
import com.auta.server.api.service.project.response.ProjectDetailResponse;
import com.auta.server.api.service.project.response.ProjectSummariesResponse;
import com.auta.server.api.service.project.response.ProjectTestDetailResponse;
import com.auta.server.api.service.project.response.ProjectTestSummariesResponse;
import com.auta.server.application.service.ProjectQueryService;
import com.auta.server.docs.RestDocsSupport;
import com.auta.server.domain.project.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class ProjectQueryControllerDocsTest extends RestDocsSupport {

    ProjectQueryService projectQueryService = mock(ProjectQueryService.class);

    @Override
    protected Object initController() {
        return new ProjectQueryController(projectQueryService);
    }

    @DisplayName("프로젝트 리스트 조회")
    @Test
    void getProjectSummaryList() throws Exception {
        //given
        given(projectQueryService.getProjectSummaryList(anyString(), anyString(), anyInt()))
                .willReturn(ProjectSummariesResponse.builder()
                        .projectSummaries(List.of(
                                ProjectSummariesResponse.ProjectSummary.builder()
                                        .projectId(1L)
                                        .projectAdmin("adminUser")
                                        .projectName("UI 테스트 프로젝트")
                                        .projectEnd(LocalDate.of(2024, 12, 31))
                                        .projectCreatedDate(LocalDate.of(2024, 1, 1))
                                        .projectStatus(ProjectStatus.IN_PROGRESS)
                                        .testRate(90)
                                        .build(),
                                ProjectSummariesResponse.ProjectSummary.builder()
                                        .projectId(2L)
                                        .projectAdmin("otherAdmin")
                                        .projectName("API 서버 개발")
                                        .projectEnd(LocalDate.of(2024, 11, 30))
                                        .projectCreatedDate(LocalDate.of(2024, 2, 15))
                                        .projectStatus(ProjectStatus.COMPLETED)
                                        .testRate(100)
                                        .build()
                        ))
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects", 1)
                                .param("projectName", "UI test")
                                .param("sortBy", "rate")
                                .param("cursor", "1")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-summaries-read",
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
                                        .description("프로젝트 요약 응답 데이터"),

                                fieldWithPath("data.projectSummaries").type(JsonFieldType.ARRAY)
                                        .description("프로젝트 요약 리스트"),

                                fieldWithPath("data.projectSummaries[].projectId").type(JsonFieldType.NUMBER)
                                        .description("프로젝트 ID"),
                                fieldWithPath("data.projectSummaries[].projectAdmin").type(JsonFieldType.STRING)
                                        .description("프로젝트 관리자 (username)"),
                                fieldWithPath("data.projectSummaries[].projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.projectSummaries[].projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectSummaries[].projectCreatedDate").type(JsonFieldType.STRING)
                                        .description("프로젝트 생성일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectSummaries[].projectStatus").type(JsonFieldType.STRING)
                                        .description("프로젝트 상태 (예: 진행중, 완료)"),
                                fieldWithPath("data.projectSummaries[].testRate").type(JsonFieldType.NUMBER)
                                        .description("테스트 통과율 (%)")
                        )));
    }

    @DisplayName("프로젝트 세부 조회")
    @Test
    void getProjectDetail() throws Exception {
        //given
        given(projectQueryService.getProjectDetail(anyLong()))
                .willReturn(ProjectDetailResponse.builder()
                        .projectName("UI 자동화 테스트")
                        .projectAdmin("adminUser")
                        .projectCreatedDate(LocalDate.of(2024, 1, 1))
                        .projectEnd(LocalDate.of(2024, 12, 31))
                        .testExecutionDate(LocalDate.of(2024, 4, 25))
                        .figmaRootPage("HomePage")
                        .description("UI 자동화 프로젝트입니다.")
                        .figmaUrl("https://figma.com/example")
                        .serviceUrl("https://service.com")
                        .reportSummary("전체 테스트 성공률 95%")
                        .testSummary(ProjectDetailResponse.TestSummary.builder()
                                .totalRoutingTest(10)
                                .totalInteractionTest(15)
                                .totalMappingTest(20)
                                .build())
                        .pages(List.of(
                                ProjectDetailResponse.PageInfo.builder()
                                        .pageName("메인 페이지")
                                        .pageUrl("/main")
                                        .pageImageUrl("https://figma.com/image/login.png")
                                        .build(),
                                ProjectDetailResponse.PageInfo.builder()
                                        .pageName("로그인 페이지")
                                        .pageUrl("/login")
                                        .pageImageUrl("https://figma.com/image/login.png")
                                        .build()
                        ))
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로젝트 상세 응답 데이터"),

                                fieldWithPath("data.projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("data.projectAdmin").type(JsonFieldType.STRING)
                                        .description("프로젝트 관리자 (username)"),
                                fieldWithPath("data.projectCreatedDate").type(JsonFieldType.STRING)
                                        .description("프로젝트 생성일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("data.testExecutionDate").type(JsonFieldType.STRING)
                                        .description("테스트 실행일 (yyyy-MM-dd)"),
                                fieldWithPath("data.figmaRootPage").type(JsonFieldType.STRING)
                                        .description("피그마 루트 페이지 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                                fieldWithPath("data.figmaUrl").type(JsonFieldType.STRING).description("피그마 URL"),
                                fieldWithPath("data.serviceUrl").type(JsonFieldType.STRING).description("서비스 URL"),
                                fieldWithPath("data.reportSummary").type(JsonFieldType.STRING).description("리포트 요약"),

                                fieldWithPath("data.testSummary.totalRoutingTest").type(JsonFieldType.NUMBER)
                                        .description("라우팅 테스트 총 개수"),
                                fieldWithPath("data.testSummary.totalInteractionTest").type(JsonFieldType.NUMBER)
                                        .description("인터랙션 테스트 총 개수"),
                                fieldWithPath("data.testSummary.totalMappingTest").type(JsonFieldType.NUMBER)
                                        .description("매핑 테스트 총 개수"),

                                fieldWithPath("data.pages").type(JsonFieldType.ARRAY).description("페이지 목록"),
                                fieldWithPath("data.pages[].pageName").type(JsonFieldType.STRING).description("페이지 이름"),
                                fieldWithPath("data.pages[].pageUrl").type(JsonFieldType.STRING).description("페이지 URL"),
                                fieldWithPath("data.pages[].pageImageUrl").type(JsonFieldType.STRING)
                                        .description("페이지 피그마 이미지 URL")
                        )
                ));
    }

    @DisplayName("프로젝트 테스트 리스트 조회")
    @Test
    void getProjectTestSummaryList() throws Exception {
        //given
        given(projectQueryService.getProjectTestSummaryList(anyString(), anyString(), anyInt()))
                .willReturn(ProjectTestSummariesResponse.builder()
                        .tests(List.of(
                                ProjectTestSummariesResponse.ProjectTestSummary.builder()
                                        .projectId(1L)
                                        .projectName("UI 자동화 테스트")
                                        .totalRoutingTest(10)
                                        .successRoutingTest(8)
                                        .totalInteractionTest(15)
                                        .successInteractionTest(14)
                                        .totalMappingTest(20)
                                        .successMappingTest(18)
                                        .projectCreatedDate(LocalDate.of(2024, 4, 1))
                                        .build(),
                                ProjectTestSummariesResponse.ProjectTestSummary.builder()
                                        .projectId(2L)
                                        .projectName("API 서버 개발")
                                        .totalRoutingTest(12)
                                        .successRoutingTest(11)
                                        .totalInteractionTest(10)
                                        .successInteractionTest(9)
                                        .totalMappingTest(15)
                                        .successMappingTest(13)
                                        .projectCreatedDate(LocalDate.of(2024, 2, 15))
                                        .build()
                        ))
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/tests")
                                .param("projectName", "UI 테스트 자동화 프로젝트")
                                .param("sortBy", "rate")
                                .param("cursor", "1")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-test-summaries-read",
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
                                        .description("프로젝트 테스트 요약 응답 데이터"),

                                fieldWithPath("data.tests").type(JsonFieldType.ARRAY)
                                        .description("프로젝트 테스트 요약 리스트"),
                                fieldWithPath("data.tests[].projectId").type(JsonFieldType.NUMBER)
                                        .description("프로젝트 ID"),
                                fieldWithPath("data.tests[].projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.tests[].totalRoutingTest").type(JsonFieldType.NUMBER)
                                        .description("라우팅 테스트 전체 수"),
                                fieldWithPath("data.tests[].successRoutingTest").type(JsonFieldType.NUMBER)
                                        .description("라우팅 테스트 성공 수"),
                                fieldWithPath("data.tests[].totalInteractionTest").type(JsonFieldType.NUMBER)
                                        .description("인터랙션 테스트 전체 수"),
                                fieldWithPath("data.tests[].successInteractionTest").type(JsonFieldType.NUMBER)
                                        .description("인터랙션 테스트 성공 수"),
                                fieldWithPath("data.tests[].totalMappingTest").type(JsonFieldType.NUMBER)
                                        .description("매핑 테스트 전체 수"),
                                fieldWithPath("data.tests[].successMappingTest").type(JsonFieldType.NUMBER)
                                        .description("매핑 테스트 성공 수"),
                                fieldWithPath("data.tests[].projectCreatedDate").type(JsonFieldType.STRING)
                                        .description("프로젝트 시작일 (yyyy-MM-dd)")
                        )));
    }

    @DisplayName("프로젝트 테스트 세부 조회")
    @Test
    void getProjectTestDetail() throws Exception {
        //given
        given(projectQueryService.getProjectTestDetail(anyLong()))
                .willReturn(ProjectTestDetailResponse.builder()
                        .projectName("UI 테스트 프로젝트")
                        .projectAdmin("example_admin")
                        .projectStart(LocalDate.of(2024, 1, 1))
                        .projectEnd(LocalDate.of(2024, 12, 31))
                        .testExecutionDate(LocalDate.of(2024, 4, 4))
                        .description("테스트 프로젝트에 대한 설명입니다.")
                        .testSummary(ProjectTestDetailResponse.TestCountSummary.builder()
                                .totalSuccessTests(24)
                                .totalFailTests(6)
                                .interactionSuccessCount(10)
                                .interactionFailCount(2)
                                .mappingSuccessCount(8)
                                .mappingFailCount(2)
                                .routingSuccessCount(6)
                                .routingFailCount(2)
                                .build())
                        .pages(List.of(
                                ProjectTestDetailResponse.PageInfo.builder()
                                        .pageId(1L)
                                        .pageName("메인 페이지")
                                        .build(),
                                ProjectTestDetailResponse.PageInfo.builder()
                                        .pageId(2L)
                                        .pageName("로그인 페이지")
                                        .build()
                        ))
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/tests/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-test-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로젝트 테스트 관리 세부 응답 데이터"),

                                fieldWithPath("data.projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("data.projectAdmin").type(JsonFieldType.STRING).description("프로젝트 관리자"),
                                fieldWithPath("data.projectStart").type(JsonFieldType.STRING)
                                        .description("프로젝트 시작일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료일 (yyyy-MM-dd)"),
                                fieldWithPath("data.testExecutionDate").type(JsonFieldType.STRING)
                                        .description("테스트 수행일 (yyyy-MM-dd)"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("프로젝트 설명"),

                                fieldWithPath("data.testSummary").type(JsonFieldType.OBJECT)
                                        .description("테스트 결과 요약 정보"),
                                fieldWithPath("data.testSummary.totalSuccessTests").type(JsonFieldType.NUMBER)
                                        .description("전체 성공 테스트 수"),
                                fieldWithPath("data.testSummary.totalFailTests").type(JsonFieldType.NUMBER)
                                        .description("전체 실패 테스트 수"),
                                fieldWithPath("data.testSummary.interactionSuccessCount").type(JsonFieldType.NUMBER)
                                        .description("인터랙션 성공 테스트 수"),
                                fieldWithPath("data.testSummary.interactionFailCount").type(JsonFieldType.NUMBER)
                                        .description("인터랙션 실패 테스트 수"),
                                fieldWithPath("data.testSummary.mappingSuccessCount").type(JsonFieldType.NUMBER)
                                        .description("매핑 성공 테스트 수"),
                                fieldWithPath("data.testSummary.mappingFailCount").type(JsonFieldType.NUMBER)
                                        .description("매핑 실패 테스트 수"),
                                fieldWithPath("data.testSummary.routingSuccessCount").type(JsonFieldType.NUMBER)
                                        .description("라우팅 성공 테스트 수"),
                                fieldWithPath("data.testSummary.routingFailCount").type(JsonFieldType.NUMBER)
                                        .description("라우팅 실패 테스트 수"),

                                fieldWithPath("data.pages").type(JsonFieldType.ARRAY).description("페이지 목록"),
                                fieldWithPath("data.pages[].pageId").type(JsonFieldType.NUMBER).description("페이지 ID"),
                                fieldWithPath("data.pages[].pageName").type(JsonFieldType.STRING).description("페이지 이름")
                        )));

    }

    @DisplayName("해당 페이지 테스트 조회")
    @Test
    void getPageTestDetails() throws Exception {
        //given
        given(projectQueryService.getPageTestDetail(anyLong()))
                .willReturn(PageTestResponse.builder()
                        .routingTest(PageTestResponse.RoutingTest.builder()
                                .success(List.of(
                                        PageTestResponse.RoutingSuccess.builder()
                                                .triggerSelector(".login-button")
                                                .expectedDestination("LoginPage")
                                                .actualDestination("/login")
                                                .build()
                                ))
                                .fail(List.of(
                                        PageTestResponse.RoutingFail.builder()
                                                .triggerSelector(".forgot-password")
                                                .expectedDestination("ForgotPasswordPage")
                                                .actualDestination("/error")
                                                .failReason("Wrong page routed")
                                                .build()
                                ))
                                .build())
                        .mappingTest(PageTestResponse.MappingTest.builder()
                                .matchedComponents(2)
                                .componentNames(List.of(
                                        PageTestResponse.ComponentName.builder()
                                                .componentName("로그인 버튼")
                                                .build(),
                                        PageTestResponse.ComponentName.builder()
                                                .componentName("회원가입 버튼")
                                                .build()
                                ))
                                .failComponents(List.of(
                                        PageTestResponse.FailComponent.builder()
                                                .componentName("비밀번호 찾기 버튼")
                                                .failReason("요소 누락")
                                                .build()
                                ))
                                .build())
                        .interactionTest(PageTestResponse.InteractionTest.builder()
                                .success(List.of(
                                        PageTestResponse.InteractionSuccess.builder()
                                                .trigger("hover")
                                                .action("show tooltip")
                                                .build()
                                ))
                                .fail(List.of(
                                        PageTestResponse.InteractionFail.builder()
                                                .trigger("click")
                                                .expectedAction("open modal")
                                                .actualAction("no action")
                                                .failReason("click 이벤트 미작동")
                                                .build()
                                ))
                                .build())
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/pages/{pageId}", 1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("page-test",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("페이지 테스트 상세 응답"),

                                fieldWithPath("data.routingTest").type(JsonFieldType.OBJECT).description("라우팅 테스트 결과"),
                                fieldWithPath("data.routingTest.success").type(JsonFieldType.ARRAY)
                                        .description("라우팅 성공 케이스 목록"),
                                fieldWithPath("data.routingTest.success[].triggerSelector").type(JsonFieldType.STRING)
                                        .description("트리거 셀렉터"),
                                fieldWithPath("data.routingTest.success[].expectedDestination").type(
                                        JsonFieldType.STRING).description("예상 이동 페이지"),
                                fieldWithPath("data.routingTest.success[].actualDestination").type(JsonFieldType.STRING)
                                        .description("실제 이동 페이지"),

                                fieldWithPath("data.routingTest.fail").type(JsonFieldType.ARRAY)
                                        .description("라우팅 실패 케이스 목록"),
                                fieldWithPath("data.routingTest.fail[].triggerSelector").type(JsonFieldType.STRING)
                                        .description("트리거 셀렉터"),
                                fieldWithPath("data.routingTest.fail[].expectedDestination").type(JsonFieldType.STRING)
                                        .description("예상 이동 페이지"),
                                fieldWithPath("data.routingTest.fail[].actualDestination").type(JsonFieldType.STRING)
                                        .description("실제 이동 페이지"),
                                fieldWithPath("data.routingTest.fail[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유"),

                                fieldWithPath("data.mappingTest").type(JsonFieldType.OBJECT).description("매핑 테스트 결과"),
                                fieldWithPath("data.mappingTest.matchedComponents").type(JsonFieldType.NUMBER)
                                        .description("매칭된 컴포넌트 개수"),
                                fieldWithPath("data.mappingTest.componentNames").type(JsonFieldType.ARRAY)
                                        .description("매칭된 컴포넌트 이름 목록"),
                                fieldWithPath("data.mappingTest.componentNames[].componentName").type(
                                        JsonFieldType.STRING).description("컴포넌트 이름"),

                                fieldWithPath("data.mappingTest.failComponents").type(JsonFieldType.ARRAY)
                                        .description("매핑 실패 컴포넌트 목록"),
                                fieldWithPath("data.mappingTest.failComponents[].componentName").type(
                                        JsonFieldType.STRING).description("컴포넌트 이름"),
                                fieldWithPath("data.mappingTest.failComponents[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유"),

                                fieldWithPath("data.interactionTest").type(JsonFieldType.OBJECT)
                                        .description("인터랙션 테스트 결과"),
                                fieldWithPath("data.interactionTest.success").type(JsonFieldType.ARRAY)
                                        .description("인터랙션 성공 목록"),
                                fieldWithPath("data.interactionTest.success[].trigger").type(JsonFieldType.STRING)
                                        .description("트리거 타입"),
                                fieldWithPath("data.interactionTest.success[].action").type(JsonFieldType.STRING)
                                        .description("동작"),

                                fieldWithPath("data.interactionTest.fail").type(JsonFieldType.ARRAY)
                                        .description("인터랙션 실패 목록"),
                                fieldWithPath("data.interactionTest.fail[].trigger").type(JsonFieldType.STRING)
                                        .description("트리거 타입"),
                                fieldWithPath("data.interactionTest.fail[].expectedAction").type(JsonFieldType.STRING)
                                        .description("예상 동작"),
                                fieldWithPath("data.interactionTest.fail[].actualAction").type(JsonFieldType.STRING)
                                        .description("실제 동작"),
                                fieldWithPath("data.interactionTest.fail[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유")
                        )));
    }
}
