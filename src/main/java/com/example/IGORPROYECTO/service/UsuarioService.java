package com.example.IGORPROYECTO.service;

import java.util.List;

import com.example.IGORPROYECTO.model.Usuario;

public interface UsuarioService {

    List<Usuario> listarTodos();
    Usuario guardar(Usuario usuario);
    Usuario buscarPorId(String id);
    void eliminar(String id);
    Usuario buscarPorUsuario(String usuario);

}

