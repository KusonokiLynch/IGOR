package com.example.IGORPROYECTO.controller;


import com.example.IGORPROYECTO.model.EventoCronograma;
import com.example.IGORPROYECTO.model.EventoCronograma.TipoEvento;
import com.example.IGORPROYECTO.service.CronogramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cronograma")
@CrossOrigin(origins = "*") // Ajusta según tu configuración de CORS
public class CronogramaController {
    
    @Autowired
    private CronogramaService cronogramaService;
    
    // Crear un nuevo evento (reunión, hito, etc.)
    @PostMapping
    public ResponseEntity<EventoCronograma> crearEvento(@RequestBody EventoCronograma evento) {
        try {
            EventoCronograma nuevoEvento = cronogramaService.crearEvento(evento);
            return new ResponseEntity<>(nuevoEvento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener cronograma completo de un proyecto
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<EventoCronograma>> obtenerCronograma(@PathVariable String proyectoId) {
        try {
            List<EventoCronograma> eventos = cronogramaService.obtenerCronogramaProyecto(proyectoId);
            if (eventos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Buscar eventos por nombre de proyecto
    @GetMapping("/buscar")
    public ResponseEntity<List<EventoCronograma>> buscarPorNombre(@RequestParam String nombreProyecto) {
        try {
            List<EventoCronograma> eventos = cronogramaService.buscarPorNombreProyecto(nombreProyecto);
            if (eventos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener eventos filtrados por tipo (REUNION, DOCUMENTO, HITO, etc.)
    @GetMapping("/proyecto/{proyectoId}/tipo/{tipo}")
    public ResponseEntity<List<EventoCronograma>> obtenerPorTipo(
        @PathVariable String proyectoId,
        @PathVariable String tipo
    ) {
        try {
            TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
            List<EventoCronograma> eventos = cronogramaService.obtenerEventosPorTipo(proyectoId, tipoEvento);
            if (eventos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener eventos del mes específico
    @GetMapping("/proyecto/{proyectoId}/mes")
    public ResponseEntity<List<EventoCronograma>> obtenerEventosDelMes(
        @PathVariable String proyectoId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        try {
            List<EventoCronograma> eventos = cronogramaService.obtenerEventosDelMes(proyectoId, year, month);
            if (eventos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener eventos en un rango de fechas
    @GetMapping("/proyecto/{proyectoId}/rango")
    public ResponseEntity<List<EventoCronograma>> obtenerEventosEnRango(
        @PathVariable String proyectoId,
        @RequestParam String fechaInicio,
        @RequestParam String fechaFin
    ) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            
            List<EventoCronograma> eventos = cronogramaService.obtenerEventosEnRango(proyectoId, inicio, fin);
            if (eventos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Actualizar un evento existente
    @PutMapping("/{eventoId}")
    public ResponseEntity<EventoCronograma> actualizarEvento(
        @PathVariable String eventoId,
        @RequestBody EventoCronograma evento
    ) {
        try {
            EventoCronograma eventoActualizado = cronogramaService.actualizarEvento(eventoId, evento);
            if (eventoActualizado != null) {
                return new ResponseEntity<>(eventoActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Eliminar un evento
    @DeleteMapping("/{eventoId}")
    public ResponseEntity<HttpStatus> eliminarEvento(@PathVariable String eventoId) {
        try {
            boolean eliminado = cronogramaService.eliminarEvento(eventoId);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Contar eventos de un proyecto
    @GetMapping("/proyecto/{proyectoId}/count")
    public ResponseEntity<Long> contarEventos(@PathVariable String proyectoId) {
        try {
            Long count = cronogramaService.contarEventos(proyectoId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}