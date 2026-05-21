package com.bibliometria.service.clustering;

import com.bibliometria.model.ScientificArticle;
import com.bibliometria.service.algorithms.SimilarityAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SimilarityMatrixServiceTest {

    private SimilarityMatrixService matrixService;
    private SimilarityAlgorithm algorithmMock;

    @BeforeEach
    void setUp() {
        matrixService = new SimilarityMatrixService();
        algorithmMock = Mockito.mock(SimilarityAlgorithm.class);
    }

    @Test
    @DisplayName("Debe generar una matriz simétrica correcta")
    void testCalculateMatrix_SymmetryAndContent() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setAbstractContent("Texto 1");
        
        ScientificArticle art2 = new ScientificArticle();
        art2.setAbstractContent("Texto 2");

        List<ScientificArticle> articles = Arrays.asList(art1, art2);
        
        // Simulamos que la similitud entre el 1 y el 2 es 0.75
        when(algorithmMock.calculate("Texto 1", "Texto 2")).thenReturn(0.75);

        // Act
        double[][] matrix = matrixService.calculateMatrix(articles, algorithmMock);

        // Assert
        assertEquals(2, matrix.length);
        assertEquals(1.0, matrix[0][0], "Diagonal debe ser 1.0");
        assertEquals(1.0, matrix[1][1], "Diagonal debe ser 1.0");
        assertEquals(0.75, matrix[0][1], "Similitud [0][1] incorrecta");
        assertEquals(0.75, matrix[1][0], "Matriz debe ser simétrica ([1][0] == [0][1])");
    }

    @Test
    @DisplayName("Debe manejar listas vacías devolviendo matriz 0x0")
    void testCalculateMatrix_EmptyList() {
        double[][] matrix = matrixService.calculateMatrix(null, algorithmMock);
        assertEquals(0, matrix.length);
    }
}
