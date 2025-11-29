package com.server.contestControl.authServer.service.logout;

import com.server.contestControl.authServer.dto.refresh.TokenValidationResult;
import com.server.contestControl.authServer.entity.RefreshToken;
import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.service.refreshToken.RefreshTokenRepoService;
import com.server.contestControl.authServer.service.refreshToken.TokenOwnershipValidator;
import com.server.contestControl.authServer.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService {

    private final TokenOwnershipValidator tokenOwnershipValidator;
    private final RefreshTokenRepoService refreshTokenRepoService;

    public void logout(HttpServletRequest request) {
        String oldToken = TokenExtractor.extractFromCookie(request);
        TokenValidationResult result = tokenOwnershipValidator.validateRefreshToken(oldToken);

        User user = result.user();
        RefreshToken stored = result.refreshToken();

        if (!stored.isRevoked()) {
            stored.setRevoked(true);
            refreshTokenRepoService.save(stored);
            log.info("Revoked refresh token {} for user {}", stored.getId(), user.getEmail());
        } else {
            log.warn("Logout called with already revoked token {}", stored.getId());
        }
    }
}