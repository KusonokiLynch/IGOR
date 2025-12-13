package com.example.IGORPROYECTO.service;

import com.example.IGORPROYECTO.model.Documento;
import com.example.IGORPROYECTO.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentoService {
    
    @Autowired
    private DocumentoRepository documentoRepository;
    
    @Autowired
    private CronogramaService cronogramaService;
    
    // Directorio donde se guardarán los archivos
    private final String UPLOAD_DIR = "uploads/documentos/";
    
    // Subir un documento
    public Documento subirDocumento(
        MultipartFile archivo,
        String proyectoId,
        String nombreProyecto,
        String usuarioId,
        String nombreUsuario,
        String descripcion,
        String categoria
    ) throws IOException {
        
        // Crear directorio si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generar nombre único para el archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;
        
        // Guardar archivo en disco
        Path rutaArchivo = uploadPath.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        
        // Crear objeto Documento
        Documento documento = new Documento();
        documento.setNombre(nombreOriginal);
        documento.setProyectoId(proyectoId);
        documento.setNombreProyecto(nombreProyecto);
        documento.setTipo(extension.replace(".", "").toUpperCase());
        documento.setRutaArchivo(rutaArchivo.toString());
        documento.setTamanio(archivo.getSize());
        documento.setUsuarioId(usuarioId);
        documento.setNombreUsuario(nombreUsuario);
        documento.setDescripcion(descripcion);
        documento.setCategoria(categoria);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setEstado("ACTIVO");
        documento.setVersion("1.0");
        
        // Guardar en MongoDB
        Documento documentoGuardado = documentoRepository.save(documento);
        
        // Registrar en el cronograma
        cronogramaService.registrarSubidaDocumento(
            proyectoId,
            nombreProyecto,
            nombreOriginal,
            usuarioId
        );
        
        return documentoGuardado;
    }
    
    // Obtener documentos por proyecto
    public List<Documento> obtenerDocumentosPorProyecto(String proyectoId) {
        return documentoRepository.findByProyectoIdAndEstadoOrderByFechaSubidaDesc(proyectoId, "ACTIVO");
    }
    
    // Obtener documento por ID
    public Optional<Documento> obtenerDocumentoPorId(String id) {
        return documentoRepository.findById(id);
    }
    
    // Buscar por tipo
    public List<Documento> obtenerPorTipo(String proyectoId, String tipo) {
        return documentoRepository.findByProyectoIdAndTipo(proyectoId, tipo);
    }
    
    // Buscar por categoría
    public List<Documento> obtenerPorCategoria(String proyectoId, String categoria) {
        return documentoRepository.findByProyectoIdAndCategoria(proyectoId, categoria);
    }
    
    // Eliminar documento (soft delete)
    public boolean eliminarDocumento(String id) {
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()) {
            Documento doc = documento.get();
            doc.setEstado("ELIMINADO");
            documentoRepository.save(doc);
            return true;
        }
        return false;
    }
    
    // Eliminar físicamente
    public boolean eliminarDocumentoFisico(String id) throws IOException {
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()) {
            Documento doc = documento.get();
            
            // Eliminar archivo del disco
            Path rutaArchivo = Paths.get(doc.getRutaArchivo());
            if (Files.exists(rutaArchivo)) {
                Files.delete(rutaArchivo);
            }
            
            // Eliminar de MongoDB
            documentoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Actualizar documento
    public Documento actualizarDocumento(String id, String descripcion, String categoria) {
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()) {
            Documento doc = documento.get();
            doc.setDescripcion(descripcion);
            doc.setCategoria(categoria);
            doc.setFechaModificacion(LocalDateTime.now());
            return documentoRepository.save(doc);
        }
        return null;
    }
    
    // Contar documentos de un proyecto
    public Long contarDocumentos(String proyectoId) {
        return documentoRepository.countByProyectoId(proyectoId);
    }
    
    // Listar todos los documentos (útil para testing)
    public List<Documento> obtenerTodos() {
        return documentoRepository.findAll();
    }
}