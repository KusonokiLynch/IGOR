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
import com.example.IGORPROYECTO.repository.DocumentacionRepository; 
import com.example.IGORPROYECTO.repository.UsuarioRepository;

@Controller
public class DocumentacionController {

    @Autowired
    private DocumentacionRepository repo;
    
    @Autowired
    private UsuarioRepository usuarioRepository; 

    // Mostrar el formulario en /Documentacion
    @GetMapping("/documentacion")
    public String mostrarDocumentacion(Model model) {
        model.addAttribute("documentacion", new Documentacion());
        return "Documentacion/Documentacion";
    }
    // Mostrar  formulario nuevo documento mas creacion de este mismo 
    @GetMapping("/NuevoDocumento")
    public String mostrarFormulario(Model model) {
        model.addAttribute("documentacion", new Documentacion());
        return "Documentacion/NuevoDocumento";
    }

    // Guardar el documento en /Documentacion/guardar
    @PostMapping("/Documentacion/guardar")
    public String guardarDocumento(@ModelAttribute Documentacion documentacion, Authentication authentication) {
        documentacion.setFechaCreacion(new Date());
        
        if (documentacion.getEstado() == null || documentacion.getEstado().isEmpty()) {
            documentacion.setEstado("ACTIVO");
        }
        
        if (documentacion.getPropietario() == null || documentacion.getPropietario().isEmpty()) {
            String nombreUsuario = obtenerNombreUsuarioActual(authentication);
            documentacion.setPropietario(nombreUsuario);
        }
        
        repo.save(documentacion);
        return "redirect:/NuevoDocumento";
    }

    
    @GetMapping("/Documentacion/editar")
    public String mostrarHistorial(Model model, Authentication authentication) {
        List<Documentacion> documentacion;
        
        String rolUsuario = obtenerRolUsuarioActual(authentication);
        
        if ("DIRECTOR".equals(rolUsuario) || "SUPERVISOR".equals(rolUsuario)) {
            documentacion = repo.findAll();
        } else {
            
            documentacion = repo.findByEstado("ACTIVO");
        }
        
        model.addAttribute("documentacion", documentacion);
        model.addAttribute("rolActual", rolUsuario);
        model.addAttribute("esDirector", "DIRECTOR".equals(rolUsuario));
        model.addAttribute("esSupervisor", "SUPERVISOR".equals(rolUsuario));
        return "Documentacion/EditarDocumento";
    }

    @PostMapping("/Documentacion/editar")
    public String editarDocumento(@ModelAttribute Documentacion documentacion) {
        
        Documentacion docExistente = repo.findById(documentacion.getId()).orElse(null);
        if (docExistente != null) {
            documentacion.setFechaCreacion(docExistente.getFechaCreacion());
        } else {
            documentacion.setFechaCreacion(new Date());
        }
        repo.save(documentacion);
        return "redirect:/Documentacion/editar";
    }

   
    @GetMapping("/Documentacion/eliminar/{id}")
    public String eliminarDocumento(@PathVariable("id") String id) {
        Documentacion doc = repo.findById(id).orElse(null);
        if (doc != null) {
            
            doc.setEstado("INACTIVO");
            repo.save(doc);
        }
        return "redirect:/Documentacion/editar";
    }
    
    
    @GetMapping("/Documentacion/reactivar/{id}")
    public String reactivarDocumento(@PathVariable("id") String id, Authentication authentication) {
        
        String rolUsuario = obtenerRolUsuarioActual(authentication);
        
        if ("DIRECTOR".equals(rolUsuario)|| "SUPERVISOR".equals(rolUsuario)) {
            Documentacion doc = repo.findById(id).orElse(null);
            if (doc != null && "INACTIVO".equals(doc.getEstado())) {
                doc.setEstado("ACTIVO");
                repo.save(doc);
            }
        }
        
        return "redirect:/Documentacion/editar";
    }

    // Método para obtener el rol del usuario actual desde la base de datos
    private String obtenerRolUsuarioActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "INVITADO";
        }
        
        try {
            // Obtener el nombre de usuario o correo desde la autenticación
            String username = authentication.getName();
            
            
            Usuario usuario = usuarioRepository.findByUsuario(username)
                .or(() -> usuarioRepository.findByCorreo(username))
                .orElse(null);
            
            if (usuario != null && usuario.getRol() != null) {
                return usuario.getRol();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener rol del usuario: " + e.getMessage());
        }
        
        return "USUARIO"; 
    }
    
    // Método auxiliar para obtener el nombre completo del usuario por si no pone propietario 
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
            System.err.println("Error al obtener nombre del usuario: " + e.getMessage());
        }
        
        return authentication.getName();
    }

    // NUEVO: Formulario de carga masiva
    @GetMapping("/documentacion/carga")
    public String mostrarFormularioCarga(Model model) {
        return "Documentacion/Documentacion"; // o una vista separada si quieres
    }

    @PostMapping("/documentacion/carga")
    public String cargarDocumentos(@RequestParam("archivo") MultipartFile archivo, Model model) {
        if (archivo.isEmpty()) {
            model.addAttribute("mensaje", "Por favor selecciona un archivo CSV válido.");
            model.addAttribute("tipoMensaje", "error");
            return "Documentacion/Documentacion";
        }

        // Crear comando
        CargarDocumentacionCommand comando = new CargarDocumentacionCommand(archivo, repo);

        // Ejecutar comando directamente
        comando.execute();

        // Agregar mensaje de éxito
        model.addAttribute("mensaje", "Archivo de documentos procesado correctamente!");
        model.addAttribute("tipoMensaje", "success");

        // Mantener formulario limpio
        model.addAttribute("documentacion", new Documentacion());

        return "Documentacion/Documentacion";
    }   
}