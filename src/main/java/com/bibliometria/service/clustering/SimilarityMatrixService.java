package com.bibliometria.service.clustering;

import com.bibliometria.model.ScientificArticle;
import com.bibliometria.service.algorithms.SimilarityAlgorithm;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio encargado de calcular la matriz de similitud o distancia
 * entre todos los pares de artículos científicos unificados.
 * Este es el cimiento para el Agrupamiento Jerárquico.
 */
@Service
public class SimilarityMatrixService {

    /**
     * Calcula una matriz cuadrada (N x N) donde la posición [i][j] 
     * representa la similitud entre el artículo i y el artículo j.
     * 
     * @param articles Lista de artículos únicos.
     * @param algorithm El algoritmo matemático a utilizar (ej. Coseno, Jaccard).
     * @return Matriz bidimensional de tipo double con los porcentajes de similitud.
     */
    public double[][] calculateMatrix(List<ScientificArticle> articles, SimilarityAlgorithm algorithm) {
        if (articles == null || articles.isEmpty()) {
            return new double[0][0];
        }

        int n = articles.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    // Autosimilitud: Un artículo es idéntico a sí mismo (100% o 1.0)
                    matrix[i][j] = 1.0; 
                } else if (i < j) {
                    // Optimización: Solo calculamos la mitad superior y espejamos
                    // porque la similitud es una relación conmutativa: sim(A,B) = sim(B,A)
                    String text1 = articles.get(i).getAbstractContent();
                    String text2 = articles.get(j).getAbstractContent();
                    
                    // Asegurar que no procesamos nulos
                    if (text1 == null) text1 = "";
                    if (text2 == null) text2 = "";

                    double similarity = algorithm.calculate(text1, text2);
                    
                    matrix[i][j] = similarity;
                    matrix[j][i] = similarity; // Espejo (Matriz simétrica)
                }
            }
        }

        return matrix;
    }
}
