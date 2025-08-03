package com.auta.server.adapter.in.project;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectStatusControllerTest extends ControllerTestSupport {

    @DisplayName("ProjectStatus 상태를 확인하는 SSE 요청 api")
    @Test
    void streamStatus() throws Exception {
        //given

        //when

        //then

        mockMvc.perform(
                        get("/api/v1/projects/{projectId}/status/stream", 1)
                ).andDo(print())
                .andExpect(status().isOk());
    }
}