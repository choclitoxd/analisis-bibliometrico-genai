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

    @Test
    @DisplayName("Debe descubrir bi-gramas y unigramas ignorando stop-words y base")
    void testDescubrirNuevasPalabras() {
        // Arrange
        ScientificArticle art = new ScientificArticle();
        // "neural networks" es bi-grama, "essential" es unigrama
        art.setAbstractContent("Neural networks are essential. Neural networks are complex.");

        // Act
        Map<String, Integer> result = service.descubrirNuevasPalabras(Collections.singletonList(art));

        // Assert
        assertTrue(result.containsKey("neural networks"), "Debe detectar el bi-grama 'neural networks'");
        assertEquals(2, result.get("neural networks"), "Debe contar 2 ocurrencias del bi-grama");
        assertTrue(result.containsKey("essential"), "Debe detectar el unigrama 'essential'");
        assertFalse(result.containsKey("are"), "No debe incluir stop-words");
    }

    @Test
    @DisplayName("Debe calcular el porcentaje de precision correctamente contra las keywords originales")
    void testCalcularPrecision() {
        // Arrange
        Map<String, Integer> nuevasPalabras = new HashMap<>();
        nuevasPalabras.put("neural", 5);
        nuevasPalabras.put("transformer", 3);

        ScientificArticle art = new ScientificArticle();
        art.setKeywords(Arrays.asList("Neural Networks", "Deep Learning"));

        // Act
        double precision = service.calcularPrecision(nuevasPalabras, Collections.singletonList(art));

        // Assert
        // Coincide "neural" (desglosado de "Neural Networks") -> 1 de 2 = 50.0
        assertEquals(50.0, precision, 0.001, "La precisión debería ser 50.0%");
    }
}
