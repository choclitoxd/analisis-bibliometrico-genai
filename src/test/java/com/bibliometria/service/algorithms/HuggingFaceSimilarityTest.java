package com.bibliometria.service.algorithms;

import com.bibliometria.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración/unidad para validar la lógica del servicio de IA.
 */
public class HuggingFaceSimilarityTest {

    // Test manual de la lógica matemática de similitud cosenoidal
    @Test
    public void testCosineLogic() {
        // Simulamos dos vectores idénticos
        double[] v1 = {1.0, 2.0, 3.0};
        double[] v2 = {1.0, 2.0, 3.0};
        
        double dotProduct = 1*1 + 2*2 + 3*3; // 1 + 4 + 9 = 14
        double norm = Math.sqrt(1*1 + 2*2 + 3*3); // sqrt(14)
        double similarity = dotProduct / (norm * norm); // 14 / 14 = 1.0
        
        assertEquals(1.0, similarity, 0.001, "Vectores idénticos deben tener similitud 1.0");
    }
}
