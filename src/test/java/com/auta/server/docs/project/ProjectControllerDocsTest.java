package com.auta.server.docs.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.adapter.in.project.ProjectController;
import com.auta.server.adapter.in.project.request.ProjectRequest;
import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.docs.RestDocsSupport;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;

public class ProjectControllerDocsTest extends RestDocsSupport {

    private final ProjectUseCase projectUseCase = mock(ProjectUseCase.class);

    @Override
    protected Object initController() {
        return new ProjectController(projectUseCase);
    }


    @DisplayName("프로젝트 테스트 실행")
    @Test
    void executeTest() throws Exception {
        //given
        //when //then
        mockMvc.perform(
                        post("/api/v1/projects/1/run-test")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-test",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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


    @DisplayName("프로젝트 생성")
    @Test
    void createProject() throws Exception {
        //given
        setMockSecurityContext();
        ProjectRequest request = ProjectRequest.builder()
                .projectName("UI 자동화 테스트")
                .expectedTestExecution(LocalDate.of(2024, 4, 4))
                .projectEnd(LocalDate.of(2024, 4, 4))
                .description("프로젝트 설명입니다.")
                .figmaUrl("https://figma.com")
                .serviceUrl("https://service.com")
                .rootFigmaPage("mainPage")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
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
                        .expectedTestExecution(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .figmaJson("https://s3.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());
        //when //then
        mockMvc.perform(
                        multipart("/api/v1/projects")
                                .file(jsonPart)
                                .file(filePart)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("request").description("프로젝트 정보 JSON"),
                                partWithName("file").description("피그마 JSON 파일")
                        ),
                        requestPartFields("request",
                                fieldWithPath("projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("expectedTestExecution").type(JsonFieldType.STRING)
                                        .description("예상 테스트 시작일"),
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
                                fieldWithPath("data.expectedTestExecution").type(JsonFieldType.STRING)
                                        .description("예상 테스트 시작일"),
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
                                fieldWithPath("data.figmaJson").type(JsonFieldType.STRING)
                                        .description("피그마 json 저장 url"),
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
                .expectedTestExecution(LocalDate.of(2024, 4, 3))
                .projectEnd(LocalDate.of(2024, 4, 4))
                .description("프로젝트 설명입니다.")
                .figmaUrl("https://figma.com")
                .serviceUrl("https://service.com")
                .rootFigmaPage("mainPage")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
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

        given(projectUseCase.updateProject(any(ProjectCommand.class), any(), anyLong()))
                .willReturn(Project.builder()
                        .id(1L)
                        .projectName("UI 자동화 테스트")
                        .user(user)
                        .projectCreatedDate(LocalDate.of(2024, 4, 3))
                        .expectedTestExecution(LocalDate.of(2024, 4, 3))
                        .projectEnd(LocalDate.of(2024, 4, 4))
                        .description("프로젝트 설명입니다.")
                        .figmaUrl("https://figma.com")
                        .figmaJson("https://s3.com")
                        .serviceUrl("https://service.com")
                        .rootFigmaPage("mainPage")
                        .build());
        //when //then
        mockMvc.perform(
                        multipart("/api/v1/projects/{projectId}", 1L)
                                .file(jsonPart)
                                .file(filePart)
                                .with(requests -> {
                                    requests.setMethod("PUT"); // PUT 메서드로 강제
                                    return requests;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("request").description("프로젝트 정보 JSON"),
                                partWithName("file").description("피그마 JSON 파일")
                        ),
                        requestPartFields("request",
                                fieldWithPath("projectName").type(JsonFieldType.STRING)
                                        .description("프로젝트 이름"),
                                fieldWithPath("expectedTestExecution").type(JsonFieldType.STRING)
                                        .description("예상 테스트 시작일"),
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
                                fieldWithPath("data.expectedTestExecution").type(JsonFieldType.STRING)
                                        .description("예상 테스트 시작일"),
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
                                fieldWithPath("data.figmaJson").type(JsonFieldType.STRING)
                                        .description("피그마 json 저장 url"),
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

        doNothing().when(projectUseCase).deleteProject(anyLong());
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
