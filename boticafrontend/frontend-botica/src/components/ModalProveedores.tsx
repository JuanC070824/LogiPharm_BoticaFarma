import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
    actualizarProveedor,
    crearProveedor,
    eliminarProveedor,
    obtenerProveedores,
} from "@/services/inventarioService";
import type { Proveedor } from "@/types/inventario";
import { Edit, Plus, Trash2, X } from "lucide-react";
import { useEffect, useState } from "react";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export const ModalProveedores = ({ open, onClose, onSuccess }: Props) => {
  const [proveedores, setProveedores] = useState<Proveedor[]>([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editando, setEditando] = useState<Proveedor | null>(null);

  // Form fields
  const [nombre, setNombre] = useState("");
  const [direccion, setDireccion] = useState("");
  const [email, setEmail] = useState("");
  const [nombreError, setNombreError] = useState("");

  const nombreValido = (value: string) => {
    const trimmed = value.trim();
    const validNameRegex = /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü0-9\s]+$/;
    return trimmed.length > 0 && validNameRegex.test(trimmed);
  };

  useEffect(() => {
    if (open) {
      cargarProveedores();
    }
  }, [open]);

  const cargarProveedores = async () => {
    setLoading(true);
    try {
      const response = await obtenerProveedores();
      if (response.success) {
        setProveedores(response.proveedores || []);
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al cargar proveedores");
    } finally {
      setLoading(false);
    }
  };

  const limpiarFormulario = () => {
    setNombre("");
    setDireccion("");
    setEmail("");
    setEditando(null);
  };

  const handleNuevo = () => {
    limpiarFormulario();
    setShowForm(true);
  };

  const handleEditar = (proveedor: Proveedor) => {
    setNombre(proveedor.nombreProveedor);
    setDireccion(proveedor.direccion);
    setEmail(proveedor.email);
    setEditando(proveedor);
    setShowForm(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!nombreValido(nombre)) {
      setNombreError("El nombre solo puede contener letras, números y espacios.");
      return;
    }

    setLoading(true);

    try {
      const data = {
        nombreProveedor: nombre.trim(),
        direccion,
        email,
      };

      let response;
      if (editando) {
        response = await actualizarProveedor(editando.idProveedor, data);
      } else {
        response = await crearProveedor(data);
      }

      if (response.success) {
        alert(editando ? "Proveedor actualizado" : "Proveedor creado");
        cargarProveedores();
        setShowForm(false);
        limpiarFormulario();
        onSuccess();
      } else {
        alert(response.message || "Error al guardar");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al guardar proveedor");
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Eliminar este proveedor?")) return;

    try {
      const response = await eliminarProveedor(id);
      if (response.success) {
        alert("Proveedor eliminado");
        cargarProveedores();
        onSuccess();
      } else {
        alert(response.message || "Error al eliminar");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al eliminar proveedor");
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-3xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-2xl font-bold">Gestión de Proveedores</h2>
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
              <Button onClick={handleNuevo} className="mb-4">
                <Plus className="h-4 w-4 mr-2" />
                Agregar Proveedor
              </Button>

              {/* Tabla */}
              <div className="border rounded-lg overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="text-left p-3 font-semibold">Nombre</th>
                      <th className="text-left p-3 font-semibold">Email</th>
                      <th className="text-left p-3 font-semibold">Dirección</th>
                      <th className="text-center p-3 font-semibold">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {loading ? (
                      <tr>
                        <td colSpan={4} className="text-center py-8 text-gray-500">
                          Cargando...
                        </td>
                      </tr>
                    ) : proveedores.length === 0 ? (
                      <tr>
                        <td colSpan={4} className="text-center py-8 text-gray-500">
                          No hay proveedores
                        </td>
                      </tr>
                    ) : (
                      proveedores.map((prov) => (
                        <tr key={prov.idProveedor} className="border-t hover:bg-gray-50">
                          <td className="p-3 font-medium">{prov.nombreProveedor}</td>
                          <td className="p-3 text-gray-600">{prov.email}</td>
                          <td className="p-3 text-gray-600">{prov.direccion}</td>
                          <td className="p-3">
                            <div className="flex justify-center gap-2">
                              <button
                                onClick={() => handleEditar(prov)}
                                className="p-2 hover:bg-blue-50 rounded text-blue-600"
                              >
                                <Edit className="h-4 w-4" />
                              </button>
                              <button
                                onClick={() => handleEliminar(prov.idProveedor)}
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
                  Nombre del Proveedor: <span className="text-red-500">*</span>
                </label>
                <Input
                  value={nombre}
                  onChange={(e) => {
                    setNombre(e.target.value);
                    if (nombreError) setNombreError("");
                  }}
                  required
                />
                {nombreError ? (
                  <p className="text-sm text-red-600 mt-2">{nombreError}</p>
                ) : null}
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">
                  Dirección: <span className="text-red-500">*</span>
                </label>
                <Input
                  value={direccion}
                  onChange={(e) => setDireccion(e.target.value)}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">
                  Email: <span className="text-red-500">*</span>
                </label>
                <Input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
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