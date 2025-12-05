package com.example.IGORPROYECTO.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.IGORPROYECTO.model.Usuario;

public class CustomUserDetails implements UserDetails {

    private final Usuario user;

    public CustomUserDetails(Usuario user) {
        this.user = user;
    }

    // IMPORTANT: return the email so authentication.getName() == correo
    @Override
    public String getUsername() {
        return user.getCorreo();
    }

    @Override
    public String getPassword() {
        return user.getContrasena();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRol() == null ? 
            java.util.Collections.emptyList() :
            java.util.List.of(new SimpleGrantedAuthority("ROLE_" + user.getRol()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // accesor para usar en tu app si necesitas
    public Usuario getUsuario() { return user; }
}
