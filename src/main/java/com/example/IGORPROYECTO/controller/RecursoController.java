package com.example.IGORPROYECTO.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
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
    public String menuRecurso(Model model, Authentication auth) {
        List<Recurso> recursos = recursoService.menuRecurso();
        
        // ✅ AGREGADO: Obtener rol sin prefijo
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
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
        
        // Crear Map de estadísticas
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalRecursos", totalRecursos);
        estadisticas.put("disponibles", disponibles);
        estadisticas.put("noDisponibles", noDisponibles);
        estadisticas.put("asignados", asignados);
        
        // Pasar datos al modelo
        model.addAttribute("recursos", recursos);
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("rol", rol); // ✅ AGREGADO
        
        // Mantener compatibilidad
        model.addAttribute("totalRecursos", totalRecursos);
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("noDisponibles", noDisponibles);
        model.addAttribute("asignados", asignados);
        
        return "Recursos/menuRecurso";
    }

    // Disponibles con estadísticas
    @GetMapping("/recursoDisponible")
    public String consultarDisponibles(Model model, Authentication auth) {
        List<Recurso> recursosDisponibles = recursoService.recursoDisponible();
        List<Recurso> todosLosRecursos = recursoService.menuRecurso();
        
        // ✅ AGREGADO: Obtener rol sin prefijo
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
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
        
        // Crear Map de estadísticas
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalRecursos", totalRecursos);
        estadisticas.put("disponibles", disponibles);
        estadisticas.put("noDisponibles", noDisponibles);
        estadisticas.put("asignados", asignados);
        
        // Pasar datos al modelo
        model.addAttribute("recursos", recursosDisponibles);
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("rol", rol); // ✅ AGREGADO
        
        // Mantener compatibilidad
        model.addAttribute("totalRecursos", totalRecursos);
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("noDisponibles", noDisponibles);
        model.addAttribute("asignados", asignados);
        
        return "Recursos/recursoDisponible";
    }

    // Formulario para crear nuevo recurso
    @GetMapping("/recursoNuevo")
    public String nuevo(Model model) {
        model.addAttribute("recurso", new Recurso());
        return "Recursos/recursoNuevo";
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
        return "Recursos/editar";
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