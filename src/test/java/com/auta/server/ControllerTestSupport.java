package com.auta.server;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.auta.server.adapter.in.auth.AuthController;
import com.auta.server.adapter.in.dashboard.DashBoardController;
import com.auta.server.adapter.in.health.HealthCheckController;
import com.auta.server.adapter.in.project.ProjectController;
import com.auta.server.adapter.in.project.ProjectQueryController;
import com.auta.server.adapter.in.user.UserController;
import com.auta.server.adapter.out.web.CookieManager;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.application.port.in.user.UserUseCase;
import com.auta.server.application.service.DashBoardService;
import com.auta.server.application.service.ProjectQueryService;
import com.auta.server.application.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = {
        UserController.class,
        AuthController.class,
        HealthCheckController.class,
        DashBoardController.class,
        ProjectController.class,
        ProjectQueryController.class})
@Import(CookieManager.class)
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected UserUseCase userUseCase;

    @MockitoBean
    protected AuthUseCase authUseCase;

    @MockitoBean
    protected DashBoardService dashBoardService;

    @MockitoBean
    protected ProjectQueryService projectQueryService;

    @MockitoBean
    protected ProjectService projectService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    protected void setMockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}
