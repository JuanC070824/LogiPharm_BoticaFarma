import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
    crearMarca,
    eliminarMarca,
    obtenerMarcas,
    obtenerProveedores,
} from "@/services/inventarioService";
import type { Marca, Proveedor } from "@/types/inventario";
import { Plus, Trash2, X } from "lucide-react";
import { useEffect, useState } from "react";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export const ModalMarcas = ({ open, onClose, onSuccess }: Props) => {
  const [marcas, setMarcas] = useState<Marca[]>([]);
  const [proveedores, setProveedores] = useState<Proveedor[]>([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);

  const [nombreMarca, setNombreMarca] = useState("");
  const [idProveedor, setIdProveedor] = useState("");

  useEffect(() => {
    if (open) {
      cargarDatos();
    }
  }, [open]);

  const cargarDatos = async () => {
    setLoading(true);
    try {
      const [marcasRes, proveedoresRes] = await Promise.all([
        obtenerMarcas(),
        obtenerProveedores(),
      ]);

      if (marcasRes.success) setMarcas(marcasRes.marcas || []);
      if (proveedoresRes.success) setProveedores(proveedoresRes.proveedores || []);
    } catch (error) {
      console.error("Error:", error);
      alert("Error al cargar datos");
    } finally {
      setLoading(false);
    }
  };

  const limpiarFormulario = () => {
    setNombreMarca("");
    setIdProveedor("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = {
        nombreMarca,
        idProveedor: parseInt(idProveedor),
      };

      const response = await crearMarca(data);

      if (response.success) {
        alert("Marca creada correctamente");
        cargarDatos();
        setShowForm(false);
        limpiarFormulario();
        onSuccess();
      } else {
        alert(response.message || "Error al crear marca");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al crear marca");
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Eliminar esta marca?")) return;

    try {
      const response = await eliminarMarca(id);
      if (response.success) {
        alert("Marca eliminada");
        cargarDatos();
        onSuccess();
      } else {
        alert(response.message || "Error al eliminar");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al eliminar marca");
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-3xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-2xl font-bold">Gestión de Marcas</h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Body */}
        <div className="p-6">
          {!showForm ? (
            <>
              <Button onClick={() => setShowForm(true)} className="mb-4">
                <Plus className="h-4 w-4 mr-2" />
                Agregar Marca
              </Button>

              {/* Tabla */}
              <div className="border rounded-lg overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="text-left p-3 font-semibold">Marca</th>
                      <th className="text-left p-3 font-semibold">Proveedor</th>
                      <th className="text-center p-3 font-semibold">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {loading ? (
                      <tr>
                        <td colSpan={3} className="text-center py-8 text-gray-500">
                          Cargando...
                        </td>
                      </tr>
                    ) : marcas.length === 0 ? (
                      <tr>
                        <td colSpan={3} className="text-center py-8 text-gray-500">
                          No hay marcas
                        </td>
                      </tr>
                    ) : (
                      marcas.map((marca) => (
                        <tr key={marca.idMarca} className="border-t hover:bg-gray-50">
                          <td className="p-3 font-medium">{marca.nombreMarca}</td>
                          <td className="p-3 text-gray-600">{marca.nombreProveedor}</td>
                          <td className="p-3">
                            <div className="flex justify-center gap-2">
                              <button
                                onClick={() => handleEliminar(marca.idMarca)}
                                className="p-2 hover:bg-red-50 rounded text-red-600"
                              >
                                <Trash2 className="h-4 w-4" />
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </>
          ) : (
            /* Formulario */
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">
                  Nombre de la Marca: <span className="text-red-500">*</span>
                </label>
                <Input
                  value={nombreMarca}
                  onChange={(e) => setNombreMarca(e.target.value)}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">
                  Proveedor: <span className="text-red-500">*</span>
                </label>
                <select
                  value={idProveedor}
                  onChange={(e) => setIdProveedor(e.target.value)}
                  className="w-full border rounded px-3 py-2"
                  required
                >
                  <option value="">Seleccionar...</option>
                  {proveedores.map((prov) => (
                    <option key={prov.idProveedor} value={prov.idProveedor}>
                      {prov.nombreProveedor}
                    </option>
                  ))}
                </select>
              </div>

              <div className="flex justify-end gap-3 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => {
                    setShowForm(false);
                    limpiarFormulario();
                  }}
                >
                  Cancelar
                </Button>
                <Button type="submit" disabled={loading}>
                  {loading ? "Guardando..." : "💾 Guardar"}
                </Button>
              </div>
            </form>
          )}
        </div>

        {/* Footer */}
        {!showForm && (
          <div className="p-6 border-t flex justify-end">
            <Button variant="outline" onClick={onClose}>
              Cerrar
            </Button>
          </div>
        )}
      </div>
    </div>
  );
};