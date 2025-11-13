import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  crearCategoria,
  eliminarCategoria,
  obtenerCategorias,
} from "@/services/inventarioService";
import type { Categoria } from "@/types/inventario";
import { Plus, Trash2, X } from "lucide-react";
import { useEffect, useState } from "react";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export const ModalCategorias = ({ open, onClose, onSuccess }: Props) => {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [nombreCategoria, setNombreCategoria] = useState("");

  useEffect(() => {
    if (open) {
      cargarCategorias();
    }
  }, [open]);

  const cargarCategorias = async () => {
    setLoading(true);
    try {
      const response = await obtenerCategorias();
      if (response.success) {
        setCategorias(response.categorias || []);
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al cargar categorías");
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = { nombre_categoria: nombreCategoria };
      const response = await crearCategoria(data);

      if (response.success) {
        alert("Categoría creada correctamente");
        cargarCategorias();
        setShowForm(false);
        setNombreCategoria("");
        onSuccess();
      } else {
        alert(response.message || "Error al crear categoría");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al crear categoría");
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Eliminar esta categoría?")) return;

    try {
      const response = await eliminarCategoria(id);
      if (response.success) {
        alert("Categoría eliminada");
        cargarCategorias();
        onSuccess();
      } else {
        alert(response.message || "Error al eliminar");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al eliminar categoría");
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-2xl font-bold">Gestión de Categorías</h2>
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
                Agregar Categoría
              </Button>

              {/* Lista */}
              <div className="border rounded-lg overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="text-left p-3 font-semibold">ID</th>
                      <th className="text-left p-3 font-semibold">Nombre</th>
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
                    ) : categorias.length === 0 ? (
                      <tr>
                        <td colSpan={3} className="text-center py-8 text-gray-500">
                          No hay categorías
                        </td>
                      </tr>
                    ) : (
                      categorias.map((cat) => (
                        <tr key={cat.idCategoria} className="border-t hover:bg-gray-50">
                          <td className="p-3">{cat.idCategoria}</td>
                          <td className="p-3 font-medium">{cat.nombre_categoria}</td>
                          <td className="p-3">
                            <div className="flex justify-center gap-2">
                              <button
                                onClick={() => handleEliminar(cat.idCategoria)}
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
                  Nombre de la Categoría: <span className="text-red-500">*</span>
                </label>
                <Input
                  value={nombreCategoria}
                  onChange={(e) => setNombreCategoria(e.target.value)}
                  placeholder="Ej: Analgésicos"
                  required
                />
              </div>

              <div className="flex justify-end gap-3 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => {
                    setShowForm(false);
                    setNombreCategoria("");
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