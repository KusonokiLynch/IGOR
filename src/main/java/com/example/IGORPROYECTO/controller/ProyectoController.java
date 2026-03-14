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
import com.example.IGORPROYECTO.dto.ProyectoDTO;
import com.example.IGORPROYECTO.service.ProyectoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new ProyectoDTO());
        return "Proyectos/nuevo";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/nuevo")
    public String guardarProyecto(@ModelAttribute ProyectoDTO proyectoDTO) {
        try {
            Proyecto proyecto = convertirDTOaProyecto(proyectoDTO);
            proyectoService.nuevo(proyecto);
            log.info("Proyecto creado exitosamente: {}", proyecto.getNombre());
        } catch (Exception e) {
            log.error("Error al crear proyecto", e);
        }
        return "redirect:/proyectos";
    }

    @GetMapping
    public String menuProyectos(Model model) {
        try {
            model.addAttribute("proyectos", proyectoService.menuTodos());
        } catch (Exception e) {
            log.error("Error al obtener menu de proyectos", e);
        }
        return "Proyectos/menu";
    }

    @GetMapping("/consultar")
    public String consultarProyectos(Model model, Authentication auth) {
        try {
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
            
            log.info("Consulta de proyectos realizada. Total: {}", totalProyectos);
        } catch (Exception e) {
            log.error("Error al consultar proyectos", e);
        }
        
        return "Proyectos/consultar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id);
            ProyectoDTO proyectoDTO = convertirProyectoaDTO(proyecto);
            model.addAttribute("proyecto", proyectoDTO);
        } catch (Exception e) {
            log.error("Error al obtener formulario de edición para proyecto: {}", id, e);
        }
        return "Proyectos/editar";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/editar/{id}")
    public String guardarCambios(@PathVariable String id, @ModelAttribute ProyectoDTO proyectoDTO) {
        try {
            Proyecto proyecto = convertirDTOaProyecto(proyectoDTO);
            proyectoService.actualizar(id, proyecto);
            log.info("Proyecto actualizado exitosamente: {}", id);
        } catch (Exception e) {
            log.error("Error al actualizar proyecto: {}", id, e);
        }
        return "redirect:/proyectos/consultar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable String id, Authentication auth) {
        try {
            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            if (!"DIRECTOR".equals(rol) && !"SUPERVISOR".equals(rol)) {
                return "Error/403";
            }
            
            proyectoService.eliminar(id);
            log.info("Proyecto eliminado exitosamente: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar proyecto: {}", id, e);
        }
        
        return "redirect:/proyectos/consultar";
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO <-> ENTIDAD ====================

    private Proyecto convertirDTOaProyecto(ProyectoDTO dto) {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(dto.getId());
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setEstado(dto.getEstado());
        proyecto.setPrograma(dto.getPrograma());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFinal(dto.getFechaFinal());
        proyecto.setResponsable(dto.getResponsable());
        proyecto.setCliente(dto.getCliente());
        proyecto.setSerial(dto.getSerial());
        proyecto.setFechaCreacion(dto.getFechaCreacion());
        return proyecto;
    }

    private ProyectoDTO convertirProyectoaDTO(Proyecto proyecto) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setNombre(proyecto.getNombre());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setEstado(proyecto.getEstado());
        dto.setPrograma(proyecto.getPrograma());
        dto.setFechaInicio(proyecto.getFechaInicio());
        dto.setFechaFinal(proyecto.getFechaFinal());
        dto.setResponsable(proyecto.getResponsable());
        dto.setCliente(proyecto.getCliente());
        dto.setSerial(proyecto.getSerial());
        dto.setFechaCreacion(proyecto.getFechaCreacion());
        return dto;
    }
}