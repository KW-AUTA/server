package com.auta.server.adapter.in.project;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectQueryControllerTest extends ControllerTestSupport {

    @DisplayName("검색 조건에 따른 프로젝트 리스트를 조회한다.")
    @Test
    void getProjectSummaryList() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects")
                                .param("projectName", "UI 테스트 자동화 프로젝트")
                                .param("sortBy", "rate")
                                .param("cursor", "1")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("프로젝트 아이디에 따른 프로젝트 세부 정보를 조회한다.")
    @Test
    void getProjectDetail() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("검색 조건에 따른 프로젝트 테스트 리스트를 조회한다.")
    @Test
    void getProjectTestSummaryList() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/tests")
                                .param("projectName", "UI 테스트 자동화 프로젝트")
                                .param("sortBy", "rate")
                                .param("cursor", "1")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("해당 프로젝트 아이디에 해당하는 페이지 리스트를 반환한다.")
    @Test
    void getProjectTestDetail() throws Exception {
        //given

        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/tests/{projectId}", 1)
                ).andDo(print())
                .andExpect(status().isOk());

    }

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