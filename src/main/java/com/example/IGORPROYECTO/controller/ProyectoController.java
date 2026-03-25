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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/nuevo")
    public String guardarProyecto(@ModelAttribute ProyectoDTO proyectoDTO,
                                   RedirectAttributes redirectAttributes) {
        log.info("========== POST /proyectos/nuevo INICIADO ==========");
        
        try {
            log.info("STEP 1 - Datos recibidos del formulario:");
            log.info("  - Nombre: {}", proyectoDTO.getNombre());
            log.info("  - Descripción: {}", proyectoDTO.getDescripcion() != null ? "✅ Presente" : "❌ NULL");
            log.info("  - Estado: {}", proyectoDTO.getEstado());
            log.info("  - Responsable: {}", proyectoDTO.getResponsable());
            log.info("  - Cliente: {}", proyectoDTO.getCliente());
            
            // Validaciones básicas
            if (proyectoDTO.getNombre() == null || proyectoDTO.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del proyecto es obligatorio");
            }
            if (proyectoDTO.getResponsable() == null || proyectoDTO.getResponsable().trim().isEmpty()) {
                throw new IllegalArgumentException("El responsable es obligatorio");
            }
            if (proyectoDTO.getCliente() == null || proyectoDTO.getCliente().trim().isEmpty()) {
                throw new IllegalArgumentException("El cliente es obligatorio");
            }
            
            log.info("STEP 2 - Convirtiendo DTO a Proyecto...");
            Proyecto proyecto = convertirDTOaProyecto(proyectoDTO);
            log.info("  ✅ Proyecto creado");
            log.info("  - Nombre: {}", proyecto.getNombre());
            
            log.info("STEP 3 - Guardando en la BD...");
            proyectoService.nuevo(proyecto);
            log.info("  ✅ Proyecto guardado exitosamente");
            log.info("  - ID generado: {}", proyecto.getId());
            
            redirectAttributes.addFlashAttribute("mensaje", "✅ Proyecto creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            log.info("========== GUARDADO COMPLETADO ==========");
            return "redirect:/proyectos";
            
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Error de validación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/proyectos/nuevo";
            
        } catch (Exception e) {
            log.error("❌ Error al guardar proyecto", e);
            log.error("Tipo de error: {}", e.getClass().getName());
            log.error("Mensaje: {}", e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear proyecto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/proyectos/nuevo";
        }
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

    @PostMapping("/editar/{id}")
    public String guardarCambios(@PathVariable String id, @ModelAttribute ProyectoDTO proyectoDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            Proyecto proyecto = convertirDTOaProyecto(proyectoDTO);
            proyectoService.actualizar(id, proyecto);
            log.info("Proyecto actualizado exitosamente: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Proyecto actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al actualizar proyecto: {}", id, e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/proyectos/consultar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable String id, Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            if (!"DIRECTOR".equals(rol) && !"SUPERVISOR".equals(rol)) {
                return "Error/403";
            }
            
            proyectoService.eliminar(id);
            log.info("Proyecto eliminado exitosamente: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Proyecto eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar proyecto: {}", id, e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
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