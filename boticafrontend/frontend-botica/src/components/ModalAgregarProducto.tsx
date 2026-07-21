import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { actualizarProducto, crearProducto } from "@/services/inventarioService";
import type { Categoria, Marca, Producto } from "@/types/inventario";
import { X } from "lucide-react";
import { useEffect, useState } from "react";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
  producto: Producto | null;
  categorias: Categoria[];
  marcas: Marca[];
}

export const ModalAgregarProducto = ({
  open,
  onClose,
  onSuccess,
  producto,
  categorias = [],
  marcas = [],
}: Props) => {
  const [nombre, setNombre] = useState("");
  const [precio, setPrecio] = useState("");
  const [idCategoria, setIdCategoria] = useState("");
  const [idMarca, setIdMarca] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (producto) {
      setNombre(producto.nombre_producto || "");
      setPrecio(producto.precio ? producto.precio.toString() : "");
      setIdCategoria(producto.idCategoria ? producto.idCategoria.toString() : "");
      setIdMarca(producto.idMarca ? producto.idMarca.toString() : "");
    } else {
      limpiarFormulario();
    }
  }, [producto, open]);

  const limpiarFormulario = () => {
    setNombre("");
    setPrecio("");
    setIdCategoria("");
    setIdMarca("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = {
        nombre_producto: nombre,
        precio: parseFloat(precio),
        stock: 0, // El stock inicial se gestiona con lotes
        idCategoria: parseInt(idCategoria),
        idMarca: parseInt(idMarca),
      };

      let response;
      if (producto) {
        response = await actualizarProducto(producto.idProducto, data);
      } else {
        response = await crearProducto(data);
      }

      // Si el backend responde array u objeto sin wrap 'success'
      if (response && (response.success || response.idProducto)) {
        alert(producto ? "Producto actualizado correctamente" : "Producto creado correctamente");
        onSuccess();
        onClose();
        limpiarFormulario();
      } else {
        alert(response?.message || "Error al guardar el producto");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al guardar el producto");
    } finally {
      setLoading(false);
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-2xl font-bold">
            {producto ? "Editar Producto" : "Agregar Producto"}
          </h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Body */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Nombre del Producto */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Nombre del Producto: <span className="text-red-500">*</span>
            </label>
            <Input
              type="text"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              placeholder="Ej: Paracetamol 500mg"
              required
            />
          </div>

          {/* Categoría y Marca */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-2">
                Categoría: <span className="text-red-500">*</span>
              </label>
              <select
                value={idCategoria}
                onChange={(e) => setIdCategoria(e.target.value)}
                className="w-full border rounded px-3 py-2 bg-white text-gray-800"
                required
              >
                <option value="">Seleccionar...</option>
                {categorias.map((cat: any) => (
                  <option key={cat.idCategoria} value={cat.idCategoria}>
                    {cat.nombre_categoria || cat.nombreCategoria || cat.nombre}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">
                Marca: <span className="text-red-500">*</span>
              </label>
              <select
                value={idMarca}
                onChange={(e) => setIdMarca(e.target.value)}
                className="w-full border rounded px-3 py-2 bg-white text-gray-800"
                required
              >
                <option value="">Seleccionar...</option>
                {marcas.map((marca: any) => (
                  <option key={marca.idMarca} value={marca.idMarca}>
                    {marca.nombreMarca || marca.nombre_marca || marca.nombre}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Precio */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Precio (S/): <span className="text-red-500">*</span>
            </label>
            <Input
              type="number"
              step="0.01"
              value={precio}
              onChange={(e) => setPrecio(e.target.value)}
              placeholder="0.00"
              required
            />
          </div>

          {/* Nota informativa */}
          <div className="bg-blue-50 border border-blue-200 rounded p-3 text-sm text-blue-700">
            ℹ️ El stock se gestionará mediante lotes después de crear el producto.
          </div>

          {/* Botones */}
          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? "Guardando..." : "💾 Guardar Producto"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};