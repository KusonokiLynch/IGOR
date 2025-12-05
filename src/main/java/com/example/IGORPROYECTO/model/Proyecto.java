package com.example.IGORPROYECTO.model;
import java.util.Date;
import lombok.Data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;



@Data
@Document(collection = "proyectos")
public class Proyecto {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private String estado;
    private String programa;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String fechaFinal;
    private String responsable;
    private String cliente;
    private String serial;
    private Date fechaCreacion;
}