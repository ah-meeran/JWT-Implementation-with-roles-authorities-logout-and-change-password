package com.example.demo.controller.api.admin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping
    public String getAdmins() {
        return "Hello Admin";
    }

    @PostMapping
    public String postMethodName() {
        // TODO: process POST request
        return "Hello Admin for post ";

    }

    @PutMapping
    public String putMethodName() {

        return "Hello Admin for put ";
    }

    @DeleteMapping
    public String deleteMethodName() {
        return "Hello Admin for Delete";

    }
}
