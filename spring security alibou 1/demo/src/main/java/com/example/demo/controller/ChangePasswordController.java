package com.example.demo.controller;

import com.example.demo.DTO.changePassword.ChangePasswordDto;
import com.example.demo.service.auth.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-enquiry")

public class ChangePasswordController {
    private final ChangePasswordService changePasswordService;

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto, Principal principal) {
        changePasswordService.ChangePasswordMechanism(changePasswordDto, principal);
        return ResponseEntity.ok().build();
    }
}
