package com.auta.server.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.auta.server.config.WebMvcConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@ExtendWith(RestDocumentationExtension.class)
@Import(WebMvcConfig.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .setCustomArgumentResolvers(
                        getArgumentResolvers().toArray(new HandlerMethodArgumentResolver[0])) // ✅ 요기
                .apply(documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();

    protected List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        return List.of();
    }
}
