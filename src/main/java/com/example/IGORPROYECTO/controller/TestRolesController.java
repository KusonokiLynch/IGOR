package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class TestRolesController {

    @GetMapping("/admin/test")
    public String adminTest(Authentication auth, Model model) {
        model.addAttribute("mensaje", "Bienvenido DIRECTOR! Tu usuario es: " + auth.getName());
        model.addAttribute("rol", auth.getAuthorities());
        return "home"; // reutiliza tu página home
    }

    @GetMapping("/cliente/test") 
    public String clienteTest(Authentication auth, Model model) {
        model.addAttribute("mensaje", "Bienvenido CLIENTE! Tu usuario es: " + auth.getName());
        model.addAttribute("rol", auth.getAuthorities());
        return "home"; 
    }

    @GetMapping("/trabajador/test")
    public String trabajadorTest(Authentication auth, Model model) {
        model.addAttribute("mensaje", "Bienvenido TRABAJADOR! Tu usuario es: " + auth.getName());
        return "home";
    }

    @GetMapping("/supervisor/test")
    public String supervisorTest(Authentication auth, Model model) {
        model.addAttribute("mensaje", "Bienvenido SUPERVISOR! Tu usuario es: " + auth.getName());
        return "home";
    }
}