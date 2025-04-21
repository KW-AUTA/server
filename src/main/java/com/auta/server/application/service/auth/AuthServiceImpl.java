package com.auta.server.application.service.auth;

import com.auta.server.application.port.in.auth.AuthCommand;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.application.port.out.auth.RefreshTokenStorePort;
import com.auta.server.application.port.out.auth.TokenClaimData;
import com.auta.server.application.port.out.auth.TokenProviderPort;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.auth.AuthTokens;
import com.auta.server.domain.auth.TokenType;
import com.auta.server.domain.user.User;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenStorePort refreshTokenStorePort;

    @Override
    public AuthTokens login(AuthCommand command) {
        String email = command.getEmail();
        Optional<User> optionalUser = userPort.findByEmail(email);

        User user = optionalUser
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        TokenClaimData tokenClaimData = TokenClaimData.fromEmail(email);
        Date createdTime = new Date();

        String accessToken = tokenProviderPort.generate(tokenClaimData, TokenType.ACCESS, createdTime);
        String refreshToken = tokenProviderPort.generate(tokenClaimData, TokenType.REFRESH, createdTime);

        refreshTokenStorePort.store("refresh_token:" + email, refreshToken, TokenType.REFRESH.getExpirationMillis());

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public void logout(String email) {
    }

    @Override
    public AuthTokens reIssue(String token) {
        return null;
    }
}
