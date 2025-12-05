package com.example.IGORPROYECTO.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.IGORPROYECTO.model.EmailDocument;
import com.example.IGORPROYECTO.repository.EmailRepository;

@Controller
public class EmailController {

    @Autowired
    private EmailRepository emailRepository;

    // Vista de bandeja de entrada
    @GetMapping("/emails")
    public String bandeja(Model model, Principal principal) {
        String correoUsuario = principal.getName(); // correo del logueado

        List<EmailDocument> lista = emailRepository.findByDestinatarioOrderByFechaDesc(correoUsuario);


        model.addAttribute("emails", lista);
        return "Correos/bandeja";
    }

    // Vista para leer un correo
    @GetMapping("/emails/ver/{id}")
    public String verCorreo(@PathVariable String id, Model model) {
        EmailDocument correo = emailRepository.findById(id).orElse(null);
        if (correo == null) return "redirect:/emails";

        model.addAttribute("correo", correo);
        return "Correos/ver";
    }
}
