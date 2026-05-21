package com.bibliometria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClusterNodeTest {

    @Test
    @DisplayName("Un nodo hoja debe contener exactamente un artículo")
    void testLeafNodeCreation() {
        ScientificArticle article = new ScientificArticle();
        article.setTitle("Articulo Test");
        
        ClusterNode leaf = new ClusterNode(article);
        
        assertTrue(leaf.isLeaf());
        assertEquals("Articulo Test", leaf.getName());
        assertEquals(1, leaf.getArticles().size());
        assertNull(leaf.getLeftChild());
    }

    @Test
    @DisplayName("Un nodo interno debe fusionar artículos de sus hijos")
    void testInternalNodeMerging() {
        // Arrange
        ScientificArticle art1 = new ScientificArticle();
        art1.setTitle("A1");
        ScientificArticle art2 = new ScientificArticle();
        art2.setTitle("A2");
        
        ClusterNode leaf1 = new ClusterNode(art1);
        ClusterNode leaf2 = new ClusterNode(art2);

        // Act
        ClusterNode parent = new ClusterNode("Cluster_A1A2", leaf1, leaf2, 0.5);

        // Assert
        assertFalse(parent.isLeaf());
        assertEquals(2, parent.getArticles().size());
        assertTrue(parent.getArticles().contains(art1));
        assertTrue(parent.getArticles().contains(art2));
        assertEquals(0.5, parent.getDistance());
    }
}
