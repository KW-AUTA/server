package com.auta.server.api.controller.page;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageControllerTest extends ControllerTestSupport {

    @DisplayName("해당 프로젝트 아이디에 해당하는 페이지 리스트를 반환한다.")
    @Test
    void getPages() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/pages/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk());

    }

}