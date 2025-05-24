package com.example.demo.controller.auth;

import com.example.demo.DTO.request.AuthendicateRequest;
import com.example.demo.DTO.request.RegisterRequest;
import com.example.demo.DTO.response.AuthenticationResponse;
import com.example.demo.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/authendicate")
    public ResponseEntity<AuthenticationResponse> authendicate(@RequestBody AuthendicateRequest authendicateRequest) {
        return ResponseEntity.ok(authService.authenticate(authendicateRequest));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PutMapping("/update-permissions/{userId}")
    public ResponseEntity<AuthenticationResponse> updateUserPermissions(
            @PathVariable Integer userId,
            @RequestBody List<String> permissions) {
        return ResponseEntity.ok(authService.updateUserPermissions(userId, permissions));
    }
}
