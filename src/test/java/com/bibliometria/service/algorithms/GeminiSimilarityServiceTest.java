package com.bibliometria.service.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeminiSimilarityServiceTest {

    private GeminiSimilarityService geminiService;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = Mockito.mock(RestTemplate.class);
        geminiService = new GeminiSimilarityService(restTemplateMock);
    }

    @Test
    @DisplayName("Manejo de nulos en Gemini Service")
    void testCalculate_NullHandling() {
        assertEquals(0.0, geminiService.calculate(null, "text"));
        Mockito.verifyNoInteractions(restTemplateMock);
    }

    @Test
    @DisplayName("Verificar Nombre del Algoritmo Gemini")
    void testAlgorithmName() {
        assertEquals("IA Google Gemini (Embeddings Semánticos)", geminiService.getAlgorithmName());
    }
}
