package com.example.demo.controller.api.manager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/v1/manager")

public class ManagerController {

    @GetMapping
    public String getMethodName() {
        return "Manager for get";
    }

    @PostMapping
    public String postMethodName() {

        return "Manager post";

    }

    @PutMapping
    public String putMethodName() {

        return "Manager Put";
    }

    @DeleteMapping
    public String deleteMethod() {
        return "Delete to admin";
    }

}
