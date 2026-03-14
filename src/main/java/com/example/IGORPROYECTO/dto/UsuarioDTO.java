package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String id;
    private String nombre_completo;
    private String usuario;
    private String contrasena;
    private String rol;
    private String correo;
    private String no_documento;
    private String telefono;
    private String direccion;
    private Date fecha_registro;
}