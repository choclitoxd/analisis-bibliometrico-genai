package com.bibliometria.service.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para validar los algoritmos matemáticos de similitud.
 * Sigue el patrón AAA: Arrange, Act, Assert.
 */
class SimilarityAlgorithmsTest {

    private JaccardService jaccardService;
    private LevenshteinService levenshteinService;
    private LcsService lcsService;

    @BeforeEach
    void setUp() {
        // Arrange general: Instanciamos los servicios antes de cada prueba
        jaccardService = new JaccardService();
        levenshteinService = new LevenshteinService();
        lcsService = new LcsService();
    }

    @Test
    @DisplayName("Test Jaccard: Textos con 50% de similitud matemática")
    void testJaccardSimilarity_CalculatesCorrectly() {
        // Arrange
        // Texto 1: "generative", "artificial", "intelligence" (3 palabras)
        String texto1 = "generative artificial intelligence";
        // Texto 2: "artificial", "intelligence", "models" (3 palabras)
        String texto2 = "artificial intelligence models";
        
        // Matemáticas:
        // Intersección (compartidas): "artificial", "intelligence" = 2
        // Unión (únicas totales): "generative", "artificial", "intelligence", "models" = 4
        // Jaccard Esperado = 2 / 4 = 0.5

        // Act
        double resultado = jaccardService.calculate(texto1, texto2);

        // Assert
        assertEquals(0.5, resultado, 0.001, "La similitud de Jaccard debería ser exactamente 0.5");
    }

    @Test
    @DisplayName("Test Levenshtein: Textos idénticos deben tener costo 0")
    void testLevenshteinDistance_IdenticalTexts() {
        // Arrange
        String texto1 = "generative ai";
        String texto2 = "generative ai";

        // Act
        double costo = levenshteinService.calculate(texto1, texto2);

        // Assert
        assertEquals(0.0, costo, "Textos idénticos no requieren operaciones, el costo debe ser 0");
    }

    @Test
    @DisplayName("Test Levenshtein: Inserción y sustitución de caracteres")
    void testLevenshteinDistance_DifferentTexts() {
        // Arrange
        String texto1 = "gato";
        String texto2 = "pato"; // 1 sustitución (g por p)
        String texto3 = "patos"; // 1 sustitución (g por p) + 1 inserción (s)

        // Act
        double costo1 = levenshteinService.calculate(texto1, texto2);
        double costo2 = levenshteinService.calculate(texto1, texto3);

        // Assert
        assertEquals(1.0, costo1, "De 'gato' a 'pato' hay exactamente 1 operación (sustitución)");
        assertEquals(2.0, costo2, "De 'gato' a 'patos' hay exactamente 2 operaciones");
    }

    @Test
    @DisplayName("Test LCS: Subsecuencia común más larga")
    void testLcs_CalculatesLongestSubsequence() {
        // Arrange
        String texto1 = "AGGTAB";
        String texto2 = "GXTXAYB";
        // La subsecuencia común más larga es "GTAB", que tiene una longitud de 4.

        // Act
        double longitud = lcsService.calculate(texto1, texto2);

        // Assert
        assertEquals(4.0, longitud, "La longitud de la subsecuencia 'GTAB' debe ser 4");
    }

    @Test
    @DisplayName("Manejo de Nulos: Los algoritmos no deben lanzar NullPointerException")
    void testAlgorithms_NullHandling() {
        // Act & Assert para Jaccard
        assertEquals(0.0, jaccardService.calculate(null, "texto"), "Jaccard debe retornar 0 si un texto es nulo");
        
        // Act & Assert para Levenshtein
        assertEquals(0.0, levenshteinService.calculate("texto", null), "Levenshtein debe retornar 0 si un texto es nulo");
    }
}
