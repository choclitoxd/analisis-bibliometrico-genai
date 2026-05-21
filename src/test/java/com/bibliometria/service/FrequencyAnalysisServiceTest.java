package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FrequencyAnalysisServiceTest {

    private FrequencyAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new FrequencyAnalysisService();
    }

    @Test
    @DisplayName("Debe contar correctamente los conceptos base en los abstracts")
    void testAnalizarFrecuencias() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setAbstractContent("Generative models are great for prompting.");
        
        ScientificArticle art2 = new ScientificArticle();
        art2.setAbstractContent("Machine learning is a core part of generative models.");

        List<ScientificArticle> articles = Arrays.asList(art1, art2);

        // Act
        Map<String, Integer> result = service.analizarFrecuencias(articles);

        // Assert
        assertEquals(2, result.get("generative models"), "Debe encontrar 'generative models' dos veces");
        assertEquals(1, result.get("prompting"), "Debe encontrar 'prompting' una vez");
        assertEquals(1, result.get("machine learning"), "Debe encontrar 'machine learning' una vez");
        assertEquals(0, result.get("ethics"), "Conceptos no presentes deben ser 0");
    }

    @Test
    @DisplayName("Debe manejar abstracts nulos o vacíos sin errores")
    void testAnalizarFrecuencias_EmptyData() {
        // Arrange
        ScientificArticle art = new ScientificArticle();
        art.setAbstractContent(null);

        // Act & Assert
        assertDoesNotThrow(() -> service.analizarFrecuencias(Collections.singletonList(art)));
        Map<String, Integer> result = service.analizarFrecuencias(Collections.singletonList(art));
        assertTrue(result.values().stream().allMatch(v -> v == 0));
    }
}
