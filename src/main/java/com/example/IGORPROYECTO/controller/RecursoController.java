package com.example.IGORPROYECTO.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.IGORPROYECTO.model.Recurso;
import com.example.IGORPROYECTO.service.RecursoService;

@Controller
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    // Listar recursos con estadísticas
    @GetMapping
    public String menuRecurso(Model model) {
        List<Recurso> recursos = recursoService.menuRecurso();
        
        // Calcular estadísticas
        long totalRecursos = recursos.size();
        long disponibles = recursos.stream()
            .filter(Recurso::isDisponibilidad)
            .count();
        long noDisponibles = recursos.stream()
            .filter(r -> !r.isDisponibilidad())
            .count();
        long asignados = recursos.stream()
            .filter(r -> r.getAsignadoA() != null && !r.getAsignadoA().isEmpty())
            .count();
        
        // Pasar datos al modelo
        model.addAttribute("recursos", recursos);
        model.addAttribute("totalRecursos", totalRecursos);
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("noDisponibles", noDisponibles);
        model.addAttribute("asignados", asignados);
        
        return "recursos/menuRecurso";
    }

    // Disponibles con estadísticas
    @GetMapping("/recursoDisponible")
    public String consultarDisponibles(Model model) {
        List<Recurso> recursosDisponibles = recursoService.recursoDisponible();
        List<Recurso> todosLosRecursos = recursoService.menuRecurso(); // Para calcular estadísticas globales
        
        // Calcular estadísticas con TODOS los recursos
        long totalRecursos = todosLosRecursos.size();
        long disponibles = todosLosRecursos.stream()
            .filter(Recurso::isDisponibilidad)
            .count();
        long noDisponibles = todosLosRecursos.stream()
            .filter(r -> !r.isDisponibilidad())
            .count();
        long asignados = todosLosRecursos.stream()
            .filter(r -> r.getAsignadoA() != null && !r.getAsignadoA().isEmpty())
            .count();
        
        // Pasar datos al modelo
        model.addAttribute("recursos", recursosDisponibles); // Solo disponibles para la tabla
        model.addAttribute("totalRecursos", totalRecursos);
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("noDisponibles", noDisponibles);
        model.addAttribute("asignados", asignados);
        
        return "recursos/recursoDisponible";
    }

    // Formulario para crear nuevo recurso
    @GetMapping("/recursoNuevo")
    public String nuevo(Model model) {
        model.addAttribute("recurso", new Recurso());
        return "recursos/recursoNuevo";
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
        return "recursos/editar";
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