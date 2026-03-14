package com.example.IGORPROYECTO.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Proyecto;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;
import com.example.IGORPROYECTO.repository.ProyectoRepository;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private DocumentacionRepository documentacionRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyectoTest;

    @BeforeEach
    public void setUp() {
        proyectoTest = new Proyecto();
        proyectoTest.setId("1");
        proyectoTest.setNombre("Proyecto IGOR");
        proyectoTest.setDescripcion("Sistema de gestión de recursos");
        proyectoTest.setEstado("ACTIVO");
        proyectoTest.setPrograma("Programa A");
        proyectoTest.setResponsable("Juan Pérez");
        proyectoTest.setFechaCreacion(new Date());
    }

    @Test
    public void testCrearProyectoExitosamente() {
        // ARRANGE
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyectoTest);

        // ACT
        Proyecto proyectoCreado = proyectoService.nuevo(proyectoTest);

        // ASSERT
        assertNotNull(proyectoCreado);
        assertEquals("Proyecto IGOR", proyectoCreado.getNombre());
        assertEquals("ACTIVO", proyectoCreado.getEstado());
        assertNotNull(proyectoCreado.getFechaCreacion());
        verify(proyectoRepository, times(1)).save(any(Proyecto.class));
    }

    @Test
    public void testObtenerTodosLosProyectos() {
        // ARRANGE
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId("2");
        proyecto2.setNombre("Proyecto 2");
        
        List<Proyecto> proyectos = Arrays.asList(proyectoTest, proyecto2);
        when(proyectoRepository.findAll()).thenReturn(proyectos);

        // ACT
        List<Proyecto> proyectosObtenidos = proyectoService.consultarTodos();

        // ASSERT
        assertNotNull(proyectosObtenidos);
        assertEquals(2, proyectosObtenidos.size());
        verify(proyectoRepository, times(1)).findAll();
    }
}