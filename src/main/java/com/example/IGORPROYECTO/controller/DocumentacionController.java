package com.example.IGORPROYECTO.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.IGORPROYECTO.Command.CargarDocumentacionCommand;
import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.dto.DocumentacionDTO;
import com.example.IGORPROYECTO.repository.DocumentacionRepository; 
import com.example.IGORPROYECTO.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DocumentacionController {

    @Autowired
    private DocumentacionRepository repo;
    
    @Autowired
    private UsuarioRepository usuarioRepository; 

    @GetMapping("/documentacion")
    public String mostrarDocumentacion(Model model) {
        model.addAttribute("documentacion", new DocumentacionDTO());
        return "Documentacion/documentacion";
    }
    
    @GetMapping("/NuevoDocumento")
    public String mostrarFormulario(Model model) {
        model.addAttribute("documentacion", new DocumentacionDTO());
        return "Documentacion/NuevoDocumento";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/Documentacion/guardar")
    public String guardarDocumento(@ModelAttribute DocumentacionDTO documentacionDTO, Authentication authentication) {
        try {
            Documentacion documentacion = convertirDTOaDocumentacion(documentacionDTO);
            documentacion.setFechaCreacion(new Date());
            
            if (documentacion.getEstado() != null) {
                documentacion.setEstado(documentacion.getEstado().toUpperCase());
            }
            
            if (documentacion.getEstado() == null || documentacion.getEstado().isEmpty()) {
                documentacion.setEstado("ACTIVO");
            }
            
            if (documentacion.getPropietario() == null || documentacion.getPropietario().isEmpty()) {
                String nombreUsuario = obtenerNombreUsuarioActual(authentication);
                documentacion.setPropietario(nombreUsuario);
            }
            
            repo.save(documentacion);
            log.info("Documento guardado exitosamente: {}", documentacion.getId());
        } catch (Exception e) {
            log.error("Error al guardar documento", e);
        }
        return "redirect:/Documentacion/editar";
    }

    @GetMapping("/Documentacion/editar")
    public String mostrarHistorial(Model model, Authentication authentication) {
        try {
            List<Documentacion> documentacion;
            
            String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            if ("DIRECTOR".equals(rolUsuario) || "SUPERVISOR".equals(rolUsuario)) {
                documentacion = repo.findAll();
            } else {
                documentacion = repo.findByEstado("ACTIVO");
            }
            
            model.addAttribute("documentacion", documentacion);
            model.addAttribute("rol", rolUsuario);
            model.addAttribute("esDirector", "DIRECTOR".equals(rolUsuario));
            model.addAttribute("esSupervisor", "SUPERVISOR".equals(rolUsuario));
        } catch (Exception e) {
            log.error("Error al obtener historial de documentación", e);
        }
        return "Documentacion/EditarDocumento";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/Documentacion/editar")
    public String editarDocumento(@ModelAttribute DocumentacionDTO documentacionDTO) {
        try {
            Documentacion documentacion = convertirDTOaDocumentacion(documentacionDTO);
            
            Documentacion docExistente = repo.findById(documentacion.getId()).orElse(null);
            if (docExistente != null) {
                documentacion.setFechaCreacion(docExistente.getFechaCreacion());
            } else {
                documentacion.setFechaCreacion(new Date());
            }
            repo.save(documentacion);
            log.info("Documento actualizado exitosamente: {}", documentacion.getId());
        } catch (Exception e) {
            log.error("Error al editar documento", e);
        }
        return "redirect:/Documentacion/editar";
    }

    @GetMapping("/documentacion/eliminar/{id}")
    public String eliminarDocumento(@PathVariable("id") String id, Authentication authentication) {
        try {
            String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            if (!"DIRECTOR".equals(rolUsuario) && !"SUPERVISOR".equals(rolUsuario)) {
                return "Error/403";
            }
            
            Documentacion doc = repo.findById(id).orElse(null);
            if (doc != null) {
                doc.setEstado("INACTIVO");
                repo.save(doc);
                log.info("Documento marcado como INACTIVO: {}", id);
            }
        } catch (Exception e) {
            log.error("Error al eliminar documento: {}", id, e);
        }
        return "redirect:/Documentacion/editar";
    }
    
    @GetMapping("/documentacion/reactivar/{id}")
    public String reactivarDocumento(@PathVariable("id") String id, Authentication authentication) {
        try {
            String rolUsuario = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            if (!"DIRECTOR".equals(rolUsuario) && !"SUPERVISOR".equals(rolUsuario)) {
                return "Error/403";
            }
            
            Documentacion doc = repo.findById(id).orElse(null);
            if (doc != null && "INACTIVO".equals(doc.getEstado())) {
                doc.setEstado("ACTIVO");
                repo.save(doc);
                log.info("Documento reactivado: {}", id);
            }
        } catch (Exception e) {
            log.error("Error al reactivar documento: {}", id, e);
        }
        
        return "redirect:/Documentacion/editar";
    }

    @Deprecated
    private String obtenerRolUsuarioActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "INVITADO";
        }
        
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByUsuario(username)
                .or(() -> usuarioRepository.findByCorreo(username))
                .orElse(null);
            
            if (usuario != null && usuario.getRol() != null) {
                return usuario.getRol().replace("ROLE_", "");
            }
        } catch (Exception e) {
            log.error("Error al obtener rol del usuario", e);
        }
        
        return "USUARIO"; 
    }
    
    private String obtenerNombreUsuarioActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Sistema";
        }
        
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByUsuario(username)
                .or(() -> usuarioRepository.findByCorreo(username))
                .orElse(null);
            
            if (usuario != null && usuario.getNombre_completo() != null) {
                return usuario.getNombre_completo();
            }
        } catch (Exception e) {
            log.error("Error al obtener nombre del usuario", e);
        }
        
        return authentication.getName();
    }

    @GetMapping("/documentacion/carga")
    public String mostrarFormularioCarga(Model model) {
        return "Documentacion/documentacion";
    }

    @PostMapping("/documentacion/carga")
    public String cargarDocumentos(@RequestParam("archivo") MultipartFile archivo, Model model) {
        try {
            if (archivo.isEmpty()) {
                model.addAttribute("mensaje", "Por favor selecciona un archivo CSV válido.");
                model.addAttribute("tipoMensaje", "error");
                return "Documentacion/documentacion";
            }

            CargarDocumentacionCommand comando = new CargarDocumentacionCommand(archivo, repo);
            comando.execute();

            model.addAttribute("mensaje", "Archivo de documentos procesado correctamente!");
            model.addAttribute("tipoMensaje", "success");
            model.addAttribute("documentacion", new DocumentacionDTO());
            
            log.info("Archivo de documentación cargado exitosamente");
        } catch (Exception e) {
            log.error("Error al cargar archivo de documentación", e);
            model.addAttribute("mensaje", "Error al procesar el archivo");
            model.addAttribute("tipoMensaje", "error");
        }

        return "Documentacion/documentacion";
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO <-> ENTIDAD ====================

    private Documentacion convertirDTOaDocumentacion(DocumentacionDTO dto) {
        Documentacion doc = new Documentacion();
        doc.setId(dto.getId());
        doc.setNombre(dto.getNombre());
        doc.setNombreProyecto(dto.getNombreProyecto());
        doc.setTipo(dto.getTipo());
        doc.setDescripcion(dto.getDescripcion());
        doc.setFechaCreacion(dto.getFechaCreacion());
        doc.setEstado(dto.getEstado());
        doc.setPropietario(dto.getPropietario());
        return doc;
    }

    private DocumentacionDTO convertirDocumentacionaDTO(Documentacion doc) {
        DocumentacionDTO dto = new DocumentacionDTO();
        dto.setId(doc.getId());
        dto.setNombre(doc.getNombre());
        dto.setNombreProyecto(doc.getNombreProyecto());
        dto.setTipo(doc.getTipo());
        dto.setDescripcion(doc.getDescripcion());
        dto.setFechaCreacion(doc.getFechaCreacion());
        dto.setEstado(doc.getEstado());
        dto.setPropietario(doc.getPropietario());
        return dto;
    }
}