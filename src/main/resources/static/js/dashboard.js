/**
 * Dashboard Bibliométrico - Lógica de Visualización
 */

function initDashboard(data) {
    console.log("Inicializando Dashboard con datos:", data);

    const geoData = data.visualizacion_geografica || {};
    const timelineData = data.visualizacion_temporal || {};
    const cloudData = data.descubrimiento_nuevas_palabras || {};

    // 1. --- Configuración Mapa (ECharts) ---
    const mapElement = document.getElementById('geoMap');
    if (mapElement) {
        const mapChart = echarts.init(mapElement);
        const mapEntries = Object.entries(geoData).map(([name, value]) => ({ name, value }));
        
        mapChart.setOption({
            tooltip: { trigger: 'item' },
            visualMap: { 
                min: 0, 
                max: 5, 
                text: ['Alto', 'Bajo'], 
                realtime: false, 
                calculable: true, 
                inRange: { color: ['lightskyblue', 'yellow', 'orangered'] } 
            },
            series: [{ 
                name: 'Artículos', 
                type: 'map', 
                map: 'world', 
                data: mapEntries 
            }]
        });
        
        window.addEventListener('resize', () => mapChart.resize());
    }

    // 2. --- Configuración Línea Temporal (Chart.js) ---
    const timelineElement = document.getElementById('timelineChart');
    if (timelineElement) {
        const years = Object.keys(timelineData).sort();
        const values = years.map(y => Object.values(timelineData[y]).reduce((a, b) => a + b, 0));
        
        new Chart(timelineElement, {
            type: 'line',
            data: { 
                labels: years, 
                datasets: [{ 
                    label: 'Producción Científica', 
                    data: values, 
                    borderColor: '#0d6efd', 
                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                    fill: true,
                    tension: 0.3
                }] 
            },
            options: {
                responsive: true,
                maintainAspectRatio: false
            }
        });
    }

    // 3. --- Configuración Nube de Palabras (WordCloud2) ---
    const cloudCanvas = document.getElementById('wordCloudCanvas');
    if (cloudCanvas) {
        const cloudArray = Object.entries(cloudData).map(([text, size]) => [text, size * 10 + 10]);
        WordCloud(cloudCanvas, { 
            list: cloudArray, 
            weightFactor: 1, 
            fontFamily: 'Arial', 
            color: 'random-dark', 
            rotateRatio: 0.5,
            gridSize: 10
        });
    }
}
