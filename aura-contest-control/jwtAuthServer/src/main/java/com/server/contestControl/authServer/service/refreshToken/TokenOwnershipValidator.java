package com.server.contestControl.authServer.service.refreshToken;

import com.server.contestControl.authServer.dto.refresh.TokenValidationResult;
import com.server.contestControl.authServer.entity.RefreshToken;
import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.enums.TokenType;

import com.server.contestControl.authServer.exception.api.*;
import com.server.contestControl.authServer.repository.UserRepository;
import com.server.contestControl.authServer.service.jwt.core.JwtService;
import com.server.contestControl.authServer.util.TokenHashUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenOwnershipValidator {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepoService refreshTokenRepoService;

    public TokenValidationResult validateRefreshToken(String rawToken) {
        if (rawToken == null) throw new MissingTokenException();
        String hashedRawToken = TokenHashUtil.hash(rawToken);
        Claims claims = jwtService.extractAllClaims(rawToken, TokenType.REFRESH);
        String email = claims.getSubject();
        Long refreshTokenId = Long.parseLong(claims.getId());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        RefreshToken stored = refreshTokenRepoService.findById(refreshTokenId)
                .orElseThrow(RefreshTokenRevokedException::new);

        if (stored.isRevoked())
            throw new RefreshTokenRevokedException();

        if (!jwtService.isTokenValid(rawToken, user, TokenType.REFRESH))
            throw new TokenExpiredException();

        if (!hashedRawToken.equals(stored.getTokenHash()))
            throw new InvalidTokenException("Token signature mismatch");

        if (!stored.getUser().getId().equals(user.getId()))
            throw new SecurityException("Token ownership mismatch");

        return new TokenValidationResult(user, stored, refreshTokenId);
    }
}
