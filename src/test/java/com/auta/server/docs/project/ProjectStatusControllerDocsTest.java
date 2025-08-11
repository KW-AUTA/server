package com.auta.server.docs.project;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.adapter.in.project.ProjectStatusController;
import com.auta.server.application.port.in.project.ProjectStatusUseCase;
import com.auta.server.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class ProjectStatusControllerDocsTest extends RestDocsSupport {
    ProjectStatusUseCase projectStatusUseCase = mock(ProjectStatusUseCase.class);

    @Override
    protected Object initController() {
        return new ProjectStatusController(projectStatusUseCase);
    }

    @DisplayName("ProjectStatus 상태를 확인하는 SSE 요청 api")
    @Test
    void streamStatus() throws Exception {
        //given
        setMockSecurityContext();
        SseEmitter dummyEmitter = new SseEmitter();
        given(projectStatusUseCase.stream(anyString())).willReturn(dummyEmitter);
        // when & then
        mockMvc.perform(
                        get("/api/v1/projects/status/stream")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("project-status-stream",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
