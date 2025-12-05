package com.example.IGORPROYECTO.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;  // 👈 necesario para obtener el usuario logueado

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.service.UsuarioService;

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
        String username = authentication.getName(); // usuario logueado
        Usuario usuario = usuarioService.buscarPorUsuario(username);
        model.addAttribute("usuario", usuario);
        return "usuarios"; // usuarios.html
    }

    
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios";
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String verUsuario(@PathVariable String id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "detalleUsuario"; 
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
