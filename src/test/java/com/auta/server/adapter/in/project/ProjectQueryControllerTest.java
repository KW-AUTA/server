package com.auta.server.adapter.in.project;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import com.auta.server.application.port.in.project.dto.ProjectDetailDto;
import com.auta.server.application.port.in.project.dto.ProjectTestDetailDto;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
        given(projectQueryUseCase.getProjectDetail(anyLong()))
                .willReturn(ProjectDetailDto.builder()
                        .project(Project.builder().user(User.builder().id(1L).username("adminUser").build())
                                .projectCreatedDate(LocalDate.of(2024, 1, 1))
                                .projectEnd(LocalDate.of(2024, 12, 31))
                                .testExecuteTime(LocalDateTime.of(2024, 4, 25, 12, 11))
                                .rootFigmaPage("HomePage")
                                .description("UI 자동화 프로젝트입니다.")
                                .figmaUrl("https://figma.com/example")
                                .serviceUrl("https://service.com")
                                .build())
                        .pages(List.of
                                (
                                        ProjectDetailDto.PageInfo
                                                .builder().pageName("메인 페이지").pageBaseUrl("/main")
                                                .build(),
                                        ProjectDetailDto.PageInfo
                                                .builder().pageName("로그인 페이지").pageBaseUrl("/login")
                                                .build()
                                ))
                        .totalRoutingTest(3)
                        .totalInteractionTest(1)
                        .totalMappingTest(2)
                        .build());
        //when  //then
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

    @DisplayName("해당 프로젝트 아이디에 해당하는 프로젝트 세부사항을 반환한다.")
    @Test
    void getProjectTestDetail() throws Exception {
        //given
        given(projectQueryUseCase.getProjectTestDetail(anyLong()))
                .willReturn(ProjectTestDetailDto.builder()
                        .project(Project.builder().projectName("AUTA")
                                .user(User.builder().id(1L).username("adminUser").build())
                                .projectCreatedDate(LocalDate.of(2024, 1, 1))
                                .projectEnd(LocalDate.of(2024, 12, 31))
                                .testExecuteTime(LocalDateTime.of(2024, 4, 25, 12, 11))
                                .rootFigmaPage("HomePage")
                                .description("UI 자동화 프로젝트입니다.")
                                .figmaUrl("https://figma.com/example")
                                .serviceUrl("https://service.com")
                                .build())
                        .pages(List.of
                                (
                                        ProjectTestDetailDto.PageInfo
                                                .builder().pageName("메인 페이지").pageId(1L)
                                                .build(),
                                        ProjectTestDetailDto.PageInfo
                                                .builder().pageName("로그인 페이지").pageId(2L)
                                                .build()
                                ))
                        .totalSuccessTests(5)
                        .totalFailTests(1)
                        .routingSuccessCount(2)
                        .routingFailCount(0)
                        .interactionSuccessCount(2)
                        .interactionFailCount(0)
                        .mappingSuccessCount(1)
                        .mappingFailCount(1)
                        .build());
        //when   //then
        mockMvc.perform(
                        get("/api/v1/projects/tests/1")
                ).andDo(print())
                .andExpect(status().isOk());

    }
}