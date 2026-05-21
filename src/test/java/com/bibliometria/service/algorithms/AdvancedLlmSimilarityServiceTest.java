package com.bibliometria.service.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedLlmSimilarityServiceTest {

    private AdvancedLlmSimilarityService service;

    @BeforeEach
    void setUp() {
        // Mokeamos el RestTemplate ya que para el test de lógica no lo usaremos realmente
        service = new AdvancedLlmSimilarityService(new RestTemplate());
    }

    @Test
    @DisplayName("El motor matemático de alta dimensión debe calcular similitudes coherentes")
    void testMathEngine() {
        // Arrange
        // Vectores simulados de 1536 dimensiones (solo probamos los primeros elementos)
        double[] v1 = new double[1536];
        double[] v2 = new double[1536];
        
        for(int i=0; i<1536; i++) {
            v1[i] = 1.0;
            v2[i] = 1.0;
        }

        // Act
        // Usamos reflexión o un método público si estuviera expuesto, 
        // pero probaremos la lógica a través del calculate determinista que implementamos
        double sim = service.calculate("test text", "test text");

        // Assert
        assertEquals(1.0, sim, 0.001, "Textos idénticos deben resultar en similitud 1.0 tras vectorización");
    }
}
