package com.example.IGORPROYECTO.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Proyecto;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;
import com.example.IGORPROYECTO.repository.ProyectoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final DocumentacionRepository documentacionRepository;

    public ProyectoService(ProyectoRepository proyectoRepository,
                           DocumentacionRepository documentacionRepository) {
        this.proyectoRepository = proyectoRepository;
        this.documentacionRepository = documentacionRepository;
    }

    public List<Proyecto> menuTodos() {
        log.info("📋 Obteniendo todos los proyectos para menú");
        return proyectoRepository.findAll();
    }

    public Proyecto nuevo(Proyecto proyecto) {
        log.info("🔄 Creando nuevo proyecto: {}", proyecto.getNombre());
        
        if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
            log.warn("⚠️ Intento de crear proyecto sin nombre");
            throw new IllegalArgumentException("El nombre del proyecto es obligatorio");
        }
        
        proyecto.setFechaCreacion(new Date());
        Proyecto guardado = proyectoRepository.save(proyecto);
        
        log.info("✅ Proyecto creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    public List<Proyecto> consultarTodos() {
        log.info("🔍 Consultando todos los proyectos");
        List<Proyecto> proyectos = proyectoRepository.findAll();
        log.info("📊 Total de proyectos encontrados: {}", proyectos.size());
        return proyectos;
    }

    public Proyecto buscarPorId(String id) {
        log.info("🔍 Buscando proyecto con ID: {}", id);
        Proyecto proyecto = proyectoRepository.findById(id).orElse(null);
        
        if (proyecto != null) {
            log.info("✅ Proyecto encontrado: {}", proyecto.getNombre());
        } else {
            log.warn("⚠️ Proyecto no encontrado con ID: {}", id);
        }
        
        return proyecto;
    }

    public Proyecto actualizar(String id, Proyecto proyectoActualizado) {
        log.info("🔄 Actualizando proyecto con ID: {}", id);
        
        return proyectoRepository.findById(id).map(proyecto -> {
            log.info("📝 Actualizando campos del proyecto: {}", proyecto.getNombre());
            
            proyecto.setNombre(proyectoActualizado.getNombre());
            proyecto.setDescripcion(proyectoActualizado.getDescripcion());
            proyecto.setEstado(proyectoActualizado.getEstado());
            proyecto.setPrograma(proyectoActualizado.getPrograma());
            proyecto.setFechaInicio(proyectoActualizado.getFechaInicio());
            proyecto.setFechaFinal(proyectoActualizado.getFechaFinal());
            proyecto.setResponsable(proyectoActualizado.getResponsable());
            proyecto.setCliente(proyectoActualizado.getCliente());
            proyecto.setSerial(proyectoActualizado.getSerial());
            
            Proyecto actualizado = proyectoRepository.save(proyecto);
            log.info("✅ Proyecto actualizado exitosamente: {}", id);
            return actualizado;
            
        }).orElseThrow(() -> {
            log.error("❌ Error: Proyecto no encontrado con id: {}", id);
            return new RuntimeException("Proyecto no encontrado con id: " + id);
        });
    }

    public void eliminar(String id) {
        log.info("🗑️  Eliminando proyecto con ID: {}", id);
        
        if (!proyectoRepository.existsById(id)) {
            log.error("❌ Intento de eliminar proyecto inexistente: {}", id);
            throw new RuntimeException("Proyecto no encontrado con id: " + id);
        }
        
        proyectoRepository.deleteById(id);
        log.info("✅ Proyecto eliminado exitosamente: {}", id);
    }

    public List<Documentacion> obtenerDocumentacionRelacionada(String idProyecto) {
        log.info("📄 Obteniendo documentación para proyecto: {}", idProyecto);
        
        Proyecto proyecto = buscarPorId(idProyecto);
        if (proyecto == null) {
            log.error("❌ No se puede obtener documentación: Proyecto no encontrado: {}", idProyecto);
            throw new RuntimeException("Proyecto no encontrado con id: " + idProyecto);
        }
        
        List<Documentacion> documentos = documentacionRepository.findByNombreProyecto(proyecto.getNombre());
        log.info("📊 Documentos encontrados para proyecto '{}': {}", proyecto.getNombre(), documentos.size());
        
        return documentos;
    }
}