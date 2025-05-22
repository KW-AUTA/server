package com.auta.server.adapter.in.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class ProjectControllerTest extends ControllerTestSupport {

    @DisplayName("프로젝트 테스트를 실행한다.")
    @Test
    void executeTest() throws Exception {
        //given
        //when //then
        mockMvc.perform(
                        post("/api/v1/projects/1/run-test")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("프로젝트를 생성한다.")
    @Test
    void createProject() throws Exception {
        //given
        setMockSecurityContext();
        ProjectRequest request = ProjectRequest.builder()
                .projectName("UI 자동화 테스트")
                .projectEnd(LocalDate.of(2024, 4, 4))
                .description("프로젝트 설명입니다.")
                .figmaUrl("https://figma.com")
                .serviceUrl("https://service.com")
                .rootFigmaPage("mainPage")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",               // @RequestPart 이름과 일치
                "request.json",          // 파일 이름
                "application/json",      // Content-Type
                json.getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile filePart = new MockMultipartFile(
                "file",                  // @RequestPart 이름과 일치
                "example.json",          // 파일 이름
                "application/json",
                "{ \"key\": \"value\" }".getBytes(StandardCharsets.UTF_8)
        );

        User user = User.builder()
                .id(1L)
                .email("example@example.com")
                .username("exampleUser")
                .address(null)
                .phoneNumber(null)
                .build();

        given(projectUseCase.createProject(any(ProjectCommand.class), any(), anyString(), any()))
                .willReturn(Project.builder()
                        .id(1L)
                        .projectName("UI 자동화 테스트")
                        .user(user)
                        .projectCreatedDate(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());

        //when //then
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/projects")
                                .file(jsonPart)
                                .file(filePart)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("프로젝트를 수정한다.")
    @Test
    void updateProject() throws Exception {
        //given
        ProjectRequest request = ProjectRequest.builder()
                .projectName("UI 자동화 테스트")
                .projectEnd(LocalDate.of(2024, 4, 4))
                .description("프로젝트 설명입니다.")
                .figmaUrl("https://figma.com")
                .serviceUrl("https://service.com")
                .rootFigmaPage("mainPage")
                .build();

        User user = User.builder()
                .id(1L)
                .email("example@example.com")
                .username("exampleUser")
                .address(null)
                .phoneNumber(null)
                .build();

        given(projectUseCase.updateProject(any(ProjectCommand.class), anyLong()))
                .willReturn(Project.builder()
                        .id(1L)
                        .projectName("UI 자동화 테스트")
                        .user(user)
                        .projectCreatedDate(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());
        //when //then
        mockMvc.perform(
                        put("/api/v1/projects/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("프로젝트를 삭제한다.")
    @Test
    void deleteProject() throws Exception {
        //given

        //when //then
        mockMvc.perform(
                        delete("/api/v1/projects/1")
                ).andDo(print())
                .andExpect(status().isOk());
    }
}