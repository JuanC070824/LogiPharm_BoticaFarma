// src/services/reporteService.ts
import { API_BASE_URL } from '../config/api';
const API_URL_VENTAS = `${API_BASE_URL}boticafarma/ventas`;
// const API_URL_VENTAS = 'http://localhost:8080/boticafarma/ventas';

// MODIFICACIÓN CLAVE: Ahora acepta el parámetro 'tipo'
export const descargarReporteVentas = async (tipo: 'diario' | 'mensual'): Promise<void> => {
    const token = localStorage.getItem('token');

    // AQUÍ ES DONDE OCURRE EL CAMBIO DE URL
    const endpoint = tipo === 'diario' ? 'diarias/pdf' : 'mensuales/pdf';

    try {
        const response = await fetch(`${API_URL_VENTAS}/${endpoint}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error('Error al generar el reporte PDF');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');

    } catch (error) {
        console.error("Error descargando el PDF:", error);
        alert("No se pudo generar el reporte PDF.");
    }
};

export const obtenerDatosGrafico = async (tipo: 'diario' | 'mensual') => {
    const token = localStorage.getItem('token');
    const endpoint = tipo === 'diario' ? 'diarias/json' : 'mensuales/json';

    try {
        const response = await fetch(`${API_URL_VENTAS}/${endpoint}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) throw new Error('Error al obtener datos del gráfico');

        return await response.json();

    } catch (error) {
        console.error("Error obteniendo datos JSON:", error);
        throw error;
    }
};