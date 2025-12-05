package com.example.IGORPROYECTO.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByUsuario(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

    return User.withUsername(usuario.getUsuario())
            .password(usuario.getContrasena())
            .authorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol()) // ✅ Correcto
            ))
            .build();
}
}
