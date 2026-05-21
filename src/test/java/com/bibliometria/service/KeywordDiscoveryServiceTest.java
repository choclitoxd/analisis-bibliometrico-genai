package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KeywordDiscoveryServiceTest {

    private KeywordDiscoveryService service;

    @BeforeEach
    void setUp() {
        service = new KeywordDiscoveryService();
    }

    @Test
    @DisplayName("Debe descubrir bi-gramas frecuentes que no están excluidos")
    void testDescubrirNuevasPalabras() {
        // Arrange
        ScientificArticle art = new ScientificArticle();
        art.setAbstractContent("Artificial intelligence is changing the world. Artificial intelligence creates new jobs.");
        Set<String> excluidas = new HashSet<>(Arrays.asList("world"));

        // Act
        Map<String, Integer> result = service.descubrirNuevasPalabras(Collections.singletonList(art), excluidas);

        // Assert
        assertTrue(result.containsKey("artificial intelligence"), "Debe descubrir el bi-grama 'artificial intelligence'");
        assertEquals(2, result.get("artificial intelligence"), "Debe contar 2 ocurrencias del bi-grama");
        assertFalse(result.containsKey("world"), "No debe incluir palabras excluidas");
    }

    @Test
    @DisplayName("Debe evaluar la precisión correctamente contra las keywords del autor")
    void testEvaluarPrecision() {
        // Arrange
        Map<String, Integer> descubiertas = new HashMap<>();
        descubiertas.put("neural networks", 5);
        descubiertas.put("transformer", 3);

        ScientificArticle art = new ScientificArticle();
        art.setKeywords(Arrays.asList("Neural Networks", "Deep Learning"));

        // Act
        double precision = service.evaluarPrecision(descubiertas, Collections.singletonList(art));

        // Assert
        // Coincide "neural networks" (1 de 2 descubiertas)
        assertEquals(0.5, precision, 0.001);
    }
}
