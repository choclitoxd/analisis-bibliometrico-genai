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
    private CosineSimilarityService cosineService;

    @BeforeEach
    void setUp() {
        jaccardService = new JaccardService();
        levenshteinService = new LevenshteinService();
        lcsService = new LcsService();
        cosineService = new CosineSimilarityService();
    }

    @Test
    @DisplayName("Test Jaccard: Textos con 50% de similitud matemática")
    void testJaccardSimilarity() {
        String t1 = "generative artificial intelligence";
        String t2 = "artificial intelligence models";
        assertEquals(0.5, jaccardService.calculate(t1, t2), 0.001);
    }

    @Test
    @DisplayName("Test Levenshtein: Distancia de edición")
    void testLevenshtein() {
        assertEquals(1.0, levenshteinService.calculate("gato", "pato"));
        assertEquals(0.0, levenshteinService.calculate("igual", "igual"));
    }

    @Test
    @DisplayName("Test LCS: Subsecuencia común")
    void testLcs() {
        assertEquals(4.0, lcsService.calculate("AGGTAB", "GXTXAYB"));
    }

    @Test
    @DisplayName("Test Cosine: Similitud vectorial por frecuencia")
    void testCosineSimilarity() {
        // "ia" aparece 2 veces en t1, 1 vez en t2
        String t1 = "ia ia generativa";
        String t2 = "ia generativa";
        
        // Vectores aproximados:
        // ia: t1=2, t2=1
        // generativa: t1=1, t2=1
        // Dot product: (2*1) + (1*1) = 3
        // Magnitude 1: sqrt(2^2 + 1^2) = sqrt(5) = 2.236
        // Magnitude 2: sqrt(1^2 + 1^2) = sqrt(2) = 1.414
        // Cosine = 3 / (2.236 * 1.414) = 3 / 3.162 = 0.948
        
        double result = cosineService.calculate(t1, t2);
        assertTrue(result > 0.9 && result < 1.0, "La similitud debe ser alta pero no 1.0");
    }

    @Test
    @DisplayName("Robustez: Manejo de nulos y vacíos")
    void testNullAndEmpty() {
        assertAll(
            () -> assertEquals(0.0, jaccardService.calculate(null, "")),
            () -> assertEquals(0.0, levenshteinService.calculate("", null)),
            () -> assertEquals(0.0, lcsService.calculate(null, null)),
            () -> assertEquals(0.0, cosineService.calculate("", ""))
        );
    }
}
