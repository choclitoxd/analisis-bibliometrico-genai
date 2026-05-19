package com.bibliometria.util;

import com.bibliometria.model.*;
import java.io.*;
import java.util.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad para la exportación de resultados a archivos.
 */
@Component
public class FileExportUtil {

    private static final Logger log = LoggerFactory.getLogger(FileExportUtil.class);

    public void guardarResultados(List<ScientificArticle> articulos, String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new File(nombreArchivo))) {
            writer.println("ID,Titulo,Autores,Fuente");
            for (ScientificArticle a : articulos) {
                writer.println(String.format("%s,\"%s\",\"%s\",%s", 
                    a.getId(), a.getTitle(), String.join("; ", a.getAuthors()), a.getSource()));
            }
            log.info("Archivo exportado exitosamente: {}", nombreArchivo);
        } catch (IOException e) {
            log.error("Error crítico al exportar archivo {}: {}", nombreArchivo, e.getMessage());
        }
    }
}
