package com.example.IGORPROYECTO.controller;

import com.example.IGORPROYECTO.model.Recurso;
import com.example.IGORPROYECTO.service.RecursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    // Listar recursos
    @GetMapping
    public String menuRecurso(Model model) {
        model.addAttribute("recursos", recursoService.menuRecurso());
        return "recursos/menuRecurso"; // templates/recursos/listar.html
    }
    //disponible
    @GetMapping("/recursoDisponible")
    public String consultarDisponibles(Model model) {
    model.addAttribute("recursos", recursoService.recursoDisponible()); 
    return "recursos/recursoDisponible";
}

// Formulario para crear nuevo recurso
    @GetMapping("/recursoNuevo")
    public String nuevo(Model model) {
        model.addAttribute("recurso", new Recurso());
        return "recursos/recursoNuevo"; // templates/recursos/nuevo.html
    }

    // Guardar recurso
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Recurso recurso) {
        recursoService.guardar(recurso);
        return "redirect:/recursos";
    }

    // Formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Recurso recurso = recursoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("recurso", recurso);
        return "recursos/editar"; // templates/recursos/editar.html
    }

    // Actualizar recurso
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id, @ModelAttribute Recurso recurso) {
        recurso.setId(id);
        recursoService.guardar(recurso);
        return "redirect:/recursos";
    }

    // Eliminar recurso
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        recursoService.eliminar(id);
        return "redirect:/recursos";
    }
}
