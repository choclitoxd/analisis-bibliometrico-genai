package com.bibliometria.service.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Suite de pruebas para el servicio de IA Avanzada.
 * Utiliza Mockito para aislar la lógica del servicio de las llamadas de red reales.
 */
class AdvancedLlmSimilarityServiceTest {

    private AdvancedLlmSimilarityService llmService;
    private RestTemplate restTemplateMock;
    private CosineSimilarityService cosineService;

    @BeforeEach
    void setUp() {
        // Arrange: Creamos un "Mock" (simulador) de la clase que hace peticiones HTTP
        restTemplateMock = Mockito.mock(RestTemplate.class);
        cosineService = new CosineSimilarityService(); // Usamos el servicio real de matemáticas
        
        llmService = new AdvancedLlmSimilarityService(restTemplateMock, cosineService);
    }

    @Test
    @DisplayName("Manejo de Nulos: No debe explotar la API si enviamos textos vacíos")
    void testCalculate_NullOrEmptyTexts() {
        // Act & Assert
        assertEquals(0.0, llmService.calculate(null, "Abstract B"));
        assertEquals(0.0, llmService.calculate("Abstract A", ""));
        
        // Verificamos que el RestTemplate NUNCA fue llamado si los textos están vacíos (Ahorro de cuota/seguridad)
        Mockito.verifyNoInteractions(restTemplateMock);
    }

    @Test
    @DisplayName("Verificar Nombre del Algoritmo")
    void testAlgorithmName() {
        assertEquals("Embeddings de IA Comercial (OpenAI/Gemini)", llmService.getAlgorithmName());
    }
}
