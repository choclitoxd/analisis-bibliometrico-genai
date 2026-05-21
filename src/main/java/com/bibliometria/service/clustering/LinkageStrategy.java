package com.bibliometria.service.clustering;

/**
 * Define las estrategias de unión (Linkage) para el agrupamiento jerárquico.
 */
public enum LinkageStrategy {
    SINGLE,   // Enlace Simple (Máxima similitud / Mínima distancia)
    COMPLETE, // Enlace Completo (Mínima similitud / Máxima distancia)
    AVERAGE   // Enlace Promedio (Promedio matemático)
}
