package com.bibliometria.service.clustering;

import com.bibliometria.model.ClusterNode;
import com.bibliometria.model.ScientificArticle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClusteringEvaluatorServiceTest {

    @Test
    @DisplayName("Debe calcular una correlación de 1.0 para una matriz idéntica al árbol")
    void testEvaluationCoherence_PerfectMatch() {
        // Arrange
        ClusteringEvaluatorService evaluator = new ClusteringEvaluatorService();
        
        ScientificArticle a1 = new ScientificArticle(); a1.setTitle("A1");
        ScientificArticle a2 = new ScientificArticle(); a2.setTitle("A2");
        List<ScientificArticle> articles = Arrays.asList(a1, a2);

        // Creamos un árbol manual donde se unieron a distancia 0.8
        ClusterNode leaf1 = new ClusterNode(a1);
        ClusterNode leaf2 = new ClusterNode(a2);
        ClusterNode root = new ClusterNode("Root", leaf1, leaf2, 0.8);

        // Matriz original idéntica (0.8 de similitud)
        double[][] original = {{1.0, 0.8}, {0.8, 1.0}};

        // Act
        double correlation = evaluator.evaluateCoherence(original, root, articles);

        // Assert
        assertEquals(1.0, correlation, 0.001, "La correlación debe ser perfecta (1.0)");
    }
}
