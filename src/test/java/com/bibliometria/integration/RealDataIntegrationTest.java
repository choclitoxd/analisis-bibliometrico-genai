package com.bibliometria.integration;

import com.bibliometria.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RealDataIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void testFullExtractionFlowWithRealApis() {
        String query = "generative artificial intelligence";
        
        // Ejecutar el proceso real
        Map<String, Object> resultado = articleService.procesarExtraccion(query);
        
        // Verificaciones básicas
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertTrue((int)resultado.get("total_procesados") > 0, "Debería haber recuperado al menos un artículo");
        
        // Verificar que hay datos de análisis (Req 3)
        Map<String, Integer> frecuencias = (Map<String, Integer>) resultado.get("analisis_frecuencias_base");
        assertNotNull(frecuencias, "El análisis de frecuencias no debería ser nulo");
        
        // Verificar descubrimiento de palabras (Req 3)
        Map<String, Integer> nuevasPalabras = (Map<String, Integer>) resultado.get("descubrimiento_nuevas_palabras");
        assertNotNull(nuevasPalabras, "El descubrimiento de palabras no debería ser nulo");

        System.out.println("TEST EXITOSO: Se recuperaron " + resultado.get("total_procesados") + " artículos reales.");
        System.out.println("Precision IA detectada: " + resultado.get("precision_descubrimiento_ia"));
    }
}
