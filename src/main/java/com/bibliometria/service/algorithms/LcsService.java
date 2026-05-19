package com.bibliometria.service.algorithms;

import org.springframework.stereotype.Service;

/**
 * Implementación de la Subsecuencia Común Más Larga (LCS) mediante Programación Dinámica.
 * Ideal para identificar patrones compartidos en textos largos.
 */
@Service
public class LcsService implements SimilarityAlgorithm {

    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) return 0;

        int m = s1.length();
        int n = s2.length();
        
        // Matriz para almacenar longitudes de subsecuencias comunes
        int[][] dp = new int[m + 1][n + 1];

        // Llenado iterativo de la matriz
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    @Override
    public String getAlgorithmName() {
        return "LCS (Longest Common Subsequence)";
    }
}
