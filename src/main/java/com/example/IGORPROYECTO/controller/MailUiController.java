package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailUiController {
    @GetMapping("/bandeja")
    public String bandeja() {
        return "bandeja"; // src/main/resources/templates/bandeja.html
    }
}
