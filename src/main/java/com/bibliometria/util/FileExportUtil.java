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

    private static final String CARPETA_PERSISTENCIA = "persistencia";

    public void guardarResultados(List<ScientificArticle> articulos, String nombreArchivo) {
        File directorio = new File(CARPETA_PERSISTENCIA);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        File archivo = new File(directorio, nombreArchivo);
        try (PrintWriter writer = new PrintWriter(archivo)) {
            writer.println("ID,Titulo,Autores,Fuente");
            for (ScientificArticle a : articulos) {
                writer.println(String.format("%s,\"%s\",\"%s\",%s", 
                    a.getId(), a.getTitle(), String.join("; ", a.getAuthors()), a.getSource()));
            }
            log.info("Archivo exportado exitosamente en persistencia: {}", archivo.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error crítico al exportar archivo {}: {}", nombreArchivo, e.getMessage());
        }
    }
}
