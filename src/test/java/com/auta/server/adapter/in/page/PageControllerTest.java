package com.auta.server.adapter.in.page;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageControllerTest extends ControllerTestSupport {

    @DisplayName("해당 페이지 아이디에 해당하는 테스트 결과를 반환한다.")
    @Test
    void getPageTestDetails() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/pages/{pageId}", 1)
                ).andDo(print())
                .andExpect(status().isOk());
    }
}