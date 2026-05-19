package com.bibliometria.service.algorithms;

import org.springframework.stereotype.Service;

/**
 * Implementación de la Distancia de Levenshtein mediante Programación Dinámica.
 * Mide el costo mínimo de transformar un texto en otro.
 */
@Service
public class LevenshteinService implements SimilarityAlgorithm {

    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;
        
        int m = s1.length();
        int n = s2.length();
        
        // Matriz de costos (m+1) x (n+1)
        int[][] dp = new int[m + 1][n + 1];

        // Inicialización de casos base
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // Llenado iterativo de la matriz
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1,    // Eliminación
                             dp[i][j - 1] + 1),   // Inserción
                    dp[i - 1][j - 1] + cost       // Sustitución
                );
            }
        }
        
        return dp[m][n];
    }

    @Override
    public String getAlgorithmName() {
        return "Distancia de Levenshtein";
    }
}
