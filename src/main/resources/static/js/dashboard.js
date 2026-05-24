/**
 * Dashboard Bibliométrico - Lógica de Visualización PRO
 * Hardened version based on expert diagnostic guidelines.
 */

async function initDashboard(data) {
    console.log("--- INICIO DE DIAGNÓSTICO ECHARTS ---");
    console.log("ECHARTS GLOBAL OBJECT:", typeof echarts !== 'undefined' ? echarts : "MISSING");
    console.log("DATOS RECIBIDOS:", data);

    const geoData = data.visualizacion_geografica || {};
    const timelineData = data.visualizacion_temporal || {};
    const cloudData = data.descubrimiento_nuevas_palabras || {};

    // 1. --- CONFIGURACIÓN MAPA MUNDIAL (ECharts) ---
    const mapElement = document.getElementById('geoMap');
    if (mapElement) {
        try {
            console.log("1. Cargando GeoJSON...");
            const mapUrl = 'https://cdn.jsdelivr.net/gh/apache/echarts-website@asf-site/examples/data/asset/geo/world.json';
            const response = await fetch(mapUrl);
            
            if (!response.ok) throw new Error(`HTTP ${response.status}: No se pudo obtener el GeoJSON`);
            
            const worldJson = await response.json();
            
            console.log("2. Registrando mapa 'world'...");
            echarts.registerMap('world', worldJson);
            
            // Verificación del mapa registrado
            const registeredMap = echarts.getMap('world');
            console.log("MAPA REGISTRADO (getMap):", registeredMap);

            if (!registeredMap) {
                throw new Error("Fallo crítico: El mapa no fue registrado en el sistema de ECharts.");
            }

            const mapChart = echarts.init(mapElement);

            const nameMap = {
                'United States': 'United States of America',
                'United Kingdom': 'United Kingdom',
                'China': 'China',
                'Spain': 'Spain',
                'Colombia': 'Colombia',
                'Brazil': 'Brazil',
                'Germany': 'Germany',
                'France': 'France',
                'Japan': 'Japan'
            };

            const mapEntries = Object.entries(geoData).map(([name, value]) => ({
                name: nameMap[name] || name,
                value: value
            }));

            const option = {
                backgroundColor: 'transparent',
                tooltip: {
                    trigger: 'item',
                    backgroundColor: '#1e293b',
                    borderColor: '#334155',
                    textStyle: { color: '#f8fafc' },
                    formatter: (params) => {
                        return params.value 
                            ? `<strong>${params.name}</strong><br/>Artículos: ${params.value}`
                            : `<strong>${params.name}</strong><br/>Sin registros`;
                    }
                },
                visualMap: {
                    min: 0,
                    max: 10,
                    left: 'left',
                    top: 'bottom',
                    text: ['Alto', 'Bajo'],
                    calculable: true,
                    inRange: { color: ['#334155', '#6366f1', '#38bdf8'] },
                    textStyle: { color: '#94a3b8' }
                },
                series: [{
                    name: 'Distribución Global',
                    type: 'map',
                    map: 'world', // Coincidencia exacta con registerMap
                    roam: true,
                    emphasis: {
                        label: { show: true, color: '#fff' },
                        itemStyle: { areaColor: '#4f46e5' }
                    },
                    itemStyle: {
                        areaColor: '#1e293b',
                        borderColor: 'rgba(255,255,255,0.1)'
                    },
                    data: mapEntries,
                    nameMap: nameMap
                }]
            };

            // LOGS DE DIAGNÓSTICO FINAL (Método más efectivo)
            console.log("--- INSPECCIÓN DE OPCIONES ANTES DE RENDER ---");
            console.log("OPTION OBJECT:", option);
            console.log("SERIES:", option.series);
            console.log("MAP NAME IN SERIES:", option.series[0].map);

            mapChart.setOption(option);
            window.addEventListener('resize', () => mapChart.resize());
            console.log("--- MAPA RENDERIZADO CON ÉXITO ---");

        } catch (error) {
            console.error("ERROR CRÍTICO EN MAPA:", error);
            mapElement.innerHTML = `<div style="color:#ef4444; padding:20px; text-align:center;">
                <p>⚠️ Error de inicialización geográfica.</p>
                <small>${error.message}</small>
            </div>`;
        }
    }

    // 2. --- LÍNEA TEMPORAL (Chart.js) ---
    const timelineElement = document.getElementById('timelineChart');
    if (timelineElement) {
        const years = Object.keys(timelineData).sort();
        const values = years.map(y => {
            const providers = timelineData[y];
            return Object.values(providers).reduce((a, b) => a + b, 0);
        });

        new Chart(timelineElement, {
            type: 'line',
            data: {
                labels: years,
                datasets: [{
                    label: 'Artículos',
                    data: values,
                    borderColor: '#6366f1',
                    backgroundColor: 'rgba(99, 102, 241, 0.1)',
                    fill: true,
                    tension: 0.4,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#94a3b8' } },
                    x: { grid: { display: false }, ticks: { color: '#94a3b8' } }
                }
            }
        });
    }

    // 3. --- NUBE DE PALABRAS (WordCloud2) ---
    const cloudCanvas = document.getElementById('wordCloudCanvas');
    if (cloudCanvas) {
        const container = cloudCanvas.parentElement;
        cloudCanvas.width = container.offsetWidth;
        cloudCanvas.height = 350;

        const words = Object.entries(cloudData).map(([text, size]) => [text, size * 6 + 12]);
        
        WordCloud(cloudCanvas, {
            list: words,
            gridSize: 12,
            weightFactor: 1,
            fontFamily: 'Inter, sans-serif',
            color: () => ['#6366f1', '#38bdf8', '#10b981', '#f8fafc'][Math.floor(Math.random() * 4)],
            backgroundColor: 'transparent',
            rotateRatio: 0
        });
    }
}
