package com.example.IGORPROYECTO.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.IGORPROYECTO.model.Documento;
import com.example.IGORPROYECTO.service.DocumentoService;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {
    
    @Autowired
    private DocumentoService documentoService;
    
    // Listar TODOS los documentos (útil para testing)
    @GetMapping
    public ResponseEntity<List<Documento>> listarTodos() {
        try {
            List<Documento> documentos = documentoService.obtenerTodos();
            if (documentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Subir documento
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirDocumento(
        @RequestParam("archivo") MultipartFile archivo,
        @RequestParam("proyectoId") String proyectoId,
        @RequestParam("nombreProyecto") String nombreProyecto,
        @RequestParam("usuarioId") String usuarioId,
        @RequestParam("nombreUsuario") String nombreUsuario,
        @RequestParam(value = "descripcion", required = false) String descripcion,
        @RequestParam(value = "categoria", required = false, defaultValue = "GENERAL") String categoria
    ) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }
            
            Documento documento = documentoService.subirDocumento(
                archivo,
                proyectoId,
                nombreProyecto,
                usuarioId,
                nombreUsuario,
                descripcion,
                categoria
            );
            
            return new ResponseEntity<>(documento, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al subir archivo: " + e.getMessage());
        }
    }
    
    // Obtener documentos de un proyecto
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Documento>> obtenerDocumentos(@PathVariable String proyectoId) {
        try {
            List<Documento> documentos = documentoService.obtenerDocumentosPorProyecto(proyectoId);
            if (documentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener documento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtenerDocumentoPorId(@PathVariable String id) {
        Optional<Documento> documento = documentoService.obtenerDocumentoPorId(id);
        return documento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Descargar documento
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable String id) {
        try {
            Optional<Documento> documentoOpt = documentoService.obtenerDocumentoPorId(id);
            
            if (documentoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Documento documento = documentoOpt.get();
            Path rutaArchivo = Paths.get(documento.getRutaArchivo());
            Resource recurso = new UrlResource(rutaArchivo.toUri());
            
            if (recurso.exists() && recurso.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + documento.getNombre() + "\"")
                    .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Filtrar por tipo
    @GetMapping("/proyecto/{proyectoId}/tipo/{tipo}")
    public ResponseEntity<List<Documento>> obtenerPorTipo(
        @PathVariable String proyectoId,
        @PathVariable String tipo
    ) {
        try {
            List<Documento> documentos = documentoService.obtenerPorTipo(proyectoId, tipo.toUpperCase());
            if (documentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Filtrar por categoría
    @GetMapping("/proyecto/{proyectoId}/categoria/{categoria}")
    public ResponseEntity<List<Documento>> obtenerPorCategoria(
        @PathVariable String proyectoId,
        @PathVariable String categoria
    ) {
        try {
            List<Documento> documentos = documentoService.obtenerPorCategoria(proyectoId, categoria);
            if (documentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Actualizar documento
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizarDocumento(
        @PathVariable String id,
        @RequestParam(required = false) String descripcion,
        @RequestParam(required = false) String categoria
    ) {
        try {
            Documento actualizado = documentoService.actualizarDocumento(id, descripcion, categoria);
            if (actualizado != null) {
                return new ResponseEntity<>(actualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Eliminar documento (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> eliminarDocumento(@PathVariable String id) {
        try {
            boolean eliminado = documentoService.eliminarDocumento(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Eliminar físicamente
    @DeleteMapping("/fisico/{id}")
    public ResponseEntity<HttpStatus> eliminarDocumentoFisico(@PathVariable String id) {
        try {
            boolean eliminado = documentoService.eliminarDocumentoFisico(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Contar documentos
    @GetMapping("/proyecto/{proyectoId}/count")
    public ResponseEntity<Long> contarDocumentos(@PathVariable String proyectoId) {
        try {
            Long count = documentoService.contarDocumentos(proyectoId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}