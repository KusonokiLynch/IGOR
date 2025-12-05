package com.example.IGORPROYECTO.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;

@Controller
public class HistorialCambiosController {

    @Autowired
    private DocumentacionRepository documentacionRepository;

    @GetMapping("/HistorialCambios")
    public String mostrarHistorial(Model model) {
        List<Documentacion> documentacion = documentacionRepository.findAll(); // Obtener todos los proyectos
        model.addAttribute("documentacion", documentacion); // Pasar a la vista
        return "Clientes/HistorialCambios"; // Thymeleaf buscará HistorialCambios.html
    }
}
