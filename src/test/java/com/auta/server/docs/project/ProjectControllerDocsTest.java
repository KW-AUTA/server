package com.auta.server.docs.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.adapter.in.project.ProjectController;
import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.api.service.project.request.ProjectServiceRequest;
import com.auta.server.api.service.project.response.ProjectResponse;
import com.auta.server.application.service.ProjectService;
import com.auta.server.docs.RestDocsSupport;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class ProjectControllerDocsTest extends RestDocsSupport {

    private final ProjectService projectService = mock(ProjectService.class);

    @Override
    protected Object initController() {
        return new ProjectController(projectService);
    }

    @DisplayName("프로젝트 생성")
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

        given(projectService.createProject(any(ProjectServiceRequest.class), anyString()))
                .willReturn(ProjectResponse.builder()
                        .projectId(1L)
                        .projectName("UI 자동화 테스트")
                        .administrator("오준혁")
                        .projectCreatedDate(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());
        //when //then
        mockMvc.perform(
                        post("/api/v1/projects")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("프로젝트 설명"),
                                fieldWithPath("figmaUrl").type(JsonFieldType.STRING)
                                        .description("피그마 URL"),
                                fieldWithPath("serviceUrl").type(JsonFieldType.STRING)
                                        .description("서비스 URL"),
                                fieldWithPath("rootFigmaPage").type(JsonFieldType.STRING)
                                        .description("피그마 루트 페이지 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),

                                fieldWithPath("data.projectId").type(JsonFieldType.NUMBER)
                                        .description("프로젝트 ID"),
                                fieldWithPath("data.projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.administrator").type(JsonFieldType.STRING)
                                        .description("프로젝트 관리자 (username)"),
                                fieldWithPath("data.projectCreatedDate").type(JsonFieldType.STRING)
                                        .description("프로젝트 생성일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                        .description("프로젝트 설명"),
                                fieldWithPath("data.figmaUrl").type(JsonFieldType.STRING)
                                        .description("피그마 URL"),
                                fieldWithPath("data.serviceUrl").type(JsonFieldType.STRING)
                                        .description("서비스 URL"),
                                fieldWithPath("data.rootFigmaPage").type(JsonFieldType.STRING)
                                        .description("피그마 루트 페이지 이름")
                        )));
    }

    @DisplayName("프로젝트 수정")
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

        given(projectService.updateProject(any(ProjectServiceRequest.class), anyLong()))
                .willReturn(ProjectResponse.builder()
                        .projectId(1L)
                        .projectName("UI 자동화 테스트")
                        .administrator("오준혁")
                        .projectCreatedDate(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());
        //when //then
        mockMvc.perform(
                        put("/api/v1/projects/{projectId}", 1L)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("projectId").description("조회할 프로젝트의 ID")
                        ),
                        requestFields(
                                fieldWithPath("projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("프로젝트 설명"),
                                fieldWithPath("figmaUrl").type(JsonFieldType.STRING)
                                        .description("피그마 URL"),
                                fieldWithPath("serviceUrl").type(JsonFieldType.STRING)
                                        .description("서비스 URL"),
                                fieldWithPath("rootFigmaPage").type(JsonFieldType.STRING)
                                        .description("피그마 루트 페이지 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),

                                fieldWithPath("data.projectId").type(JsonFieldType.NUMBER)
                                        .description("프로젝트 ID"),
                                fieldWithPath("data.projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("data.administrator").type(JsonFieldType.STRING)
                                        .description("프로젝트 관리자 (username)"),
                                fieldWithPath("data.projectCreatedDate").type(JsonFieldType.STRING)
                                        .description("프로젝트 생성일 (yyyy-MM-dd)"),
                                fieldWithPath("data.projectEnd").type(JsonFieldType.STRING)
                                        .description("프로젝트 종료 예정일 (yyyy-MM-dd)"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                        .description("프로젝트 설명"),
                                fieldWithPath("data.figmaUrl").type(JsonFieldType.STRING)
                                        .description("피그마 URL"),
                                fieldWithPath("data.serviceUrl").type(JsonFieldType.STRING)
                                        .description("서비스 URL"),
                                fieldWithPath("data.rootFigmaPage").type(JsonFieldType.STRING)
                                        .description("피그마 루트 페이지 이름")
                        )));
    }

    @DisplayName("프로젝트 삭제")
    @Test
    void deleteProject() throws Exception {
        //given

        doNothing().when(projectService).deleteProject(anyLong());
        //when //then
        mockMvc.perform(
                        delete("/api/v1/projects/{projectId}", 1L)

                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("projectId").description("조회할 프로젝트의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )));
    }

}
