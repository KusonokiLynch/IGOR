package com.example.IGORPROYECTO.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.IGORPROYECTO.Command.CargarUsuarioCommand;
import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.dto.UsuarioDTO;
import com.example.IGORPROYECTO.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/miPerfil")
    public String verUsuarioLogueado(Model model, Authentication authentication) {
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioService.buscarPorUsuario(username);
            model.addAttribute("usuario", usuario);
            log.info("Perfil del usuario consultado: {}", username);
        } catch (Exception e) {
            log.error("Error al obtener perfil del usuario", e);
        }
        return "usuarios";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        try {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            log.info("Lista de usuarios consultada");
        } catch (Exception e) {
            log.error("Error al listar usuarios", e);
        }
        return "usuarios";
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registro";
    }

    // ✅ FIX: Recibir DTO en lugar de entidad JPA
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = convertirDTOaUsuario(usuarioDTO);
            usuarioService.guardar(usuario);
            log.info("Usuario guardado exitosamente: {}", usuario.getUsuario());
        } catch (Exception e) {
            log.error("Error al guardar usuario", e);
        }
        return "redirect:/clientes";
    }

    @GetMapping("/usuarios/{id}")
    public String verUsuario(@PathVariable String id, Model model) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuario);
            log.info("Usuario consultado: {}", id);
        } catch (Exception e) {
            log.error("Error al obtener usuario: {}", id, e);
        }
        return "detalleUsuario"; 
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        try {
            usuarioService.eliminar(id);
            log.info("Usuario eliminado exitosamente: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", id, e);
        }
        return "redirect:/clientes";
    }

    @GetMapping("/usuarios/carga")
    public String mostrarFormularioCarga(Model model) {
        return "cargaUsuarios";     
    }

    @PostMapping("/usuarios/carga")
    public String cargarUsuarios(@RequestParam("archivo") MultipartFile archivo, Model model) {
        try {
            CargarUsuarioCommand comando = new CargarUsuarioCommand(archivo, usuarioService);
            comando.execute();
            model.addAttribute("mensaje", "Archivo de usuarios procesado correctamente!");
            model.addAttribute("usuario", new UsuarioDTO());
            log.info("Archivo de usuarios cargado exitosamente");
        } catch (Exception e) {
            log.error("Error al cargar archivo de usuarios", e);
            model.addAttribute("mensaje", "Error al procesar el archivo");
        }
        return "registro"; 
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO <-> ENTIDAD ====================

    private Usuario convertirDTOaUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre_completo(dto.getNombre_completo());
        usuario.setUsuario(dto.getUsuario());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(dto.getRol());
        usuario.setCorreo(dto.getCorreo());
        usuario.setNo_documento(dto.getNo_documento());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFecha_registro(dto.getFecha_registro());
        return usuario;
    }

    private UsuarioDTO convertirUsuarioaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre_completo(usuario.getNombre_completo());
        dto.setUsuario(usuario.getUsuario());
        dto.setContrasena(usuario.getContrasena());
        dto.setRol(usuario.getRol());
        dto.setCorreo(usuario.getCorreo());
        dto.setNo_documento(usuario.getNo_documento());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setFecha_registro(usuario.getFecha_registro());
        return dto;
    }
}