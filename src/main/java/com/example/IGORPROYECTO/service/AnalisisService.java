package com.example.IGORPROYECTO.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Kpis;
import com.example.IGORPROYECTO.model.Peticion;
import com.example.IGORPROYECTO.repository.KpisRepository;
import com.example.IGORPROYECTO.repository.PeticionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalisisService {

    private final PeticionRepository peticionRepository;
    private final KpisRepository kpisRepository;

    // ==================== PETICIONES ====================
    
    /**
     * Crear una nueva petición
     */
    public Peticion crearPeticion(Peticion peticion) {
        peticion.setFechaSolicitud(new Date());
        peticion.setFechaActualizacion(new Date());
        if (peticion.getEstado() == null) {
            peticion.setEstado("Pendiente");
        }
        if (peticion.getProgreso() == null) {
            peticion.setProgreso(0);
        }
        return peticionRepository.save(peticion);
    }

    /**
     * Obtener todas las peticiones
     */
    public List<Peticion> obtenerTodasPeticiones() {
        return peticionRepository.findAllByOrderByFechaSolicitudDesc();
    }

    /**
     * Obtener petición por ID
     */
    public Optional<Peticion> obtenerPeticionPorId(String id) {
        return peticionRepository.findById(id);
    }

    /**
     * Obtener peticiones por estado
     */
    public List<Peticion> obtenerPeticionesPorEstado(String estado) {
        return peticionRepository.findByEstado(estado);
    }

    /**
     * Obtener peticiones por solicitante
     */
    public List<Peticion> obtenerPeticionesPorSolicitante(String solicitante) {
        return peticionRepository.findBySolicitante(solicitante);
    }

    /**
     * Actualizar estado de petición
     */
    public Peticion actualizarEstadoPeticion(String id, String nuevoEstado, Integer progreso) {
        Optional<Peticion> peticionOpt = peticionRepository.findById(id);
        if (peticionOpt.isPresent()) {
            Peticion peticion = peticionOpt.get();
            peticion.setEstado(nuevoEstado);
            if (progreso != null) {
                peticion.setProgreso(progreso);
            }
            peticion.setFechaActualizacion(new Date());
            return peticionRepository.save(peticion);
        }
        return null;
    }

    /**
     * Actualizar petición completa
     */
    public Peticion actualizarPeticion(Peticion peticion) {
        peticion.setFechaActualizacion(new Date());
        return peticionRepository.save(peticion);
    }

    /**
     * Eliminar petición
     */
    public void eliminarPeticion(String id) {
        peticionRepository.deleteById(id);
    }

    /**
     * Contar peticiones por estado
     */
    public long contarPeticionesPorEstado(String estado) {
        return peticionRepository.findByEstado(estado).size();
    }

    // ==================== KPIs ====================

    /**
     * Crear un nuevo KPI
     */
    public Kpis crearKPI(Kpis kpi) {
        kpi.setFechaCreacion(new Date());
        if (kpi.getEstado() == null) {
            kpi.setEstado("Activo");
        }
        return kpisRepository.save(kpi);
    }

    /**
     * Obtener todos los KPIs
     */
    public List<Kpis> obtenerTodosKPIs() {
        return kpisRepository.findAllByOrderByFechaCreacionDesc();
    }

    /**
     * Obtener KPI por ID
     */
    public Optional<Kpis> obtenerKPIPorId(String id) {
        return kpisRepository.findById(id);
    }

    /**
     * Obtener KPIs por proyecto
     */
    public List<Kpis> obtenerKPIsPorProyecto(String nombreProyecto) {
        return kpisRepository.findByNombreProyecto(nombreProyecto);
    }

    /**
     * Obtener KPIs por tipo
     */
    public List<Kpis> obtenerKPIsPorTipo(String tipo) {
        return kpisRepository.findByTipo(tipo);
    }

    /**
     * Obtener KPIs por propietario
     */
    public List<Kpis> obtenerKPIsPorPropietario(String propietario) {
        return kpisRepository.findByPropietario(propietario);
    }

    /**
     * Actualizar KPI
     */
    public Kpis actualizarKPI(Kpis kpi) {
        return kpisRepository.save(kpi);
    }

    /**
     * Eliminar KPI
     */
    public void eliminarKPI(String id) {
        kpisRepository.deleteById(id);
    }

    /**
     * Contar KPIs por estado
     */
    public long contarKPIsPorEstado(String estado) {
        return kpisRepository.findByEstado(estado).size();
    }

    // ==================== REPORTES Y ESTADÍSTICAS ====================

    /**
     * Obtener estadísticas generales
     */
    public EstadisticasDTO obtenerEstadisticas() {
        EstadisticasDTO stats = new EstadisticasDTO();
        
        // Estadísticas de peticiones
        stats.setTotalPeticiones(peticionRepository.count());
        stats.setPeticionesPendientes(contarPeticionesPorEstado("Pendiente"));
        stats.setPeticionesEnProceso(contarPeticionesPorEstado("En Proceso"));
        stats.setPeticionesCompletadas(contarPeticionesPorEstado("Completado"));
        
        // Estadísticas de KPIs
        stats.setTotalKPIs(kpisRepository.count());
        stats.setKpisActivos(contarKPIsPorEstado("Activo"));
        
        return stats;
    }

    // Clase interna para estadísticas
    @lombok.Data
    public static class EstadisticasDTO {
        private long totalPeticiones;
        private long peticionesPendientes;
        private long peticionesEnProceso;
        private long peticionesCompletadas;
        private long totalKPIs;
        private long kpisActivos;
    }
}