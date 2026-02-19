package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestErrorController {

    @GetMapping("/test-403")
    public String test403() {
        return "Error/403";
    }
}   