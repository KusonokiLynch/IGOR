package com.example.IGORPROYECTO.service;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.EventoCronograma;
import com.example.IGORPROYECTO.model.EventoCronograma.TipoEvento;
import com.example.IGORPROYECTO.repository.CronogramaRepository;

@Service
public class CronogramaService {
    
    @Autowired
    private CronogramaRepository cronogramaRepository;
    
    // Crear un nuevo evento en el cronograma
    public EventoCronograma crearEvento(EventoCronograma evento) {
        evento.setFechaCreacion(LocalDateTime.now());
        return cronogramaRepository.save(evento);
    }
    
    // Obtener todos los eventos de un proyecto
    public List<EventoCronograma> obtenerCronogramaProyecto(String proyectoId) {
        return cronogramaRepository.findByProyectoIdOrderByFechaDesc(proyectoId);
    }
    
    // Obtener eventos por nombre de proyecto
    public List<EventoCronograma> buscarPorNombreProyecto(String nombreProyecto) {
        return cronogramaRepository.findByNombreProyectoContainingIgnoreCaseOrderByFechaDesc(nombreProyecto);
    }
    
    // Obtener eventos de un proyecto filtrados por tipo
    public List<EventoCronograma> obtenerEventosPorTipo(String proyectoId, TipoEvento tipo) {
        return cronogramaRepository.findByProyectoIdAndTipoOrderByFechaDesc(proyectoId, tipo);
    }
    
    // Obtener eventos del mes actual de un proyecto
    public List<EventoCronograma> obtenerEventosDelMes(String proyectoId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime inicioMes = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime finMes = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        
        return cronogramaRepository.findByProyectoIdAndFechaBetweenOrderByFechaDesc(
            proyectoId, inicioMes, finMes
        );
    }
    
    // Obtener eventos en un rango de fechas
    public List<EventoCronograma> obtenerEventosEnRango(
        String proyectoId, 
        LocalDateTime fechaInicio, 
        LocalDateTime fechaFin
    ) {
        return cronogramaRepository.findByProyectoIdAndFechaBetweenOrderByFechaDesc(
            proyectoId, fechaInicio, fechaFin
        );
    }
    
    // Eliminar un evento
    public boolean eliminarEvento(String eventoId) {
        Optional<EventoCronograma> evento = cronogramaRepository.findById(eventoId);
        if (evento.isPresent()) {
            cronogramaRepository.deleteById(eventoId);
            return true;
        }
        return false;
    }
    
    // Actualizar un evento
    public EventoCronograma actualizarEvento(String eventoId, EventoCronograma eventoActualizado) {
        Optional<EventoCronograma> eventoExistente = cronogramaRepository.findById(eventoId);
        
        if (eventoExistente.isPresent()) {
            EventoCronograma evento = eventoExistente.get();
            evento.setFecha(eventoActualizado.getFecha());
            evento.setTipo(eventoActualizado.getTipo());
            evento.setDescripcion(eventoActualizado.getDescripcion());
            evento.setDetalles(eventoActualizado.getDetalles());
            return cronogramaRepository.save(evento);
        }
        return null;
    }
    
    // Contar total de eventos de un proyecto
    public Long contarEventos(String proyectoId) {
        return cronogramaRepository.countByProyectoId(proyectoId);
    }
    
    // Método auxiliar para crear evento de documento (llamar desde tu servicio de documentos)
    public EventoCronograma registrarSubidaDocumento(
        String proyectoId, 
        String nombreProyecto, 
        String nombreDocumento,
        String usuarioId
    ) {
        EventoCronograma evento = new EventoCronograma();
        evento.setProyectoId(proyectoId);
        evento.setNombreProyecto(nombreProyecto);
        evento.setFecha(LocalDateTime.now());
        evento.setTipo(TipoEvento.DOCUMENTO);
        evento.setDescripcion("Documento subido: " + nombreDocumento);
        evento.setDetalles("Archivo: " + nombreDocumento);
        evento.setCreadoPor(usuarioId);
        
        return crearEvento(evento);
    }
}