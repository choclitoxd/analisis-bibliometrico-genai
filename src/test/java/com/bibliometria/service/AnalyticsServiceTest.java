package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsServiceTest {

    private AnalyticsService service;

    @BeforeEach
    void setUp() {
        service = new AnalyticsService();
    }

    @Test
    @DisplayName("Debe extraer países correctamente de los abstracts")
    void testGenerarDatosGeograficos() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setAbstractContent("Study conducted in USA and UK.");
        art1.setAuthors(Arrays.asList("Author A"));

        ScientificArticle art2 = new ScientificArticle();
        art2.setAbstractContent("Research in Spain.");
        art2.setAuthors(Arrays.asList("Author B (USA)"));

        List<ScientificArticle> articles = Arrays.asList(art1, art2);

        // Act
        Map<String, Integer> result = service.generarDatosGeograficos(articles);

        // Assert
        assertEquals(2, result.get("USA"), "USA debe aparecer 2 veces");
        assertEquals(1, result.get("UK"), "UK debe aparecer 1 vez");
        assertEquals(1, result.get("Spain"), "Spain debe aparecer 1 vez");
    }

    @Test
    @DisplayName("Debe agrupar artículos por año y fuente correctamente")
    void testGenerarDatosTemporales() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setPublicationYear(2023);
        art1.setSource("ACM");

        ScientificArticle art2 = new ScientificArticle();
        art2.setPublicationYear(2023);
        art2.setSource("ACM");

        ScientificArticle art3 = new ScientificArticle();
        art3.setPublicationYear(2024);
        art3.setSource("ScienceDirect");

        List<ScientificArticle> articles = Arrays.asList(art1, art2, art3);

        // Act
        Map<Integer, Map<String, Long>> result = service.generarDatosTemporales(articles);

        // Assert
        assertEquals(2, result.get(2023).get("ACM"), "2023-ACM debe tener 2 artículos");
        assertEquals(1, result.get(2024).get("ScienceDirect"), "2024-ScienceDirect debe tener 1 artículo");
    }
}
