package com.auta.server.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auta.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CurrentUserArgumentResolverTest {

    private CurrentUserArgumentResolver resolver;

    @Mock
    private MethodParameter parameter;

    @BeforeEach
    void setUp() {
        resolver = new CurrentUserArgumentResolver();
    }

    @DisplayName("@CurrentUser(CurrentUserType.EMAIL)이면 이메일을 반환한다.")
    @Test
    void returnEmail() {
        //given
        User mockUser = User.builder()
                .email("test@example.com")
                .username("테스트유저")
                .build();

        mockSecurityContextWithUser(mockUser);
        mockParameterAnnotation(CurrentUserType.EMAIL);

        //when
        Object result = resolver.resolveArgument(parameter, null, null, null);
        //then
        assertThat(result).isEqualTo("test@example.com");
    }

    @DisplayName("@CurrentUser(CurrentUserType.NAME)이면 이름을 반환한다.")
    @Test
    void returnName() {
        //given
        User mockUser = User.builder()
                .email("test@example.com")
                .username("테스트유저")
                .build();
        mockSecurityContextWithUser(mockUser);
        mockParameterAnnotation(CurrentUserType.NAME);

        //when
        Object result = resolver.resolveArgument(parameter, null, null, null);
        //then
        assertThat(result).isEqualTo("테스트유저");
    }

    @DisplayName("@CurrentUser(CurrentUserType.Entity)이면 User을 반환한다.")
    @Test
    void returnUser() {
        //given
        User mockUser = User.builder()
                .email("test@example.com")
                .username("테스트유저")
                .build();
        mockSecurityContextWithUser(mockUser);
        mockParameterAnnotation(CurrentUserType.ENTITY);

        //when
        Object result = resolver.resolveArgument(parameter, null, null, null);
        //then
        assertThat(result).isInstanceOf(User.class).extracting("email", "username")
                .contains("test@example.com", "테스트유저");
    }

    private void mockSecurityContextWithUser(User user) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);
    }

    private void mockParameterAnnotation(CurrentUserType type) {
        CurrentUser annotation = mock(CurrentUser.class);
        when(parameter.getParameterAnnotation(CurrentUser.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(type);
    }
}