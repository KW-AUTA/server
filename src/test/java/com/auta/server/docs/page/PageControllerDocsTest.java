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

import com.auta.server.api.controller.page.PageController;
import com.auta.server.api.service.page.PageService;
import com.auta.server.api.service.page.response.PageResponse;
import com.auta.server.api.service.page.response.PageResponse.PageInfo;
import com.auta.server.docs.RestDocsSupport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class PageControllerDocsTest extends RestDocsSupport {

    PageService pageService = mock(PageService.class);

    @Override
    protected Object initController() {
        return new PageController(pageService);
    }

    @DisplayName("페이지 리스트 조회")
    @Test
    void getPages() throws Exception {
        //given
        given(pageService.getPagesBy(anyLong()))
                .willReturn(PageResponse.builder().projectName("Ui-test-project")
                        .testExecutionDate(LocalDate.of(2024, 4, 4))
                        .pages(List.of(
                                        PageInfo.builder()
                                                .pageId(1L)
                                                .pageName("메인페이지")
                                                .build()
                                )
                        )
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/pages/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("pages-read",
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
                                fieldWithPath("data.projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.testExecutionDate").type(JsonFieldType.STRING)
                                        .description("테스트 수행 일자 (yyyy-MM-dd)"),
                                fieldWithPath("data.pages").type(JsonFieldType.ARRAY)
                                        .description("페이지 정보 목록"),
                                fieldWithPath("data.pages[].pageId").type(JsonFieldType.NUMBER)
                                        .description("페이지 ID"),
                                fieldWithPath("data.pages[].pageName").type(JsonFieldType.STRING)
                                        .description("페이지 이름")

                        )));

    }
}
