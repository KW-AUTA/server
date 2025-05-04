//package com.auta.server.docs.page;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.auta.server.adapter.in.page.PageController;
//import com.auta.server.adapter.in.page.response.PageTestResponse;
//import com.auta.server.application.service.page.PageServiceImpl;
//import com.auta.server.docs.RestDocsSupport;
//import com.auta.server.domain.page.Page;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.restdocs.payload.JsonFieldType;
//
//public class PageControllerDocsTest extends RestDocsSupport {
//    PageServiceImpl pageService = mock(PageServiceImpl.class);
//
//    @Override
//    protected Object initController() {
//        return new PageController(pageService);
//    }
//
//    @DisplayName("해당 페이지 테스트 조회")
//    @Test
//    void getPageTestDetails() throws Exception {
//        //given
//        given(pageService.getPageTestDetail(anyLong()))
//                .willReturn(Page.builder()
//                        .routingTest(PageTestResponse.RoutingTest.builder()
//                                .success(List.of(
//                                        PageTestResponse.RoutingSuccess.builder()
//                                                .triggerSelector(".login-button")
//                                                .expectedDestination("LoginPage")
//                                                .actualDestination("/login")
//                                                .build()
//                                ))
//                                .fail(List.of(
//                                        PageTestResponse.RoutingFail.builder()
//                                                .triggerSelector(".forgot-password")
//                                                .expectedDestination("ForgotPasswordPage")
//                                                .actualDestination("/error")
//                                                .failReason("Wrong page routed")
//                                                .build()
//                                ))
//                                .build())
//                        .mappingTest(PageTestResponse.MappingTest.builder()
//                                .matchedComponents(2)
//                                .componentNames(List.of(
//                                        PageTestResponse.ComponentName.builder()
//                                                .componentName("로그인 버튼")
//                                                .build(),
//                                        PageTestResponse.ComponentName.builder()
//                                                .componentName("회원가입 버튼")
//                                                .build()
//                                ))
//                                .failComponents(List.of(
//                                        PageTestResponse.FailComponent.builder()
//                                                .componentName("비밀번호 찾기 버튼")
//                                                .failReason("요소 누락")
//                                                .build()
//                                ))
//                                .build())
//                        .interactionTest(PageTestResponse.InteractionTest.builder()
//                                .success(List.of(
//                                        PageTestResponse.InteractionSuccess.builder()
//                                                .trigger("hover")
//                                                .action("show tooltip")
//                                                .build()
//                                ))
//                                .fail(List.of(
//                                        PageTestResponse.InteractionFail.builder()
//                                                .trigger("click")
//                                                .expectedAction("open modal")
//                                                .actualAction("no action")
//                                                .failReason("click 이벤트 미작동")
//                                                .build()
//                                ))
//                                .build())
//                        .build());
//        //when   //then
//        mockMvc.perform(
//                        get("/api/v1/pages/{pageId}", 1)
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("page-test",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
//                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("페이지 테스트 상세 응답"),
//
//                                fieldWithPath("data.routingTest").type(JsonFieldType.OBJECT).description("라우팅 테스트 결과"),
//                                fieldWithPath("data.routingTest.success").type(JsonFieldType.ARRAY)
//                                        .description("라우팅 성공 케이스 목록"),
//                                fieldWithPath("data.routingTest.success[].triggerSelector").type(JsonFieldType.STRING)
//                                        .description("트리거 셀렉터"),
//                                fieldWithPath("data.routingTest.success[].expectedDestination").type(
//                                        JsonFieldType.STRING).description("예상 이동 페이지"),
//                                fieldWithPath("data.routingTest.success[].actualDestination").type(JsonFieldType.STRING)
//                                        .description("실제 이동 페이지"),
//
//                                fieldWithPath("data.routingTest.fail").type(JsonFieldType.ARRAY)
//                                        .description("라우팅 실패 케이스 목록"),
//                                fieldWithPath("data.routingTest.fail[].triggerSelector").type(JsonFieldType.STRING)
//                                        .description("트리거 셀렉터"),
//                                fieldWithPath("data.routingTest.fail[].expectedDestination").type(JsonFieldType.STRING)
//                                        .description("예상 이동 페이지"),
//                                fieldWithPath("data.routingTest.fail[].actualDestination").type(JsonFieldType.STRING)
//                                        .description("실제 이동 페이지"),
//                                fieldWithPath("data.routingTest.fail[].failReason").type(JsonFieldType.STRING)
//                                        .description("실패 사유"),
//
//                                fieldWithPath("data.mappingTest").type(JsonFieldType.OBJECT).description("매핑 테스트 결과"),
//                                fieldWithPath("data.mappingTest.matchedComponents").type(JsonFieldType.NUMBER)
//                                        .description("매칭된 컴포넌트 개수"),
//                                fieldWithPath("data.mappingTest.componentNames").type(JsonFieldType.ARRAY)
//                                        .description("매칭된 컴포넌트 이름 목록"),
//                                fieldWithPath("data.mappingTest.componentNames[].componentName").type(
//                                        JsonFieldType.STRING).description("컴포넌트 이름"),
//
//                                fieldWithPath("data.mappingTest.failComponents").type(JsonFieldType.ARRAY)
//                                        .description("매핑 실패 컴포넌트 목록"),
//                                fieldWithPath("data.mappingTest.failComponents[].componentName").type(
//                                        JsonFieldType.STRING).description("컴포넌트 이름"),
//                                fieldWithPath("data.mappingTest.failComponents[].failReason").type(JsonFieldType.STRING)
//                                        .description("실패 사유"),
//
//                                fieldWithPath("data.interactionTest").type(JsonFieldType.OBJECT)
//                                        .description("인터랙션 테스트 결과"),
//                                fieldWithPath("data.interactionTest.success").type(JsonFieldType.ARRAY)
//                                        .description("인터랙션 성공 목록"),
//                                fieldWithPath("data.interactionTest.success[].trigger").type(JsonFieldType.STRING)
//                                        .description("트리거 타입"),
//                                fieldWithPath("data.interactionTest.success[].action").type(JsonFieldType.STRING)
//                                        .description("동작"),
//
//                                fieldWithPath("data.interactionTest.fail").type(JsonFieldType.ARRAY)
//                                        .description("인터랙션 실패 목록"),
//                                fieldWithPath("data.interactionTest.fail[].trigger").type(JsonFieldType.STRING)
//                                        .description("트리거 타입"),
//                                fieldWithPath("data.interactionTest.fail[].expectedAction").type(JsonFieldType.STRING)
//                                        .description("예상 동작"),
//                                fieldWithPath("data.interactionTest.fail[].actualAction").type(JsonFieldType.STRING)
//                                        .description("실제 동작"),
//                                fieldWithPath("data.interactionTest.fail[].failReason").type(JsonFieldType.STRING)
//                                        .description("실패 사유")
//                        )));
//    }
//}
