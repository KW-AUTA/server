package com.auta.server.docs.page;

import static org.mockito.ArgumentMatchers.anyLong;
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

import com.auta.server.adapter.in.page.PageController;
import com.auta.server.application.service.page.PageServiceImpl;
import com.auta.server.docs.RestDocsSupport;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class PageControllerDocsTest extends RestDocsSupport {
    PageServiceImpl pageService = mock(PageServiceImpl.class);

    @Override
    protected Object initController() {
        return new PageController(pageService);
    }

    @DisplayName("해당 페이지 테스트 조회")
    @Test
    void getPageTestDetails() throws Exception {
        //given
        given(pageService.getPageTestDetail(anyLong()))
                .willReturn(List.of(
                        // destination test - passed
                        com.auta.server.domain.test.Test.builder()
                                .id(1L)
                                .testType(TestType.ROUTING)
                                .testStatus(TestStatus.PASSED)
                                .triggerSelector("#submit-button")
                                .expectedDestination("/home")
                                .actualDestination("/login")
                                .build(),

                        // action test - passed
                        com.auta.server.domain.test.Test.builder()
                                .id(2L)
                                .testType(TestType.INTERACTION)
                                .testStatus(TestStatus.PASSED)
                                .trigger("click")
                                .expectedAction("open-modal")
                                .actualAction("reload")
                                .build(),

                        // mapping test - failed
                        com.auta.server.domain.test.Test.builder()
                                .id(3L)
                                .testType(TestType.MAPPING)
                                .testStatus(TestStatus.FAILED)
                                .componentName("LoginForm")
                                .failReason("존재하지 않음")
                                .build(),

                        // destination test - failed
                        com.auta.server.domain.test.Test.builder()
                                .id(4L)
                                .testType(TestType.ROUTING)
                                .testStatus(TestStatus.FAILED)
                                .triggerSelector(".nav-link")
                                .expectedDestination("/dashboard")
                                .actualDestination("/error")
                                .failReason("라우팅 누락")
                                .build(),

                        // destination test - pending
                        com.auta.server.domain.test.Test.builder()
                                .id(5L)
                                .testType(TestType.ROUTING)
                                .testStatus(TestStatus.PASSED)
                                .triggerSelector(".menu-item")
                                .expectedDestination("/settings")
                                .actualDestination("/settings")
                                .build(),

                        // action test - failed
                        com.auta.server.domain.test.Test.builder()
                                .id(6L)
                                .testType(TestType.INTERACTION)
                                .testStatus(TestStatus.FAILED)
                                .trigger("hover")
                                .expectedAction("show-tooltip")
                                .actualAction("nothing")
                                .failReason("이벤트 리스너 미작동")
                                .build(),

                        // mapping test - passed
                        com.auta.server.domain.test.Test.builder()
                                .id(7L)
                                .testType(TestType.MAPPING)
                                .testStatus(TestStatus.PASSED)
                                .componentName("SignupForm")
                                .build(),

                        // mapping test - pending
                        com.auta.server.domain.test.Test.builder()
                                .id(8L)
                                .testType(TestType.MAPPING)
                                .testStatus(TestStatus.PASSED)
                                .componentName("HeaderNav")
                                .build()
                ));
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
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("페이지 테스트 결과"),

                                // 1. 라우팅 테스트
                                fieldWithPath("data.routingTest").type(JsonFieldType.OBJECT).description("라우팅 테스트 결과"),
                                fieldWithPath("data.routingTest.success").type(JsonFieldType.ARRAY)
                                        .description("성공한 라우팅 테스트 목록"),
                                fieldWithPath("data.routingTest.success[].triggerSelector").type(JsonFieldType.STRING)
                                        .description("트리거 셀렉터"),
                                fieldWithPath("data.routingTest.success[].expectedDestination").type(
                                        JsonFieldType.STRING).description("예상 이동 경로"),
                                fieldWithPath("data.routingTest.success[].actualDestination").type(JsonFieldType.STRING)
                                        .description("실제 이동 경로"),

                                fieldWithPath("data.routingTest.fail").type(JsonFieldType.ARRAY)
                                        .description("실패한 라우팅 테스트 목록"),
                                fieldWithPath("data.routingTest.fail[].triggerSelector").type(JsonFieldType.STRING)
                                        .description("트리거 셀렉터"),
                                fieldWithPath("data.routingTest.fail[].expectedDestination").type(JsonFieldType.STRING)
                                        .description("예상 이동 경로"),
                                fieldWithPath("data.routingTest.fail[].actualDestination").type(JsonFieldType.STRING)
                                        .description("실제 이동 경로"),
                                fieldWithPath("data.routingTest.fail[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유"),

                                // 2. 매핑 테스트
                                fieldWithPath("data.mappingTest").type(JsonFieldType.OBJECT)
                                        .description("컴포넌트 매핑 테스트 결과"),
                                fieldWithPath("data.mappingTest.matchedComponents").type(JsonFieldType.NUMBER)
                                        .description("정상 매핑된 컴포넌트 수"),
                                fieldWithPath("data.mappingTest.componentNames").type(JsonFieldType.ARRAY)
                                        .description("정상 매핑된 컴포넌트 이름 목록"),
                                fieldWithPath("data.mappingTest.componentNames[].componentName").type(
                                        JsonFieldType.STRING).description("컴포넌트 이름"),

                                fieldWithPath("data.mappingTest.failComponents").type(JsonFieldType.ARRAY)
                                        .description("매핑 실패 컴포넌트 목록"),
                                fieldWithPath("data.mappingTest.failComponents[].componentName").type(
                                        JsonFieldType.STRING).description("컴포넌트 이름"),
                                fieldWithPath("data.mappingTest.failComponents[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유"),

                                // 3. 인터랙션 테스트
                                fieldWithPath("data.interactionTest").type(JsonFieldType.OBJECT)
                                        .description("인터랙션 테스트 결과"),
                                fieldWithPath("data.interactionTest.success").type(JsonFieldType.ARRAY)
                                        .description("성공한 인터랙션 테스트 목록"),
                                fieldWithPath("data.interactionTest.success[].trigger").type(JsonFieldType.STRING)
                                        .description("트리거 동작"),
                                fieldWithPath("data.interactionTest.success[].actualAction").type(JsonFieldType.STRING)
                                        .description("실제 실행된 액션"),

                                fieldWithPath("data.interactionTest.fail").type(JsonFieldType.ARRAY)
                                        .description("실패한 인터랙션 테스트 목록"),
                                fieldWithPath("data.interactionTest.fail[].trigger").type(JsonFieldType.STRING)
                                        .description("트리거 동작"),
                                fieldWithPath("data.interactionTest.fail[].expectedAction").type(JsonFieldType.STRING)
                                        .description("기대한 액션"),
                                fieldWithPath("data.interactionTest.fail[].actualAction").type(JsonFieldType.STRING)
                                        .description("실제 실행된 액션"),
                                fieldWithPath("data.interactionTest.fail[].failReason").type(JsonFieldType.STRING)
                                        .description("실패 사유")
                        )));
    }
}
