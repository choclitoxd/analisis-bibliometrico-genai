package com.bibliometria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScientificArticleTest {

    @Test
    @DisplayName("Deduplicación: Dos artículos con títulos casi iguales deben ser iguales")
    void testEqualsAndHashCode_TitleNormalization() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setTitle("Generative AI in Education."); // Con punto
        
        ScientificArticle art2 = new ScientificArticle();
        art2.setTitle("generative ai in education"); // Minúscula y sin punto

        // Act & Assert
        assertEquals(art1, art2, "Deben ser iguales por normalización de título");
        assertEquals(art1.hashCode(), art2.hashCode(), "HashCodes deben coincidir");
    }

    @Test
    @DisplayName("Deduplicación: Dos artículos con títulos distintos deben ser diferentes")
    void testEqualsAndHashCode_DifferentTitles() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setTitle("Neural Networks");
        
        ScientificArticle art2 = new ScientificArticle();
        art2.setTitle("Deep Learning");

        // Act & Assert
        assertNotEquals(art1, art2, "Títulos distintos deben generar objetos desiguales");
    }
}
