package com.bibliometria.service.algorithms;

/**
 * Interfaz para algoritmos de similitud textual.
 * Define el contrato para el cálculo de distancias o coincidencias entre textos.
 */
public interface SimilarityAlgorithm {
    /**
     * Calcula un valor de similitud o distancia entre dos textos.
     * @param text1 Primer texto (ej. Abstract A)
     * @param text2 Segundo texto (ej. Abstract B)
     * @return Valor numérico que representa la relación entre los textos.
     */
    double calculate(String text1, String text2);
    
    String getAlgorithmName();
}
