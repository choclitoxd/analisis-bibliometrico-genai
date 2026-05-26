package com.bibliometria.integration;

import com.bibliometria.service.algorithms.*;
import com.bibliometria.service.benchmarking.AlgorithmBenchmarkingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class AlgorithmVeracityIntegrationTest {

    @Autowired
    private AlgorithmBenchmarkingService benchmarkingService;

    @Autowired
    private JaccardService jaccardService;

    @Autowired
    private CosineSimilarityService cosineService;

    @Autowired
    private HuggingFaceSimilarityService huggingFaceService;

    @Autowired
    private GeminiSimilarityService geminiService;

    @Autowired
    private LevenshteinService levenshteinService;

    @Autowired
    private LcsService lcsService;

    @Test
    public void validateAlgorithmResultsTable() {
        List<SimilarityAlgorithm> algorithms = Arrays.asList(
            jaccardService,
            cosineService,
            huggingFaceService,
            geminiService,
            levenshteinService,
            lcsService
        );

        System.out.println("\n" + "=".repeat(80));
        System.out.println("VALIDACIÓN EMPÍRICA DE ALGORITMOS (DATOS VERÍDICOS 2026)");
        System.out.println("=".repeat(80));
        System.out.printf("%-25s | %-10s | %-10s | %-10s | %-15s | %-20s\n", 
            "Algoritmo", "Precisión", "Recall", "F1-Score", "Tiempo (ms)", "Calidad");
        System.out.println("-".repeat(110));

        for (SimilarityAlgorithm algo : algorithms) {
            Map<String, Object> res = benchmarkingService.runBenchmark(algo);
            System.out.printf("%-25s | %-10s | %-10s | %-10s | %-15s | %-20s\n",
                res.get("algoritmo"),
                res.get("precision") + "%",
                res.get("recall") + "%",
                res.get("f1_score") + "%",
                res.get("tiempo_promedio_ms"),
                res.get("calidad")
            );
        }
        System.out.println("=".repeat(80) + "\n");
    }
}
