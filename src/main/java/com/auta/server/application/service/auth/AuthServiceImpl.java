package com.auta.server.application.service.auth;

import com.auta.server.application.port.in.auth.AuthCommand;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.application.port.out.auth.RefreshTokenStorePort;
import com.auta.server.application.port.out.auth.TokenClaimData;
import com.auta.server.application.port.out.auth.TokenProviderPort;
import com.auta.server.application.port.out.persistence.user.UserPort;
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
        User user = findUserByEmail(email);

        validatePassword(command, user);

        TokenClaimData tokenClaimData = TokenClaimData.fromEmail(email);
        Date createdTime = new Date();

        String accessToken = generateAccessToken(tokenClaimData, createdTime);
        String refreshToken = generateRefreshToken(tokenClaimData, createdTime);

        storeRefreshToken(email, refreshToken);

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public void logout(String email) {
        refreshTokenStorePort.delete(buildRefreshTokenKey(email));
    }

    @Override
    public AuthTokens reIssue(String email) {
        refreshTokenStorePort.delete(buildRefreshTokenKey(email));

        TokenClaimData tokenClaimData = TokenClaimData.fromEmail(email);
        Date createdTime = new Date();

        String accessToken = generateAccessToken(tokenClaimData, createdTime);
        String refreshToken = generateRefreshToken(tokenClaimData, createdTime);

        storeRefreshToken(email, refreshToken);

        return new AuthTokens(accessToken, refreshToken);

    }

    private User findUserByEmail(String email) {
        Optional<User> optionalUser = userPort.findByEmail(email);

        return optionalUser
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private void validatePassword(AuthCommand command, User user) {
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private String generateAccessToken(TokenClaimData tokenClaimData, Date createdTime) {
        return tokenProviderPort.generate(tokenClaimData, TokenType.ACCESS, createdTime);
    }

    private String generateRefreshToken(TokenClaimData tokenClaimData, Date createdTime) {
        return tokenProviderPort.generate(tokenClaimData, TokenType.REFRESH, createdTime);
    }

    private void storeRefreshToken(String email, String refreshToken) {
        refreshTokenStorePort.store(buildRefreshTokenKey(email), refreshToken, TokenType.REFRESH.getExpirationMillis());
    }

    private String buildRefreshTokenKey(String email) {
        return "refresh_token:" + email;
    }
}
