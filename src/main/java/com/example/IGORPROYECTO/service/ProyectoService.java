package com.example.IGORPROYECTO.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Proyecto;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;
import com.example.IGORPROYECTO.repository.ProyectoRepository;

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
        return proyectoRepository.findAll();
    }

    public Proyecto nuevo(Proyecto proyecto) {
        proyecto.setFechaCreacion(new Date());
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> consultarTodos() {
        return proyectoRepository.findAll();
    }

    public Proyecto buscarPorId(String id) {
        return proyectoRepository.findById(id).orElse(null);
    }

    public Proyecto actualizar(String id, Proyecto proyectoActualizado) {
        return proyectoRepository.findById(id).map(proyecto -> {
            proyecto.setNombre(proyectoActualizado.getNombre());
            proyecto.setDescripcion(proyectoActualizado.getDescripcion());
            proyecto.setEstado(proyectoActualizado.getEstado());
            proyecto.setPrograma(proyectoActualizado.getPrograma());
            proyecto.setFechaInicio(proyectoActualizado.getFechaInicio());
            proyecto.setFechaFinal(proyectoActualizado.getFechaFinal());
            proyecto.setResponsable(proyectoActualizado.getResponsable());
            proyecto.setCliente(proyectoActualizado.getCliente());
            proyecto.setSerial(proyectoActualizado.getSerial());
            return proyectoRepository.save(proyecto);
        }).orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
    }

    public void eliminar(String id) {
        proyectoRepository.deleteById(id);
    }

    public List<Documentacion> obtenerDocumentacionRelacionada(String idProyecto) {
        Proyecto proyecto = buscarPorId(idProyecto);
        if (proyecto == null) {
            throw new RuntimeException("Proyecto no encontrado con id: " + idProyecto);
        }
        return documentacionRepository.findByNombreProyecto(proyecto.getNombre());
    }
}