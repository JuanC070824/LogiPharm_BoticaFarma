import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { crearLote, obtenerLotesPorProducto } from "@/services/inventarioService";
import type { Producto } from "@/types/inventario";
import { Package, Plus, X } from "lucide-react";
import { useEffect, useState } from "react";

interface Lote {
  idLote: number;
  codigoLote: string;
  cantidadInicial: number;
  cantidadActual: number;
  fechaIngreso: string;
  fechaVencimiento: string;
  estadoLote: string;
  precioCompra: number;
}

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
  producto: Producto | null;
}

export const ModalLotes = ({ open, onClose, onSuccess, producto }: Props) => {
  const [lotes, setLotes] = useState<Lote[]>([]);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [cantidad, setCantidad] = useState("");
  const [fechaVencimiento, setFechaVencimiento] = useState("");
  const [precioCompra, setPrecioCompra] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (open && producto) {
      cargarLotes();
    }
  }, [open, producto]);

  const cargarLotes = async () => {
    try {
      const response = await obtenerLotesPorProducto(producto!.idProducto);
      if (response.success) {
        setLotes(response.lotes || []);
      }
    } catch (error) {
      console.error("Error al cargar lotes:", error);
      // No mostrar alerta si es error 403, solo en consola
    }
  };

  const handleAgregarLote = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Obtener el idUsuario del localStorage o del token
      const userId = localStorage.getItem('userId') || '1'; // Valor por defecto
      
      const response = await crearLote({
        idProducto: producto!.idProducto,
        idUsuario: parseInt(userId),
        cantidad: parseInt(cantidad),
        fechaVencimiento: fechaVencimiento,
        precioCompra: parseFloat(precioCompra),
      });

      if (response.success) {
        alert("Lote agregado correctamente");
        setCantidad("");
        setFechaVencimiento("");
        setPrecioCompra("");
        setMostrarFormulario(false);
        cargarLotes();
        onSuccess();
      } else {
        alert(response.message || "Error al agregar lote");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al agregar lote");
    } finally {
      setLoading(false);
    }
  };

  if (!open || !producto) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b">
          <div>
            <h2 className="text-2xl font-bold">Lotes de {producto.nombre_producto}</h2>
            <p className="text-sm text-gray-600 mt-1">
              Stock total: {producto.stock} unidades
            </p>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Body */}
        <div className="p-6 space-y-4">
          {/* Botón agregar lote */}
          {!mostrarFormulario && (
            <Button onClick={() => setMostrarFormulario(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Agregar Lote
            </Button>
          )}

          {/* Formulario agregar lote */}
          {mostrarFormulario && (
            <Card>
              <CardContent className="pt-6">
                <form onSubmit={handleAgregarLote} className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium mb-2">
                        Cantidad: <span className="text-red-500">*</span>
                      </label>
                      <Input
                        type="number"
                        value={cantidad}
                        onChange={(e) => setCantidad(e.target.value)}
                        placeholder="100"
                        required
                        min="1"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium mb-2">
                        Precio de Compra (S/): <span className="text-red-500">*</span>
                      </label>
                      <Input
                        type="number"
                        step="0.01"
                        value={precioCompra}
                        onChange={(e) => setPrecioCompra(e.target.value)}
                        placeholder="15.50"
                        required
                        min="0.01"
                      />
                    </div>

                    <div className="col-span-2">
                      <label className="block text-sm font-medium mb-2">
                        Fecha de Vencimiento: <span className="text-red-500">*</span>
                      </label>
                      <Input
                        type="date"
                        value={fechaVencimiento}
                        onChange={(e) => setFechaVencimiento(e.target.value)}
                        required
                        min={new Date().toISOString().split('T')[0]}
                      />
                    </div>
                  </div>

                  <div className="flex justify-end gap-3">
                    <Button
                      type="button"
                      variant="outline"
                      onClick={() => setMostrarFormulario(false)}
                    >
                      Cancelar
                    </Button>
                    <Button type="submit" disabled={loading}>
                      {loading ? "Guardando..." : "Guardar Lote"}
                    </Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          )}

          {/* Tabla de lotes */}
          <Card>
            <CardContent className="pt-6">
              <h3 className="font-semibold mb-4">Lotes Disponibles</h3>
              {lotes.length === 0 ? (
                <div className="text-center py-8 text-gray-500">
                  <Package className="h-12 w-12 mx-auto mb-2 opacity-50" />
                  <p>No hay lotes registrados o no tienes permisos para verlos</p>
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-3">Código</th>
                        <th className="text-left p-3">Cantidad Inicial</th>
                        <th className="text-left p-3">Cantidad Actual</th>
                        <th className="text-left p-3">Precio Compra</th>
                        <th className="text-left p-3">Ingreso</th>
                        <th className="text-left p-3">Vencimiento</th>
                        <th className="text-left p-3">Estado</th>
                      </tr>
                    </thead>
                    <tbody>
                      {lotes.map((lote) => (
                        <tr key={lote.idLote} className="border-b hover:bg-gray-50">
                          <td className="p-3 font-medium">{lote.codigoLote}</td>
                          <td className="p-3">{lote.cantidadInicial}</td>
                          <td className="p-3 font-semibold">{lote.cantidadActual}</td>
                          <td className="p-3">S/ {lote.precioCompra?.toFixed(2)}</td>
                          <td className="p-3">{new Date(lote.fechaIngreso).toLocaleDateString()}</td>
                          <td className="p-3">{new Date(lote.fechaVencimiento).toLocaleDateString()}</td>
                          <td className="p-3">
                            <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                              lote.estadoLote === 'DISPONIBLE' 
                                ? 'bg-green-100 text-green-700'
                                : lote.estadoLote === 'PROXIMO_A_VENCER'
                                ? 'bg-yellow-100 text-yellow-700'
                                : lote.estadoLote === 'PROXIMO_A_TERMINAR'
                                ? 'bg-orange-100 text-orange-700'
                                : 'bg-red-100 text-red-700'
                            }`}>
                              {lote.estadoLote.replace('_', ' ')}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}