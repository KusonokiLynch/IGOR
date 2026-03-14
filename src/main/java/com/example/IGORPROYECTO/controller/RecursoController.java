package com.example.IGORPROYECTO.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.IGORPROYECTO.model.Recurso;
import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.dto.RecursoDTO;
import com.example.IGORPROYECTO.repository.UsuarioRepository;
import com.example.IGORPROYECTO.service.RecursoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @GetMapping
    public String menuRecurso(Model model, Authentication auth) {
        try {
            List<Recurso> recursos = recursoService.menuRecurso();
            
            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            long totalRecursos = recursos.size();
            long disponibles = recursos.stream().filter(Recurso::isDisponibilidad).count();
            long noDisponibles = recursos.stream().filter(r -> !r.isDisponibilidad()).count();
            long asignados = recursos.stream()
                .filter(r -> r.getAsignadoA() != null && !r.getAsignadoA().isEmpty()).count();
            
            Map<String, Long> estadisticas = new HashMap<>();
            estadisticas.put("totalRecursos", totalRecursos);
            estadisticas.put("disponibles", disponibles);
            estadisticas.put("noDisponibles", noDisponibles);
            estadisticas.put("asignados", asignados);
            
            model.addAttribute("recursos", recursos);
            model.addAttribute("estadisticas", estadisticas);
            model.addAttribute("rol", rol);
            model.addAttribute("totalRecursos", totalRecursos);
            model.addAttribute("disponibles", disponibles);
            model.addAttribute("noDisponibles", noDisponibles);
            model.addAttribute("asignados", asignados);
            
            log.info("Menu de recursos cargado. Total: {}", totalRecursos);
        } catch (Exception e) {
            log.error("Error al cargar menu de recursos", e);
        }
        
        return "Recursos/menuRecurso";
    }

    @GetMapping("/recursoDisponible")
    public String consultarDisponibles(Model model, Authentication auth) {
        try {
            List<Recurso> recursosDisponibles = recursoService.recursoDisponible();
            List<Recurso> todosLosRecursos = recursoService.menuRecurso();
            
            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            long totalRecursos = todosLosRecursos.size();
            long disponibles = todosLosRecursos.stream().filter(Recurso::isDisponibilidad).count();
            long noDisponibles = todosLosRecursos.stream().filter(r -> !r.isDisponibilidad()).count();
            long asignados = todosLosRecursos.stream()
                .filter(r -> r.getAsignadoA() != null && !r.getAsignadoA().isEmpty()).count();
            
            Map<String, Long> estadisticas = new HashMap<>();
            estadisticas.put("totalRecursos", totalRecursos);
            estadisticas.put("disponibles", disponibles);
            estadisticas.put("noDisponibles", noDisponibles);
            estadisticas.put("asignados", asignados);
            
            model.addAttribute("recursos", recursosDisponibles);
            model.addAttribute("estadisticas", estadisticas);
            model.addAttribute("rol", rol);
            model.addAttribute("totalRecursos", totalRecursos);
            model.addAttribute("disponibles", disponibles);
            model.addAttribute("noDisponibles", noDisponibles);
            model.addAttribute("asignados", asignados);
            
            log.info("Recursos disponibles consultados. Total disponibles: {}", disponibles);
        } catch (Exception e) {
            log.error("Error al consultar recursos disponibles", e);
        }
        
        return "Recursos/recursoDisponible";
    }

    @GetMapping("/recursoNuevo")
    public String nuevo(Model model) {
        model.addAttribute("recurso", new RecursoDTO());
        return "Recursos/recursoNuevo";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute RecursoDTO recursoDTO) {
        try {
            Recurso recurso = convertirDTOaRecurso(recursoDTO);
            recursoService.guardar(recurso);
            log.info("Recurso guardado exitosamente: {}", recurso.getNombre());
        } catch (Exception e) {
            log.error("Error al guardar recurso", e);
        }
        return "redirect:/recursos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        try {
            Recurso recurso = recursoService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            RecursoDTO recursoDTO = convertirRecursoaDTO(recurso);
            model.addAttribute("recurso", recursoDTO);
        } catch (Exception e) {
            log.error("Error al obtener recurso para editar: {}", id, e);
        }
        return "Recursos/editar";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id, @ModelAttribute RecursoDTO recursoDTO) {
        try {
            Recurso recurso = convertirDTOaRecurso(recursoDTO);
            recurso.setId(id);
            recursoService.guardar(recurso);
            log.info("Recurso actualizado exitosamente: {}", id);
        } catch (Exception e) {
            log.error("Error al actualizar recurso: {}", id, e);
        }
        return "redirect:/recursos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        try {
            recursoService.eliminar(id);
            log.info("Recurso eliminado exitosamente: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar recurso: {}", id, e);
        }
        return "redirect:/recursos";
    }

    // Mostrar formulario de asignación
    @GetMapping("/asignar/{id}")
    public String mostrarAsignar(@PathVariable String id, Model model) {
        try {
            Recurso recurso = recursoService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));

            // Traer solo Director, Supervisor y Trabajador
            List<Usuario> usuarios = usuarioRepository.findAll().stream()
                    .filter(u -> u.getRol() != null && (
                            u.getRol().contains("DIRECTOR") ||
                            u.getRol().contains("SUPERVISOR") ||
                            u.getRol().contains("TRABAJADOR")))
                    .collect(Collectors.toList());

            // Mapa id -> nombre para mostrar asignaciones actuales
            Map<String, String> usuariosMap = usuarios.stream()
                    .collect(Collectors.toMap(Usuario::getId, Usuario::getNombre_completo));

            model.addAttribute("recurso", recurso);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuariosMap", usuariosMap);
            
            log.info("Formulario de asignación de recurso cargado: {}", id);
        } catch (Exception e) {
            log.error("Error al mostrar formulario de asignación: {}", id, e);
        }

        return "Recursos/AsignarRecurso";
    }

    // Procesar asignación
    @PostMapping("/asignar/{id}")
    public String procesarAsignar(@PathVariable String id,
                                   @RequestParam String usuarioId,
                                   Model model) {
        try {
            recursoService.asignar(id, usuarioId);
            log.info("Recurso asignado exitosamente. Recurso: {}, Usuario: {}", id, usuarioId);
            return "redirect:/recursos";
        } catch (Exception e) {
            log.error("Error al asignar el recurso: {}", id, e);
            model.addAttribute("error", "Error al asignar el recurso: " + e.getMessage());
            return mostrarAsignar(id, model);
        }
    }

    // Desasignar usuario de recurso
    @PostMapping("/desasignar/{id}")
    public String desasignar(@PathVariable String id,
                              @RequestParam String usuarioId) {
        try {
            recursoService.desasignar(id, usuarioId);
            log.info("Recurso desasignado exitosamente. Recurso: {}, Usuario: {}", id, usuarioId);
        } catch (Exception e) {
            log.error("Error al desasignar recurso: {}", id, e);
        }
        return "redirect:/recursos/asignar/" + id;
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO <-> ENTIDAD ====================

    private Recurso convertirDTOaRecurso(RecursoDTO dto) {
        Recurso recurso = new Recurso();
        recurso.setId(dto.getId());
        recurso.setNombre(dto.getNombre());
        recurso.setTipo(dto.getTipo());
        recurso.setCantidad(dto.getCantidad());
        recurso.setDisponibilidad(dto.isDisponibilidad());
        recurso.setAsignadoA(dto.getAsignadoA());
        return recurso;
    }

    private RecursoDTO convertirRecursoaDTO(Recurso recurso) {
        RecursoDTO dto = new RecursoDTO();
        dto.setId(recurso.getId());
        dto.setNombre(recurso.getNombre());
        dto.setTipo(recurso.getTipo());
        dto.setCantidad(recurso.getCantidad());
        dto.setDisponibilidad(recurso.isDisponibilidad());
        dto.setAsignadoA(recurso.getAsignadoA());
        return dto;
    }
}