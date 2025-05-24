package com.example.demo.service.auth;

import com.example.demo.model.token.Token;
import com.example.demo.model.token.TokenType;
import com.example.demo.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Logout attempted without valid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        log.info("Processing logout for token: {}", token);

        var storedToken = tokenRepository.findByToken(token)
                .filter(t -> t.getTokenType() == TokenType.BEARER)
                .orElse(null);

        if (storedToken == null) {
            log.warn("No valid token found for logout");

            return;
        }

        log.info("Found token for user: {}", storedToken.getCustomerUser().getEmail());

        // Get all valid tokens for the user
        List<Token> userTokens = tokenRepository.findAllValidToken(storedToken.getCustomerUser().getId());
        log.info("Found {} valid tokens to revoke", userTokens.size());

        // Revoke all tokens
        userTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
            log.info("Revoking token: {} of type: {}", t.getToken(), t.getTokenType());
        });

        // Save all revoked tokens
        List<Token> savedTokens = tokenRepository.saveAll(userTokens);
        log.info("Successfully revoked {} tokens", savedTokens.size());

        // Verify the tokens were actually revoked
        savedTokens.forEach(t -> {
            if (!t.isExpired() || !t.isRevoked()) {
                log.error("Token {} was not properly revoked!", t.getToken());
            }
        });
    }
}
