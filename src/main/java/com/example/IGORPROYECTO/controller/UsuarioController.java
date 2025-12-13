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
        return "redirect:/clientes";
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
        return "redirect:/clientes";
    }

        // NUEVO: Formulario de carga masiva
    @GetMapping("/usuarios/carga")
    public String mostrarFormularioCarga(Model model) {
        return "cargaUsuarios";     
    }

    @PostMapping("/usuarios/carga")
    public String cargarUsuarios(@RequestParam("archivo") MultipartFile archivo, Model model) {
    // Crear comando
    CargarUsuarioCommand comando = new CargarUsuarioCommand(archivo, usuarioService);
    // Ejecutar el comando directamente
    comando.execute();
    // Agregar mensaje de éxito
    model.addAttribute("mensaje", "Archivo de usuarios procesado correctamente!");
    // Retornar la misma plantilla de registro
    model.addAttribute("usuario", new Usuario()); // para mantener el formulario limpio
    return "registro"; 
} 

}
