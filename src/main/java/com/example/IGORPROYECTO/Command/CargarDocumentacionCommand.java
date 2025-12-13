package com.example.IGORPROYECTO.Command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;

public class CargarDocumentacionCommand implements Command {

    private final MultipartFile archivo;
    private final DocumentacionRepository documentacionRepository;

    public CargarDocumentacionCommand(MultipartFile archivo, DocumentacionRepository documentacionRepository) {
        this.archivo = archivo;
        this.documentacionRepository = documentacionRepository;
    }

    @Override
    public void execute() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {

            String linea;
            List<Documentacion> documentos = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // saltar encabezado
                }

                // Separar campos
                String[] campos = linea.split(",", -1); // -1 para no perder campos vacíos

                // Crear objeto
                Documentacion doc = new Documentacion();
                doc.setNombre(campos.length > 0 ? campos[0].trim() : "");
                doc.setNombreProyecto(campos.length > 1 ? campos[1].trim() : "");
                doc.setTipo(campos.length > 2 ? campos[2].trim() : "");
                doc.setDescripcion(campos.length > 3 ? campos[3].trim() : "");

                // FechaCreacion
                if (campos.length > 4 && !campos[4].trim().isEmpty()) {
                    try {
                        doc.setFechaCreacion(sdf.parse(campos[4].trim()));
                    } catch (Exception e) {
                        doc.setFechaCreacion(new Date());
                    }
                } else {
                    doc.setFechaCreacion(new Date());
                }

                // Estado
                doc.setEstado(campos.length > 5 && !campos[5].trim().isEmpty() ? campos[5].trim() : "ACTIVO");

                // Propietario
                doc.setPropietario(campos.length > 6 && !campos[6].trim().isEmpty() ? campos[6].trim() : "Sistema");

                documentos.add(doc);
            }

            documentacionRepository.saveAll(documentos);
            System.out.println("Carga masiva completada: " + documentos.size() + " documentos guardados.");

        } catch (Exception e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
