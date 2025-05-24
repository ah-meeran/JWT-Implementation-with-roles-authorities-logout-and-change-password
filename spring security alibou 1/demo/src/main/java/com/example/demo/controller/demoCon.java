package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class demoCon {
    @GetMapping("/1")
    public String getMethodName1() {
        return "first endpoint";
    }

    @GetMapping("/2")
    public String getMethodName() {
        return "secondf endpoint";
    }

}
