package com.example.IGORPROYECTO.Command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.IGORPROYECTO.model.Usuario;
import com.example.IGORPROYECTO.service.UsuarioService;

public class CargarUsuarioCommand implements Command {

    private final MultipartFile archivo;
    private final UsuarioService usuarioService;

    public CargarUsuarioCommand(MultipartFile archivo, UsuarioService usuarioService) {
        this.archivo = archivo;
        this.usuarioService = usuarioService;
    }

    @Override
    public void execute() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {

            String linea;
            List<Usuario> usuarios = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            boolean primeraLinea = true; // ← bandera para saltar encabezado

            while ((linea = br.readLine()) != null) {

                // Saltar encabezado (header)
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] campos = linea.split(",");

                if (campos.length < 9) continue;

                Usuario usuario = new Usuario();
                usuario.setNombre_completo(campos[0]);
                usuario.setUsuario(campos[1]);
                usuario.setContrasena(campos[2]);
                usuario.setRol(campos[3]);
                usuario.setCorreo(campos[4]);
                usuario.setNo_documento(campos[5]);
                usuario.setTelefono(campos[6]);
                usuario.setDireccion(campos[7]);

                // Fecha
                if (!campos[8].isEmpty()) {
                    usuario.setFecha_registro(sdf.parse(campos[8]));
                } else {
                    usuario.setFecha_registro(new Date());
                }


                usuarios.add(usuario);
            }

            usuarioService.guardarTodos(usuarios);
            System.out.println("Carga masiva completada: " + usuarios.size() + " usuarios guardados.");

        } catch (Exception e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
