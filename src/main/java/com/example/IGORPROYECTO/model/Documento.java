package com.example.IGORPROYECTO.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documentos")
public class Documento {
    
    @Id
    private String id;
    
    private String nombre; // Nombre del archivo (ej: "Requerimientos.pdf")
    
    private String proyectoId; // ID del proyecto al que pertenece
    
    private String nombreProyecto; // Nombre del proyecto (para búsquedas fáciles)
    
    private String tipo; // Tipo de documento (PDF, DOCX, XLSX, etc.)
    
    private String url; // URL donde está almacenado (si usas un servicio de storage)
    
    private String rutaArchivo; // Ruta física del archivo
    
    private Long tamanio; // Tamaño en bytes
    
    private String usuarioId; // ID del usuario que subió el documento
    
    private String nombreUsuario; // Nombre del usuario (para mostrar)
    
    private String descripcion; // Descripción opcional del documento
    
    private LocalDateTime fechaSubida; // Fecha y hora de subida
    
    private LocalDateTime fechaModificacion; // Última modificación
    
    private String version; // Versión del documento (ej: "1.0", "2.1")
    
    private String estado; // Estado: "ACTIVO", "ARCHIVADO", "ELIMINADO"
    
    private String categoria; // Categoría: "REQUISITOS", "DISEÑO", "PRUEBAS", etc.
}