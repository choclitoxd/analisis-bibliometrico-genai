package com.bibliometria.service.clustering;

import com.bibliometria.model.ClusterNode;
import com.bibliometria.model.ScientificArticle;
import com.bibliometria.service.algorithms.SimilarityAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Motor principal de Agrupamiento Jerárquico.
 * Implementa el proceso iterativo de fusión de clústeres.
 */
@Service
public class HierarchicalClusteringService {

    private static final Logger log = LoggerFactory.getLogger(HierarchicalClusteringService.class);
    private final SimilarityMatrixService matrixService;

    public HierarchicalClusteringService(SimilarityMatrixService matrixService) {
        this.matrixService = matrixService;
    }

    /**
     * Ejecuta el agrupamiento jerárquico y retorna el nodo raíz del dendrograma.
     */
    public ClusterNode cluster(List<ScientificArticle> articles, SimilarityAlgorithm algorithm, LinkageStrategy strategy) {
        if (articles == null || articles.isEmpty()) return null;

        // FASE 1: Preparación
        List<ClusterNode> activeClusters = new ArrayList<>();
        for (ScientificArticle art : articles) {
            activeClusters.add(new ClusterNode(art));
        }

        log.info("Iniciando agrupamiento para {} artículos usando estrategia {}", articles.size(), strategy);

        // FASE 2: Bucle Principal
        int clusterCount = 1;
        while (activeClusters.size() > 1) {
            // Paso 1: Búsqueda del Óptimo (Par más similar)
            double maxSimilarity = -1.0;
            int indexA = -1;
            int indexB = -1;

            for (int i = 0; i < activeClusters.size(); i++) {
                for (int j = i + 1; j < activeClusters.size(); j++) {
                    double sim = calculateClusterSimilarity(activeClusters.get(i), activeClusters.get(j), algorithm, strategy);
                    if (sim > maxSimilarity) {
                        maxSimilarity = sim;
                        indexA = i;
                        indexB = j;
                    }
                }
            }

            // Paso 2: La Fusión (Merge)
            ClusterNode nodeA = activeClusters.get(indexA);
            ClusterNode nodeB = activeClusters.get(indexB);
            
            ClusterNode mergedNode = new ClusterNode(
                "Cluster_" + (clusterCount++),
                nodeA,
                nodeB,
                maxSimilarity
            );

            // Paso 4: Limpieza de Estado (Eliminar A y B, añadir C)
            // Importante: Eliminar primero el de mayor índice para no alterar la posición del otro
            activeClusters.remove(indexB);
            activeClusters.remove(indexA);
            activeClusters.add(mergedNode);
            
            log.debug("Fusión realizada: {} y {} con similitud {}", nodeA.getName(), nodeB.getName(), maxSimilarity);
        }

        // FASE 3: Salida (El último nodo es la raíz)
        log.info("Agrupamiento finalizado. Dendrograma construido exitosamente.");
        return activeClusters.get(0);
    }

    /**
     * Paso 3: Actualización Matemática (Los 3 Algoritmos de Enlace).
     * Calcula la similitud entre dos clústeres basándose en sus miembros.
     */
    private double calculateClusterSimilarity(ClusterNode c1, ClusterNode c2, SimilarityAlgorithm algorithm, LinkageStrategy strategy) {
        List<ScientificArticle> list1 = c1.getArticles();
        List<ScientificArticle> list2 = c2.getArticles();
        
        double result = (strategy == LinkageStrategy.COMPLETE) ? Double.MAX_VALUE : 0.0;
        double sum = 0.0;
        int pairCount = 0;

        for (ScientificArticle a1 : list1) {
            for (ScientificArticle a2 : list2) {
                double sim = algorithm.calculate(a1.getAbstractContent(), a2.getAbstractContent());
                
                switch (strategy) {
                    case SINGLE:
                        if (sim > result) result = sim;
                        break;
                    case COMPLETE:
                        if (sim < result) result = sim;
                        break;
                    case AVERAGE:
                        sum += sim;
                        pairCount++;
                        break;
                }
            }
        }

        return (strategy == LinkageStrategy.AVERAGE) ? (sum / pairCount) : result;
    }
}
