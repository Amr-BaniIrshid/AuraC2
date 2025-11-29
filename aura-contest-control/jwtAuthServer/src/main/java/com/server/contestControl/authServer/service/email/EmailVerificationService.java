package com.server.contestControl.authServer.service.email;

import com.server.contestControl.authServer.entity.EmailVerificationToken;
import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.exception.api.InvalidVerificationTokenException;
import com.server.contestControl.authServer.exception.api.VerificationTokenExpiredException;
import com.server.contestControl.authServer.repository.EmailVerificationTokenRepository;
import com.server.contestControl.authServer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;


    @Transactional
    public String verify(String token) {
        EmailVerificationToken verificationToken= tokenRepository.findByToken(token)
                .orElseThrow(InvalidVerificationTokenException::new);

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new VerificationTokenExpiredException();
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return "Email verified successfully!";
    }
}
