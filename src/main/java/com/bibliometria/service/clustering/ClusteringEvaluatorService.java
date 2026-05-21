package com.bibliometria.service.clustering;

import com.bibliometria.model.ClusterNode;
import com.bibliometria.model.ScientificArticle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de evaluar la calidad y fidelidad de los clústeres generados.
 * Utiliza el Coeficiente de Correlación Cofenética (CPCC).
 */
@Service
public class ClusteringEvaluatorService {

    /**
     * Calcula la correlación de Pearson entre la matriz original y la matriz del árbol (cofenética).
     */
    public double evaluateCoherence(double[][] originalMatrix, ClusterNode root, List<ScientificArticle> articles) {
        int n = articles.size();
        if (n <= 1) return 1.0;

        double[][] copheneticMatrix = generateCopheneticMatrix(root, articles);

        // Aplanamos las matrices en vectores para calcular la correlación
        // Solo tomamos el triángulo superior (sin diagonal) para evitar redundancia
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                x.add(originalMatrix[i][j]);
                y.add(copheneticMatrix[i][j]);
            }
        }

        return calculatePearsonCorrelation(x, y);
    }

    /**
     * Genera la matriz cofenética: la distancia a la que cada par de artículos se unió en el árbol.
     */
    private double[][] generateCopheneticMatrix(ClusterNode root, List<ScientificArticle> articles) {
        int n = articles.size();
        double[][] cophenetic = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = findCopheneticDistance(root, articles.get(i), articles.get(j));
                cophenetic[i][j] = dist;
                cophenetic[j][i] = dist;
            }
        }
        return cophenetic;
    }

    /**
     * Encuentra el Ancestro Común más Bajo (LCA) entre dos artículos y devuelve su distancia de fusión.
     */
    private double findCopheneticDistance(ClusterNode node, ScientificArticle a1, ScientificArticle a2) {
        if (node.isLeaf()) return 0.0;

        boolean inLeft = node.getLeftChild().getArticles().contains(a1);
        boolean inRight = node.getRightChild().getArticles().contains(a1);
        
        boolean target2InLeft = node.getLeftChild().getArticles().contains(a2);
        boolean target2InRight = node.getRightChild().getArticles().contains(a2);

        // Si uno está en la izquierda y otro en la derecha, este nodo es su primera unión
        if ((inLeft && target2InRight) || (inRight && target2InLeft)) {
            return node.getDistance();
        }

        // Si ambos están en el mismo lado, seguimos bajando
        if (inLeft && target2InLeft) {
            return findCopheneticDistance(node.getLeftChild(), a1, a2);
        } else {
            return findCopheneticDistance(node.getRightChild(), a1, a2);
        }
    }

    /**
     * Implementación de la fórmula de Correlación de Pearson.
     */
    private double calculatePearsonCorrelation(List<Double> x, List<Double> y) {
        int n = x.size();
        if (n == 0) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0;
        double sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            sumY2 += yi * yi;
        }

        double numerator = (n * sumXY) - (sumX * sumY);
        double denominator = Math.sqrt(((n * sumX2) - (sumX * sumX)) * ((n * sumY2) - (sumY * sumY)));

        if (denominator == 0) return 0.0;
        return numerator / denominator;
    }
}
