package com.auta.server.api.controller.health;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HealthCheckControllerTest extends ControllerTestSupport {

    @DisplayName("헬스 체크를 진행한다.")
    @Test
    void checkHealth() throws Exception {
        //when //then
        mockMvc.perform(
                        get("/api/v1/health")
                ).andDo(print())
                .andExpect(status().isOk());
    }
}