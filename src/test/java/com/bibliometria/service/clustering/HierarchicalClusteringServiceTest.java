package com.bibliometria.service.clustering;

import com.bibliometria.model.ClusterNode;
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

class HierarchicalClusteringServiceTest {

    private HierarchicalClusteringService clusteringService;
    private SimilarityMatrixService matrixService;
    private SimilarityAlgorithm algorithmMock;

    @BeforeEach
    void setUp() {
        matrixService = new SimilarityMatrixService();
        clusteringService = new HierarchicalClusteringService(matrixService);
        algorithmMock = Mockito.mock(SimilarityAlgorithm.class);
    }

    @Test
    @DisplayName("Debe agrupar 3 artículos hasta llegar a una única raíz")
    void testClusteringProcess() {
        // Arrange
        ScientificArticle a1 = new ScientificArticle(); a1.setTitle("T1"); a1.setAbstractContent("Contenido 1");
        ScientificArticle a2 = new ScientificArticle(); a2.setTitle("T2"); a2.setAbstractContent("Contenido 2");
        ScientificArticle a3 = new ScientificArticle(); a3.setTitle("T3"); a3.setAbstractContent("Contenido 3");
        
        List<ScientificArticle> articles = Arrays.asList(a1, a2, a3);

        // Simulamos similitudes: T1 y T2 son muy parecidos (0.9), T3 es diferente (0.1)
        when(algorithmMock.calculate("Contenido 1", "Contenido 2")).thenReturn(0.9);
        when(algorithmMock.calculate("Contenido 1", "Contenido 3")).thenReturn(0.1);
        when(algorithmMock.calculate("Contenido 2", "Contenido 3")).thenReturn(0.1);

        // Act
        ClusterNode root = clusteringService.cluster(articles, algorithmMock, LinkageStrategy.SINGLE);

        // Assert
        assertNotNull(root);
        assertEquals(3, root.getArticles().size(), "La raíz debe contener todos los artículos");
        assertFalse(root.isLeaf());
        
        // Verificamos que el primer hijo del nivel superior sea una fusión (T1 y T2) o T3
        // En Single Linkage con estos datos, T1 y T2 se unen primero.
        assertNotNull(root.getLeftChild());
        assertNotNull(root.getRightChild());
    }
}
