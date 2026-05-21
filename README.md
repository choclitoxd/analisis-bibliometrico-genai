# Análisis de Algoritmos: Bibliometría de IA Generativa

Este proyecto es una herramienta avanzada de análisis bibliométrico diseñada para automatizar la extracción, unificación y análisis de artículos científicos en el dominio de la **Inteligencia Artificial Generativa**.

## 🚀 Características Principales

1.  **Extracción Multi-Fuente**: Descarga automatizada desde ScienceDirect y ACM Digital Library.
2.  **Arquitectura por Capas**: Estructura limpia y mantenible basada en Spring Boot 3.
3.  **Motor de Similitud Séxtuple**:
    *   *Matemáticos*: Levenshtein, LCS, Jaccard y Similitud del Coseno.
    *   *Inteligencia Artificial*: Embeddings de Hugging Face y Google Gemini.
4.  **Análisis Estadístico Avanzado**:
    *   Conteo de frecuencias de conceptos clave de IA en educación.
    *   Descubrimiento inteligente de nuevos términos mediante N-gramas.
    *   Evaluación automática de precisión contra keywords de autor.

## 🛠️ Requisitos e Instalación

*   **Java**: 17+
*   **Gradle**: 8.7+ (se incluye Wrapper)
*   **Dominio Obligatorio**: "generative artificial intelligence"

### Configuración de Seguridad
Antes de ejecutar, configura tus tokens en `src/main/resources/application.properties`:
```properties
huggingface.api.token=TU_TOKEN
gemini.api.token=TU_TOKEN
```

### Ejecución
Para iniciar el servidor:
```bash
./gradlew bootRun
```
Accede al endpoint de extracción:
`GET http://localhost:8080/api/bibliometria/extraer?query=generative+artificial+intelligence`

## 📚 Documentación Técnica
Para una explicación profunda de los algoritmos y sus ecuaciones de recurrencia, consulta:
*   [DOCUMENTACION_TECNICA.md](./DOCUMENTACION_TECNICA.md)
*   [GEMINI.md](./GEMINI.md) (Directrices del proyecto)
