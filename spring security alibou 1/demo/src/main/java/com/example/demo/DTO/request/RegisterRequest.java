package com.example.demo.DTO.request;

import java.util.List;
import java.util.Set;

import com.example.demo.model.permission.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

    private Set<String> permissions;

}
