package com.example.IGORPROYECTO.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    public void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setId("1");
        usuarioTest.setNombre_completo("Juan Pérez");
        usuarioTest.setUsuario("juanperez");
        usuarioTest.setCorreo("juan@gmail.com");
        usuarioTest.setContrasena("password123");
        usuarioTest.setRol("TRABAJADOR");
    }

    @Test
    public void testGuardarUsuarioExitosamente() {
        // ARRANGE
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // ACT
        Usuario usuarioGuardado = usuarioService.guardar(usuarioTest);

        // ASSERT
        assertNotNull(usuarioGuardado);
        assertEquals("Juan Pérez", usuarioGuardado.getNombre_completo());
        assertEquals("juan@gmail.com", usuarioGuardado.getCorreo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testBuscarUsuarioPorUsuario() {
        // ARRANGE
        when(usuarioRepository.findByUsuario("juanperez")).thenReturn(Optional.of(usuarioTest));

        // ACT
        Usuario usuarioEncontrado = usuarioService.buscarPorUsuario("juanperez");

        // ASSERT
        assertNotNull(usuarioEncontrado);
        assertEquals("juanperez", usuarioEncontrado.getUsuario());
        assertEquals("Juan Pérez", usuarioEncontrado.getNombre_completo());
        verify(usuarioRepository, times(1)).findByUsuario("juanperez");
    }
}