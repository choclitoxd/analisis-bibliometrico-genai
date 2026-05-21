package com.bibliometria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un nodo en el árbol jerárquico (Dendrograma).
 * Puede ser una hoja (un artículo individual) o un nodo interno (la fusión de dos clústeres).
 */
public class ClusterNode {
    
    private String name; // Nombre del artículo o del clúster generado
    private ClusterNode leftChild;
    private ClusterNode rightChild;
    private double distance; // La distancia a la que se fusionaron los hijos
    private List<ScientificArticle> articles; // Artículos contenidos en este clúster

    /**
     * Constructor para un nodo HOJA (Un solo artículo inicial).
     * @param article El artículo científico original.
     */
    public ClusterNode(ScientificArticle article) {
        this.name = article.getTitle();
        this.leftChild = null;
        this.rightChild = null;
        this.distance = 0.0;
        this.articles = new ArrayList<>();
        this.articles.add(article);
    }

    /**
     * Constructor para un nodo INTERNO (Fusión de dos clústeres).
     * @param name Nombre identificador del nuevo clúster.
     * @param left Nodo hijo izquierdo.
     * @param right Nodo hijo derecho.
     * @param distance Métrica de distancia/similitud en el momento de la unión.
     */
    public ClusterNode(String name, ClusterNode left, ClusterNode right, double distance) {
        this.name = name;
        this.leftChild = left;
        this.rightChild = right;
        this.distance = distance;
        this.articles = new ArrayList<>();
        // Un nodo padre hereda todos los artículos de sus sub-clústeres
        if (left != null) this.articles.addAll(left.getArticles());
        if (right != null) this.articles.addAll(right.getArticles());
    }

    // Getters
    public String getName() { return name; }
    public ClusterNode getLeftChild() { return leftChild; }
    public ClusterNode getRightChild() { return rightChild; }
    public double getDistance() { return distance; }
    public List<ScientificArticle> getArticles() { return articles; }
    
    /**
     * Indica si el nodo es una hoja (representa un solo artículo).
     */
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }
}
