package com.auta.server;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.auta.server.api.controller.auth.AuthController;
import com.auta.server.api.controller.health.HealthCheckController;
import com.auta.server.api.controller.main.MainPageController;
import com.auta.server.api.controller.page.PageController;
import com.auta.server.api.controller.user.UserController;
import com.auta.server.api.service.auth.AuthService;
import com.auta.server.api.service.main.MainPageService;
import com.auta.server.api.service.page.PageService;
import com.auta.server.api.service.user.UserService;
import com.auta.server.common.token.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
        MainPageController.class,
        PageController.class})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected AuthService authService;

    @MockitoBean
    protected MainPageService mainPageService;

    @MockitoBean
    protected PageService pageService;

    @MockitoBean
    protected TokenGenerator tokenGenerator;

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
