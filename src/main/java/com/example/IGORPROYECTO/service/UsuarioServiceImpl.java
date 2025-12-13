package com.example.IGORPROYECTO.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
//Normal//
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // 🔐 Encriptar antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarPorUsuario(String usuario) {
    return usuarioRepository.findByUsuario(usuario).orElse(null);
}

@Override
public void guardarTodos(List<Usuario> usuarios) {
    for (Usuario u : usuarios) {
        u.setContrasena(passwordEncoder.encode(u.getContrasena()));
    }
    usuarioRepository.saveAll(usuarios);
}

}
