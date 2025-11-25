import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Rectangle } from 'recharts';
import { descargarReporteVentas, obtenerDatosGrafico } from '../services/reporteService';
import { Download, TrendingUp, AlertCircle, Calendar, CalendarDays } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface DataVenta {
  etiqueta: string;
  total: number;
}

// Definimos los tipos de vista disponibles
type TipoVista = 'mensual' | 'diario';

const Reportes = () => {
  const [data, setData] = useState<DataVenta[]>([]);
  const [loading, setLoading] = useState(true);
  const [loadingPdf, setLoadingPdf] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Estado para controlar qué estamos viendo
  const [vista, setVista] = useState<TipoVista>('mensual');

  // Efecto que se ejecuta al iniciar O cuando cambia la 'vista'
  useEffect(() => {
    const cargarDatos = async () => {
      try {
        setLoading(true);
        setError(null);

        // Pasamos la vista (diario/mensual) al servicio
        const resultados = await obtenerDatosGrafico(vista);

        // Protección: Aseguramos que sea un array antes de guardarlo
        if (Array.isArray(resultados)) {
          setData(resultados);
        } else {
          console.error("Datos inválidos:", resultados);
          setData([]);
        }
      } catch (err) {
        console.error("Error cargando gráfico", err);
        setError("No se pudieron cargar los datos del reporte.");
      } finally {
        setLoading(false);
      }
    };
    cargarDatos();
  }, [vista]); // <--- Importante: Dependencia [vista]

  const handleDescargarPDF = async () => {
    setLoadingPdf(true);
    try {
      // ¡AQUÍ ESTABA EL ERROR!
      // Antes estaba vacío: descargarReporteVentas()
      // Ahora le pasamos el estado actual: descargarReporteVentas(vista)
      await descargarReporteVentas(vista);
    } catch (e) {
      console.error(e);
    }
    setLoadingPdf(false);
  };

  // Cálculo seguro del total acumulado
  const totalAcumulado = data.reduce((acc, item) => acc + (Number(item.total) || 0), 0);

  return (
    <div className="p-8 bg-background min-h-full">

      {/* Encabezado y Controles */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
        <div>
          <h1 className="text-4xl font-bold text-foreground mb-2">Reportes Financieros</h1>
          <p className="text-muted-foreground">
            Resumen de ingresos {vista === 'mensual' ? 'por mes' : 'por día'}.
          </p>
        </div>

        <div className="flex items-center gap-3">

          {/* --- BOTONES DE CAMBIO DE VISTA --- */}
          <div className="bg-muted p-1 rounded-lg flex text-sm font-medium border border-border">
            <button
              onClick={() => setVista('diario')}
              className={`flex items-center gap-2 px-3 py-1.5 rounded-md transition-all ${vista === 'diario'
                ? 'bg-background text-foreground shadow-sm'
                : 'text-muted-foreground hover:text-foreground'
                }`}
            >
              <CalendarDays size={16} /> Diario
            </button>
            <button
              onClick={() => setVista('mensual')}
              className={`flex items-center gap-2 px-3 py-1.5 rounded-md transition-all ${vista === 'mensual'
                ? 'bg-background text-foreground shadow-sm'
                : 'text-muted-foreground hover:text-foreground'
                }`}
            >
              <Calendar size={16} /> Mensual
            </button>
          </div>

          {/* Botón PDF */}
          <button
            onClick={handleDescargarPDF}
            disabled={loadingPdf}
            className={`flex items-center gap-2 px-4 py-2 rounded-md text-white font-medium transition-all shadow-sm
              ${loadingPdf ? 'bg-blue-400 cursor-wait' : 'bg-blue-600 hover:bg-blue-700'}`}
          >
            {loadingPdf ? 'Generando...' : <><Download size={18} /> PDF</>}
          </button>
        </div>
      </div>

      {/* Manejo de Errores Visual */}
      {error ? (
        <div className="p-4 bg-destructive/10 text-destructive rounded-md flex items-center gap-2 border border-destructive/20">
          <AlertCircle size={20} /> {error}
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

          {/* --- GRÁFICO (Ocupa 2 columnas) --- */}
          <Card className="lg:col-span-2 border-border bg-card overflow-hidden">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-foreground">
                <TrendingUp className="text-blue-500" size={20} />
                Evolución {vista === 'mensual' ? 'Mensual' : 'Diaria'}
              </CardTitle>
            </CardHeader>
            <CardContent>
              {/* Contenedor con scroll horizontal por si hay muchos días */}
              <div className="flex justify-center w-full overflow-x-auto pb-2">
                {loading ? (
                  <div className="h-80 flex items-center justify-center text-muted-foreground">
                    Cargando gráfico...
                  </div>
                ) : (
                  /* Ancho dinámico: Si es 'diario', el gráfico crece para que no se apiñen las barras */
                  <BarChart
                    width={vista === 'diario' ? Math.max(800, data.length * 40) : 800}
                    height={350}
                    data={data}
                    margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
                  >
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
                    <XAxis
                      dataKey="etiqueta"
                      axisLine={false}
                      tickLine={false}
                      tick={{ fill: '#64748b', fontSize: 12 }}
                      dy={10}
                    />
                    <YAxis
                      axisLine={false}
                      tickLine={false}
                      tick={{ fill: '#64748b', fontSize: 12 }}
                      tickFormatter={(value) => `S/${value}`}
                    />
                    <Tooltip
                      cursor={{ fill: 'var(--muted)' }}
                      contentStyle={{ borderRadius: '8px', border: '1px solid #e2e8f0', color: '#000' }}
                    />
                    {/* Color cambia: Azul para mes, Verde esmeralda para día */}
                    <Bar
                      dataKey="total"
                      fill={vista === 'mensual' ? "#3b82f6" : "#10b981"}
                      radius={[4, 4, 0, 0]}
                      barSize={vista === 'mensual' ? 50 : 25}
                      activeBar={<Rectangle fill={vista === 'mensual' ? "#2563eb" : "#059669"} />}
                    />
                  </BarChart>
                )}
              </div>
            </CardContent>
          </Card>

          {/* --- TABLA DE DETALLE (Ocupa 1 columna) --- */}
          <Card className="border-border bg-card flex flex-col h-[500px]">
            <CardHeader>
              <CardTitle className="text-foreground">Detalle por {vista === 'mensual' ? 'Periodo' : 'Fecha'}</CardTitle>
            </CardHeader>
            <CardContent className="flex-1 overflow-hidden flex flex-col">
              <div className="overflow-y-auto flex-1 rounded-md border border-border">
                <table className="min-w-full divide-y divide-border">
                  <thead className="bg-muted/50 sticky top-0">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase">
                        {vista === 'mensual' ? 'Mes' : 'Día'}
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase">Ingreso</th>
                    </tr>
                  </thead>
                  <tbody className="bg-card divide-y divide-border">
                    {loading ? (
                      <tr><td colSpan={2} className="p-4 text-center text-sm text-muted-foreground">Cargando...</td></tr>
                    ) : data.length === 0 ? (
                      <tr><td colSpan={2} className="p-4 text-center text-sm text-muted-foreground">No hay datos</td></tr>
                    ) : data.map((item, index) => (
                      <tr key={index} className="hover:bg-muted/50 transition-colors">
                        <td className="px-4 py-3 text-sm text-foreground font-medium">
                          {item.etiqueta || "Sin fecha"}
                        </td>
                        <td className="px-4 py-3 text-sm text-right text-emerald-600 font-bold">
                          S/ {Number(item.total).toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Total Acumulado */}
              <div className="mt-4 p-4 bg-muted/30 rounded-lg border border-border shrink-0">
                <p className="text-sm text-muted-foreground mb-1">Total del Periodo</p>
                <p className="text-2xl font-bold text-foreground">
                  S/ {totalAcumulado.toFixed(2)}
                </p>
              </div>
            </CardContent>
          </Card>

        </div>
      )}
    </div>
  );
};

export default Reportes;