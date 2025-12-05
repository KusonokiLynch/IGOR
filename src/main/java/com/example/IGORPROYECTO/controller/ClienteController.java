package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.service.UsuarioService;

@Controller
public class ClienteController {

    private final UsuarioService usuarioService;

    public ClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Vista principal del módulo clientes
    @GetMapping("/clientes")
    public String clientes() {
        return "Clientes/clientes"; // clientes.html
    }

    // Vista con la lista de usuarios
   @GetMapping("/Clientes/ListaClientes")
    public String listaClientes(Model model) {
        model.addAttribute("clientes", usuarioService.listarTodos());
        return "Clientes/ListaClientes"; // ListaClientes.html
    }

    // Vista detalle de un cliente
    @GetMapping("/cliente/{id}")
    public String verCliente(@PathVariable String id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "Clientes/EditarCliente"; 
    }

    @GetMapping("/Documentacion/ListaDocumentos")
    public String listaDocumentos(Model model) {
        model.addAttribute("clientes", usuarioService.listarTodos());
        return "Documentacion/ListaDocumentos"; // ListaClientes.html
    }

    @GetMapping("/Documentacion/NuevoDocumento")
    public String nuevoDocumento(Model model) {
        model.addAttribute("clientes", usuarioService.listarTodos());
        return "Documentacion/NuevoDocumento"; // ListaClientes.html
    }
}
