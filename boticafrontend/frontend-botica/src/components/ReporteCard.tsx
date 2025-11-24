import { useState } from 'react';
// CAMBIO AQUÍ: Solo subimos un nivel (..) porque estamos en src/components
import { descargarReporteVentas } from '../services/reporteService';

const ReporteCard = () => {
    const [loading, setLoading] = useState(false);

    const handleGenerarReporte = async () => {
        setLoading(true);
        await descargarReporteVentas();
        setLoading(false);
    };

    return (
        <div className="bg-white shadow-lg rounded-lg p-6 border border-gray-200 max-w-sm hover:shadow-xl transition-shadow duration-300">
            <div className="flex items-center space-x-4 mb-4">
                <div className="p-3 bg-blue-100 rounded-full text-blue-600">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8">
                        <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z" />
                    </svg>
                </div>
                <div>
                    <h3 className="text-xl font-bold text-gray-800">Reporte de Ventas</h3>
                    <p className="text-sm text-gray-500">Mensual y Gráfico</p>
                </div>
            </div>

            <p className="text-gray-600 mb-6 text-sm">
                Genera un PDF con el total de ventas agrupadas por mes y visualiza la evolución de ingresos.
            </p>

            <button
                onClick={handleGenerarReporte}
                disabled={loading}
                className={`w-full flex justify-center items-center px-4 py-2 rounded-md text-white font-medium transition-all
          ${loading
                        ? 'bg-blue-400 cursor-wait'
                        : 'bg-blue-600 hover:bg-blue-700 shadow-md hover:shadow-lg'
                    }`}
            >
                {loading ? (
                    <>Generando...</>
                ) : (
                    'Descargar PDF'
                )}
            </button>
        </div>
    );
};

export default ReporteCard;