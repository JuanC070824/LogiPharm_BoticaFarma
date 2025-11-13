import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  buscarProductos,
  eliminarProducto,
  obtenerCategorias,
  obtenerMarcas,
  obtenerProductos,
} from "@/services/inventarioService";
import type { Categoria, Marca, Producto } from "@/types/inventario";
import { Edit, Package, Plus, Search, Store, Tag, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { ModalAgregarProducto } from "../components/ModalAgregarProducto";
import { ModalCategorias } from "../components/ModalCategorias";
import { ModalLotes } from "../components/ModalLotes";
import { ModalMarcas } from "../components/ModalMarcas";
import { ModalProveedores } from "../components/ModalProveedores";

const Inventario = () => {
  const [productos, setProductos] = useState<Producto[]>([]);
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [marcas, setMarcas] = useState<Marca[]>([]);
  const [loading, setLoading] = useState(false);

  // Filtros
  const [busqueda, setBusqueda] = useState("");
  const [categoriaFiltro, setCategoriaFiltro] = useState<string>("");
  const [marcaFiltro, setMarcaFiltro] = useState<string>("");

  // Paginación
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Modales
  const [modalProductoOpen, setModalProductoOpen] = useState(false);
  const [modalProveedoresOpen, setModalProveedoresOpen] = useState(false);
  const [modalMarcasOpen, setModalMarcasOpen] = useState(false);
  const [modalCategoriasOpen, setModalCategoriasOpen] = useState(false);
  const [productoEditar, setProductoEditar] = useState<Producto | null>(null);

  const [modalLotesOpen, setModalLotesOpen] = useState(false);
  const [productoParaLotes, setProductoParaLotes] = useState<Producto | null>(null);
  const handleVerLotes = (producto: Producto) => {
    setProductoParaLotes(producto);
    setModalLotesOpen(true);
  };
  useEffect(() => {
    cargarDatos();
  }, [currentPage]);

  const cargarDatos = async () => {
    setLoading(true);
    try {
      const [productosRes, categoriasRes, marcasRes] = await Promise.all([
        obtenerProductos(currentPage, 10),
        obtenerCategorias(),
        obtenerMarcas(),
      ]);

      if (productosRes.success) {
        setProductos(productosRes.productos || []);
        setTotalPages(productosRes.totalPages || 0);
      }
      if (categoriasRes.success) setCategorias(categoriasRes.categorias || []);
      if (marcasRes.success) setMarcas(marcasRes.marcas || []);
    } catch (error) {
      console.error("Error al cargar datos: tabla vacía", error);
      alert("No se pudieron cargar los datos");
    } finally {
      setLoading(false);
    }
  };

  const handleBuscar = async () => {
    setLoading(true);
    try {
      const idCategoria = categoriaFiltro && categoriaFiltro !== "all" ? parseInt(categoriaFiltro) : undefined;
      const idMarca = marcaFiltro && marcaFiltro !== "all" ? parseInt(marcaFiltro) : undefined;

      const response = await buscarProductos(
        busqueda || undefined,
        idCategoria,
        idMarca,
        0,
        10
      );

      if (response.success) {
        setProductos(response.productos || []);
        setTotalPages(response.totalPages || 0);
        setCurrentPage(0);
      }
    } catch (error) {
      console.error("Error al buscar:", error);
      alert("Error al buscar productos");
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Estás seguro de eliminar este producto?")) return;

    try {
      const response = await eliminarProducto(id);
      if (response.success) {
        alert("Producto eliminado correctamente");
        cargarDatos();
      } else {
        alert(response.message || "No se pudo eliminar el producto");
      }
    } catch (error) {
      console.error("Error al eliminar:", error);
      alert("Error al eliminar el producto");
    }
  };

  const handleEditar = (producto: Producto) => {
    setProductoEditar(producto);
    setModalProductoOpen(true);
  };

  const handleNuevoProducto = () => {
    setProductoEditar(null);
    setModalProductoOpen(true);
  };

  const getStockBadge = (stock: number) => {
    if (stock === 0) {
      return <span className="px-3 py-1 rounded-full text-xs font-medium bg-red-100 text-red-700">Sin Stock</span>;
    } else if (stock < 10) {
      return <span className="px-3 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-700">Stock Bajo</span>;
    } else {
      return <span className="px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700">En Stock</span>;
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-4xl font-bold mb-8"> Inventario</h1>

      {/* Barra de búsqueda */}
      <Card className="mb-6">
        <CardContent className="pt-6">
          <div className="flex gap-4 items-center">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="Buscar producto..."
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleBuscar()}
                className="pl-10"
              />
            </div>
            <Button onClick={handleBuscar}>
              <Search className="h-4 w-4 mr-2" />
              Buscar
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Filtros */}
      <Card className="mb-6">
        <CardContent className="pt-6">
          <p className="text-sm font-medium mb-3">Filtros:</p>
          <div className="flex gap-4">
            <select
              value={categoriaFiltro}
              onChange={(e) => setCategoriaFiltro(e.target.value)}
              className="border rounded px-3 py-2 w-[200px]"
            >
              <option value="all">Todas las categorías</option>
              {categorias.map((cat) => (
                <option key={cat.idCategoria} value={cat.idCategoria}>
                  {cat.nombre_categoria}
                </option>
              ))}
            </select>

            <select
              value={marcaFiltro}
              onChange={(e) => setMarcaFiltro(e.target.value)}
              className="border rounded px-3 py-2 w-[200px]"
            >
              <option value="all">Todas las marcas</option>
              {marcas.map((marca) => (
                <option key={marca.idMarca} value={marca.idMarca}>
                  {marca.nombreMarca}
                </option>
              ))}
            </select>
          </div>
        </CardContent>
      </Card>
      
      {/* Botones de acciones */}
      <div className="flex flex-wrap gap-3 mb-6">
        <Button onClick={handleNuevoProducto}>
          <Plus className="h-4 w-4 mr-2" />
          Agregar Producto
        </Button>
        <Button onClick={() => setModalProveedoresOpen(true)} variant="outline">
          <Store className="h-4 w-4 mr-2" />
          Proveedores
        </Button>
        <Button onClick={() => setModalMarcasOpen(true)} variant="outline">
          <Tag className="h-4 w-4 mr-2" />
          Marcas
        </Button>
        <Button onClick={() => setModalCategoriasOpen(true)} variant="outline">
          <Package className="h-4 w-4 mr-2" />
          Categorías
        </Button>
      </div>


      {/* Tabla de productos */}
      <Card>
        <CardContent className="pt-6">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left p-3 font-semibold">ID</th>
                  <th className="text-left p-3 font-semibold">Nombre</th>
                  <th className="text-left p-3 font-semibold">Categoría</th>
                  <th className="text-left p-3 font-semibold">Marca</th>
                  <th className="text-left p-3 font-semibold">Precio</th>
                  <th className="text-left p-3 font-semibold">Stock</th>
                  <th className="text-center p-3 font-semibold">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan={7} className="text-center py-8 text-gray-500">
                      Cargando...
                    </td>
                  </tr>
                ) : productos.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="text-center py-8 text-gray-500">
                      No hay productos registrados
                    </td>
                  </tr>
                ) : (
                  productos.map((producto) => (
                    <tr key={producto.idProducto} className="border-b hover:bg-gray-50">
                      <td className="p-3">{producto.idProducto}</td>
                      <td className="p-3 font-medium">{producto.nombre_producto}</td>
                      <td className="p-3 text-gray-600">{producto.nombreCategoria}</td>
                      <td className="p-3 text-gray-600">{producto.nombreMarca}</td>
                      <td className="p-3">S/ {producto.precio.toFixed(2)}</td>
                      <td className="p-3">
                        <div className="flex flex-col gap-2">
                          <span className="font-medium">{producto.stock} unidades</span>
                          {getStockBadge(producto.stock)}
                        </div>
                      </td>
                      <td className="p-3">
                        <div className="flex justify-center gap-2">
                          <button
                            onClick={() => handleVerLotes(producto)}  // ← NUEVO
                            className="p-2 hover:bg-green-50 rounded text-green-600"
                            title="Ver Lotes"
                          >
                            <Package className="h-4 w-4" />  {/* Usa el ícono Package */}
                          </button>
                          <button
                            onClick={() => handleEditar(producto)}
                            className="p-2 hover:bg-blue-50 rounded text-blue-600"
                          >
                            <Edit className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleEliminar(producto.idProducto)}
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
        </CardContent>
      </Card>

      {/* Paginación */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-6">
          <Button
            variant="outline"
            onClick={() => setCurrentPage((prev) => Math.max(0, prev - 1))}
            disabled={currentPage === 0}
          >
            Anterior
          </Button>
          <span className="flex items-center px-4 text-sm text-gray-600">
            Página {currentPage + 1} de {totalPages}
          </span>
          <Button
            variant="outline"
            onClick={() => setCurrentPage((prev) => Math.min(totalPages - 1, prev + 1))}
            disabled={currentPage === totalPages - 1}
          >
            Siguiente
          </Button>
        </div>
      )}

      {/* Modales */}
      <ModalAgregarProducto
        open={modalProductoOpen}
        onClose={() => {
          setModalProductoOpen(false);
          setProductoEditar(null);
        }}
        onSuccess={cargarDatos}
        producto={productoEditar}
        categorias={categorias}
        marcas={marcas}
      />

      <ModalProveedores
        open={modalProveedoresOpen}
        onClose={() => setModalProveedoresOpen(false)}
        onSuccess={cargarDatos}
      />

      <ModalMarcas
        open={modalMarcasOpen}
        onClose={() => setModalMarcasOpen(false)}
        onSuccess={cargarDatos}
      />

      <ModalCategorias
        open={modalCategoriasOpen}
        onClose={() => setModalCategoriasOpen(false)}
        onSuccess={cargarDatos}
      />
      <ModalLotes
        open={modalLotesOpen}
        onClose={() => {
          setModalLotesOpen(false);
          setProductoParaLotes(null);
        }}
        onSuccess={cargarDatos}
        producto={productoParaLotes}
      />
    </div>
  );
};

export default Inventario;