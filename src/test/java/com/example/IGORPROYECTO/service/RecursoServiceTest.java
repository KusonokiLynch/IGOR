package com.example.IGORPROYECTO.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.IGORPROYECTO.model.Recurso;
import com.example.IGORPROYECTO.repository.RecursoRepository;

@ExtendWith(MockitoExtension.class)
public class RecursoServiceTest {

    @Mock
    private RecursoRepository recursoRepository;

    @InjectMocks
    private RecursoService recursoService;

    private Recurso recursoTest;

    @BeforeEach
    public void setUp() {
        recursoTest = new Recurso();
        recursoTest.setId("1");
        recursoTest.setNombre("Servidor Principal");
        recursoTest.setTipo("Infraestructura");
        recursoTest.setCantidad(5);
        recursoTest.setDisponibilidad(true);
    }

    @Test
    public void testCrearRecursoExitosamente() {
        // ARRANGE
        when(recursoRepository.save(recursoTest)).thenReturn(recursoTest);

        // ACT
        Recurso recursoCreado = recursoService.guardar(recursoTest);

        // ASSERT
        assertNotNull(recursoCreado);
        assertEquals("Servidor Principal", recursoCreado.getNombre());
        assertEquals("Infraestructura", recursoCreado.getTipo());
        assertEquals(5, recursoCreado.getCantidad());
        assertTrue(recursoCreado.isDisponibilidad());
        verify(recursoRepository, times(1)).save(recursoTest);
    }

    @Test
    public void testVerificarRecursoDisponible() {
        // ARRANGE
        when(recursoRepository.findById("1")).thenReturn(Optional.of(recursoTest));

        // ACT
        Optional<Recurso> recursoObtenido = recursoRepository.findById("1");

        // ASSERT
        assertTrue(recursoObtenido.isPresent());
        assertTrue(recursoObtenido.get().isDisponibilidad());
        assertEquals(5, recursoObtenido.get().getCantidad());
    }
}