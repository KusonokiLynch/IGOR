package com.example.IGORPROYECTO.controller;

import java.util.List;

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

    // Mostrar formulario para crear nuevo proyecto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyectos/nuevo";
    }

    // Guardar proyecto (POST)
    @PostMapping("/nuevo")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.nuevo(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping
    public String menuProyectos(Model model) {
        model.addAttribute("proyectos", proyectoService.menuTodos());
        return "proyectos/menu";
    }

    @GetMapping("/consultar")
    public String consultarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoService.consultarTodos();
        
        // Calcular estadísticas
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
        
        // Pasar datos al modelo
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("totalProyectos", totalProyectos);
        model.addAttribute("activos", activos);
        model.addAttribute("enEjecucion", enEjecucion);
        model.addAttribute("finalizados", finalizados);
        
        return "proyectos/consultar";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id);
        model.addAttribute("proyecto", proyecto);
        return "proyectos/editar";
    }

    // Guardar cambios del proyecto editado
    @PostMapping("/editar/{id}")
    public String guardarCambios(@PathVariable String id, @ModelAttribute Proyecto proyecto) {
        proyectoService.actualizar(id, proyecto);
        return "redirect:/proyectos/consultar";
    }
}