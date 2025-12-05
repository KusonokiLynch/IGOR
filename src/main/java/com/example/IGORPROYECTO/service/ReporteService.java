package com.example.IGORPROYECTO.service;

import com.example.IGORPROYECTO.model.Proyecto;
import com.example.IGORPROYECTO.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ProyectoRepository proyectoRepository;

   public Map<String, Long> obtenerProyectosPorEstado() {
    return proyectoRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                p -> Optional.ofNullable(p.getEstado()).orElse("SIN ESTADO"),
                Collectors.counting()
            ));
}

public Map<String, Long> obtenerProyectosPorResponsable() {
    return proyectoRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                p -> Optional.ofNullable(p.getResponsable()).orElse("SIN RESPONSABLE"),
                Collectors.counting()
            ));
}

    public Map<String, Long> obtenerProyectosPorPrograma() {
    return proyectoRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                p -> Optional.ofNullable(p.getPrograma()).orElse("SIN PROGRAMA"),
                Collectors.counting()
            ));
}


    // Estadísticas por cliente
    public Map<String, Long> obtenerProyectosPorCliente() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        return proyectos.stream()
                .collect(Collectors.groupingBy(
                    Proyecto::getCliente,
                    Collectors.counting()
                ));
    }

    // Resumen general
    public Map<String, Object> obtenerResumenGeneral() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total", proyectos.size());
        resumen.put("porEstado", obtenerProyectosPorEstado());
        resumen.put("porResponsable", obtenerProyectosPorResponsable());
        resumen.put("porPrograma", obtenerProyectosPorPrograma());
        resumen.put("porCliente", obtenerProyectosPorCliente());
        
        return resumen;
    }
}