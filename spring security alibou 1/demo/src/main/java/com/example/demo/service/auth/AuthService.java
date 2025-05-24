package com.example.demo.service.auth;

import com.example.demo.DTO.request.AuthendicateRequest;
import com.example.demo.DTO.request.RegisterRequest;
import com.example.demo.DTO.response.AuthenticationResponse;
import com.example.demo.config.security.JwtService;
import com.example.demo.model.permission.Permission;
import com.example.demo.model.token.Token;
import com.example.demo.model.token.TokenType;
import com.example.demo.model.user.CustomerUser;
import com.example.demo.repository.CustomerUserRepository;
import com.example.demo.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final CustomerUserRepository customerUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        if (customerUserRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Set<Permission> permissionSet = new HashSet<>();
        if (request.getPermissions() != null) {
            permissionSet = request.getPermissions().stream()
                    .map(Permission::valueOf)
                    .collect(Collectors.toSet());
        }

        CustomerUser user = CustomerUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .permission(permissionSet)
                .build();

        var user_info = customerUserRepo.save(user);
        revokeAllUserTokens(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, user_info, TokenType.BEARER);
        saveUserToken(refreshToken, user_info, TokenType.REFRESH);

        return AuthenticationResponse.builder()
                .access_token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void revokeAllUserTokens(CustomerUser customerUser) {
        var validAllToken = tokenRepository.findAllValidToken(customerUser.getId());
        validAllToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validAllToken);
    }

    private void saveUserToken(String token, CustomerUser user_info, TokenType tokenType) {
        Token tokenEntity = Token.builder()
                .token(token)
                .tokenType(tokenType)
                .customerUser(user_info)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(tokenEntity);
    }

    public AuthenticationResponse authenticate(AuthendicateRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            var user = customerUserRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            revokeAllUserTokens(user);

            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            saveUserToken(accessToken, user, TokenType.BEARER);
            saveUserToken(refreshToken, user, TokenType.REFRESH);

            return AuthenticationResponse.builder()
                    .access_token(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public AuthenticationResponse updateUserPermissions(Integer userId, List<String> permissions) {
        CustomerUser user = customerUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Permission> permissionSet = permissions.stream()
                .map(Permission::valueOf)
                // .map(Permission::fromPermissionString) {this is used for lowe case}
                .collect(Collectors.toSet());

        user.setPermission(permissionSet);
        customerUserRepo.save(user);

        return AuthenticationResponse.builder()
                .access_token("Permissions updated successfully")
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        AuthenticationResponse authResponse = null;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        String userNameFromToken = jwtService.extractUserName(refreshToken);

        if (userNameFromToken != null) {
            var user = this.customerUserRepo.findByEmail(userNameFromToken)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var storedToken = tokenRepository.findByToken(refreshToken)
                    .filter(t -> t.getTokenType() == TokenType.REFRESH)
                    .filter(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(null);

            if (storedToken != null && jwtService.isTokenValid(refreshToken, user)) {
                revokeAllUserTokens(user);

                String newAccessToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);

                saveUserToken(newAccessToken, user, TokenType.BEARER);
                saveUserToken(newRefreshToken, user, TokenType.REFRESH);

                authResponse = AuthenticationResponse.builder()
                        .access_token(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            }
        }

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }
}
