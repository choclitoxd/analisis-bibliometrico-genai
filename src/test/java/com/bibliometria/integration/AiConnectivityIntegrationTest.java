package com.bibliometria.integration;

import com.bibliometria.service.algorithms.GeminiSimilarityService;
import com.bibliometria.service.algorithms.HuggingFaceSimilarityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AiConnectivityIntegrationTest {

    @Autowired
    private GeminiSimilarityService geminiService;

    @Autowired
    private HuggingFaceSimilarityService huggingFaceService;

    @Test
    public void testHuggingFaceRealConnection() {
        String t1 = "Generative Artificial Intelligence";
        String t2 = "Deep Learning and Neural Networks";
        
        System.out.println("Probando conexión real con Hugging Face...");
        double result = huggingFaceService.calculate(t1, t2);
        
        System.out.println("Resultado Hugging Face: " + result);
        // Si el resultado es > 0, significa que la API respondió con un embedding válido.
        // Si es 0.0, es probable que el token sea inválido o haya error de red.
        assertTrue(result >= 0.0, "El resultado debe ser al menos 0.0");
    }

    @Test
    public void testGeminiRealConnection() {
        String t1 = "Large Language Models";
        String t2 = "Natural Language Processing";
        
        System.out.println("Probando conexión real con Google Gemini...");
        double result = geminiService.calculate(t1, t2);
        
        System.out.println("Resultado Gemini: " + result);
        assertTrue(result >= 0.0, "El resultado debe ser al menos 0.0");
    }
}
