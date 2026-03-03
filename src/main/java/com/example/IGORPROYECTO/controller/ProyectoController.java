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

import com.example.IGORPROYECTO.model.Proyecto;
import com.example.IGORPROYECTO.service.ProyectoService;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "Proyectos/nuevo";
    }

    @PostMapping("/nuevo")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.nuevo(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping
    public String menuProyectos(Model model) {
        model.addAttribute("proyectos", proyectoService.menuTodos());
        return "Proyectos/menu";
    }

    @GetMapping("/consultar")
    public String consultarProyectos(Model model, Authentication auth) {
        List<Proyecto> proyectos = proyectoService.consultarTodos();
        
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
        long totalProyectos = proyectos.size();
        long activos = proyectos.stream()
            .filter(p -> "Activo".equalsIgnoreCase(p.getEstado()))
            .count();
        long enEjecucion = proyectos.stream()
            .filter(p -> "En Ejecución".equalsIgnoreCase(p.getEstado()))
            .count();
        long finalizados = proyectos.stream()
            .filter(p -> "Finalizado".equalsIgnoreCase(p.getEstado()))
            .count();
        
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalProyectos", totalProyectos);
        estadisticas.put("activos", activos);
        estadisticas.put("enEjecucion", enEjecucion);
        estadisticas.put("finalizados", finalizados);
        
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("rol", rol);
        model.addAttribute("totalProyectos", totalProyectos);
        model.addAttribute("activos", activos);
        model.addAttribute("enEjecucion", enEjecucion);
        model.addAttribute("finalizados", finalizados);
        
        return "Proyectos/consultar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id);
        model.addAttribute("proyecto", proyecto);
        return "Proyectos/editar";
    }

    @PostMapping("/editar/{id}")
    public String guardarCambios(@PathVariable String id, @ModelAttribute Proyecto proyecto) {
        proyectoService.actualizar(id, proyecto);
        return "redirect:/proyectos/consultar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable String id, Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
        if (!"DIRECTOR".equals(rol) && !"SUPERVISOR".equals(rol)) {
            return "Error/403";
        }
        
        proyectoService.eliminar(id);
        return "redirect:/proyectos/consultar";
    }
}