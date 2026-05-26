# Análisis de Algoritmos: Bibliometría de IA Generativa 2026

Este proyecto es una plataforma avanzada de análisis bibliométrico diseñada para automatizar la extracción, unificación y análisis semántico de literatura científica sobre **Inteligencia Artificial Generativa**.

## 🚀 Características Principales

1.  **Orquestación Multi-Fuente**: Ingestión asíncrona de datos reales desde **Semantic Scholar** (vía S2 API Key) y **Scopus** (Elsevier).
2.  **Arquitectura por Capas**: Implementación de *Clean Architecture* con Spring Boot 3.4 para un sistema robusto y escalable.
3.  **Análisis de Similitud Híbrido**:
    *   **Algoritmos Léxicos**: Levenshtein, Jaccard, LCS y Similitud del Coseno local.
    *   **Inferencia Semántica**: Integración con **Google Gemini (embedding-001)** y **Hugging Face (Router 2025)**.
4.  **Descubrimiento de Conocimiento (Fase 2)**:
    *   Filtro inteligente de *stop-words* y ruido léxico.
    *   Inferencia de **Bigramas** mediante ventana deslizante.
    *   Métrica de precisión real basada en metadatos académicos enriquecidos (`s2FieldsOfStudy`).
5.  **Visualización Analítica**: Dashboard con Heatmaps, WordClouds dinámicos y reportes inmutables en PDF.

## 🛠️ Requisitos Técnicos

*   **Java**: 17 o superior.
*   **Gradle**: 8.7+ (Wrapper incluido).
*   **Contenedores**: Docker (opcional para despliegue).

## 📦 Despliegue de la Aplicación

### 1. Preparación del Entorno
Asegúrese de tener configuradas las claves de API necesarias en el archivo `src/main/resources/application.properties` antes de compilar.

### 2. Despliegue Local (Gradle)
Para compilar y ejecutar la aplicación directamente:
```bash
# Compilar y empaquetar el ejecutable (JAR)
./gradlew build -x test

# Ejecutar el servidor
./gradlew bootRun
```

### 3. Despliegue mediante JAR Ejecutable
Una vez generado el paquete en `build/libs/`, puede ejecutarlo en cualquier entorno con Java 17:
```bash
java -jar build/libs/proyecto-final-0.0.1-SNAPSHOT.jar
```

### 4. Despliegue con Docker (Recomendado)
El proyecto incluye una configuración de contenedores optimizada:
```bash
# Construir la imagen de Docker
docker build -t bibliometria-genai .

# Lanzar el contenedor
docker run -p 8080:8080 bibliometria-genai
```

## 🖥️ Acceso al Sistema

Una vez iniciada la aplicación, puede acceder a las siguientes interfaces:

*   **Dashboard Principal**: [http://localhost:8080](http://localhost:8080)
*   **API de Extracción**: `GET /api/bibliometria/extraer?query=generative+artificial+intelligence`
