package com.server.contestControl.authServer.service.login;

import com.server.contestControl.authServer.dto.login.LoginRequest;
import com.server.contestControl.authServer.dto.login.LoginResponse;
import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.exception.api.EmailNotVerifiedException;
import com.server.contestControl.authServer.exception.api.InvalidCredentialsException;
import com.server.contestControl.authServer.exception.api.UserNotFoundException;
import com.server.contestControl.authServer.repository.UserRepository;
import com.server.contestControl.authServer.util.TokenIssuerUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenIssuerUtil tokenIssuerUtil;

    @Transactional
    public LoginResponse login(LoginRequest request, String deviceIp, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        validateCredentials(user, request);

        String accessToken = tokenIssuerUtil.issueTokens(user, deviceIp, response);

        return new LoginResponse(accessToken, "Login successful");
    }

    private void validateCredentials(User user, LoginRequest req) {
        if (!user.isEmailVerified()) throw new EmailNotVerifiedException();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException();
    }
}
