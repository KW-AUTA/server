package com.auta.server.api.controller.main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainPageControllerTest extends ControllerTestSupport {
    @DisplayName("대시보드 정보들을 조회한다.")
    @Test
    void getDashboardData() throws Exception {
        //given
        setMockSecurityContext();
        //when //then
        mockMvc.perform(
                        get("/api/v1/home")
                                .header("Authorization", "Bearer JwtToken")
                ).andDo(print())
                .andExpect(status().isOk());
    }
}