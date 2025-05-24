package com.example.demo.service.auth;


import com.example.demo.DTO.changePassword.ChangePasswordDto;
import com.example.demo.model.user.CustomerUser;
import com.example.demo.repository.CustomerUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerUserRepository customerUserRepository;

    public void ChangePasswordMechanism(ChangePasswordDto changePasswordDto, Principal connectedUser) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        //var user1 = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var user = (CustomerUser) authentication.getPrincipal();
        //CHECK THE CURRENT PASSWORD

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("password not match");

        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConformPassword())) {
            throw new IllegalStateException("conform password not match");
        }

        var current_user = customerUserRepository.findByEmail(user.getUsername()).orElseThrow(() -> new IllegalStateException("user not found"));

        System.out.println("current_user = " + changePasswordDto.getNewPassword() + passwordEncoder.encode(changePasswordDto.getNewPassword()));
        current_user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        customerUserRepository.save(current_user);


        //
    }
}
