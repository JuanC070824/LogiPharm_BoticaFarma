import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { buscarProductos, obtenerProductos } from "@/services/inventarioService";
import { procesarVenta } from "@/services/ventasService";
import type { Producto } from "@/types/inventario";
import { Minus, Plus, Search, ShoppingCart, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

interface CartItem {
  idProducto: number;
  nombre: string;
  stock: number;
  precio: number;
  quantity: number;
}

interface ClienteData {
  idCliente?: number;
  nombre: string;
  apellidoPat: string;
  apellidoMat: string;
  dni: string;
  ruc: string;
}

const Ventas = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [productos, setProductos] = useState<Producto[]>([]);
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [metodoPago, setMetodoPago] = useState<string>("EFECTIVO");
  const [processingVenta, setProcessingVenta] = useState(false);
  
  // NUEVOS ESTADOS
  const [tipoCliente, setTipoCliente] = useState<string>("COMUN");
  const [tipoComprobante, setTipoComprobante] = useState<string>("BOLETA");
  const [clienteData, setClienteData] = useState<ClienteData>({
    nombre: "",
    apellidoPat: "",
    apellidoMat: "",
    dni: "",
    ruc: ""
  });

  useEffect(() => {
    cargarProductos();
  }, []);

  const cargarProductos = async () => {
    setLoading(true);
    try {
      const response = await obtenerProductos(0, 100);
      if (response.success) {
        setProductos(response.productos || []);
      }
    } catch (error) {
      console.error("Error al cargar productos:", error);
      toast.error("No se pudieron cargar los productos");
    } finally {
      setLoading(false);
    }
  };

  const handleBuscar = async () => {
    if (!searchTerm.trim()) {
      cargarProductos();
      return;
    }

    setLoading(true);
    try {
      const response = await buscarProductos(searchTerm, undefined, undefined, 0, 100);
      if (response.success) {
        setProductos(response.productos || []);
      }
    } catch (error) {
      console.error("Error al buscar:", error);
      toast.error("Error al buscar productos");
    } finally {
      setLoading(false);
    }
  };

  const addToCart = (producto: Producto) => {
    const existingItem = cart.find(item => item.idProducto === producto.idProducto);
    
    if (existingItem) {
      if (existingItem.quantity < producto.stock) {
        setCart(cart.map(item =>
          item.idProducto === producto.idProducto
            ? { ...item, quantity: item.quantity + 1 }
            : item
        ));
      } else {
        toast.error("Stock insuficiente");
      }
    } else {
      if (producto.stock > 0) {
        setCart([...cart, {
          idProducto: producto.idProducto,
          nombre: producto.nombre_producto,
          stock: producto.stock,
          precio: producto.precio,
          quantity: 1
        }]);
      } else {
        toast.error("Sin stock disponible");
      }
    }
  };

  const updateQuantity = (idProducto: number, newQuantity: number) => {
    const item = cart.find(i => i.idProducto === idProducto);
    if (!item) return;

    if (newQuantity <= 0) {
      removeFromCart(idProducto);
      return;
    }

    if (newQuantity > item.stock) {
      toast.error("Stock insuficiente");
      return;
    }

    setCart(cart.map(cartItem =>
      cartItem.idProducto === idProducto
        ? { ...cartItem, quantity: newQuantity }
        : cartItem
    ));
  };

  const removeFromCart = (idProducto: number) => {
    setCart(cart.filter(item => item.idProducto !== idProducto));
  };

  const subtotal = cart.reduce((sum, item) => sum + (item.precio * item.quantity), 0);
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  const processSale = async () => {
    if (cart.length === 0) {
      toast.error("Carrito vacío");
      return;
    }

    // Validaciones para factura
    if (tipoComprobante === "FACTURA") {
      if (tipoCliente === "COMUN") {
        toast.error("Para factura debe seleccionar Cliente Registrado");
        return;
      }
      if (!clienteData.ruc || clienteData.ruc.length !== 11) {
        toast.error("El RUC debe tener 11 dígitos para factura");
        return;
      }
    }

    // Validaciones para cliente registrado
    if (tipoCliente === "REGISTRADO") {
      if (!clienteData.nombre.trim() || !clienteData.apellidoPat.trim() || !clienteData.dni.trim()) {
        toast.error("Complete todos los datos del cliente");
        return;
      }
      if (clienteData.dni.length !== 8) {
        toast.error("El DNI debe tener 8 dígitos");
        return;
      }
    }

    setProcessingVenta(true);

    try {
      const userId = localStorage.getItem('userId') || '1';
      
      // Preparar datos del cliente según el tipo
      let clientePayload = null;
      if (tipoCliente === "REGISTRADO") {
        clientePayload = {
          idCliente: clienteData.idCliente || null, // null para nuevo cliente
          nombre: clienteData.nombre,
          apellidoPat: clienteData.apellidoPat,
          apellidoMat: clienteData.apellidoMat,
          dni: clienteData.dni,
          ruc: tipoComprobante === "FACTURA" ? clienteData.ruc : ""
        };
      }

      const ventaData = {
        idUsuario: parseInt(userId),
        metodoPago: metodoPago,
        tipoComprobante: tipoComprobante,
        cliente: clientePayload,
        detalles: cart.map(item => ({
          idProducto: item.idProducto,
          cantidad: item.quantity,
          precioUnitario: item.precio
        }))
      };

      const response = await procesarVenta(ventaData);

      if (response.success) {
        toast.success(`✅ Venta procesada correctamente (Total: S/ ${total.toFixed(2)})`);
        setCart([]);
        // Limpiar datos del cliente
        setClienteData({
          nombre: "",
          apellidoPat: "",
          apellidoMat: "",
          dni: "",
          ruc: ""
        });
        setTipoCliente("COMUN");
        setTipoComprobante("BOLETA");
        cargarProductos(); // Recargar para actualizar stocks
      } else {
        toast.error("Error al procesar la venta")
      }
    } catch (error: any) {
      console.error("Error:", error);
      toast.error(error.response?.data?.message || "Error al procesar la venta")
    } finally {
      setProcessingVenta(false);
    }
  };

  const filteredProducts = productos.filter(p => 
    p.nombre_producto.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8">
      <h1 className="text-4xl font-bold mb-8">Venta</h1>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex gap-3">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                <Input
                  placeholder="Buscar producto por nombre..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && handleBuscar()}
                  className="pl-10"
                />
              </div>
              <Button onClick={handleBuscar}>
                <Search className="h-4 w-4 mr-2" />
                Buscar
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-3 max-h-[600px] overflow-y-auto">
              {loading ? (
                <p className="text-center py-8 text-gray-500">Cargando productos...</p>
              ) : filteredProducts.length === 0 ? (
                <p className="text-center py-8 text-gray-500">No se encontraron productos</p>
              ) : (
                filteredProducts.map((producto) => (
                  <div
                    key={producto.idProducto}
                    className="flex justify-between items-center p-4 bg-gray-50 rounded-lg border hover:border-blue-300 transition-colors"
                  >
                    <div>
                      <p className="font-semibold">{producto.nombre_producto}</p>
                      <p className="text-sm text-gray-600">
                        Stock: {producto.stock} | Precio: S/ {producto.precio.toFixed(2)}
                      </p>
                      <p className="text-xs text-gray-500">
                        {producto.nombreCategoria} - {producto.nombreMarca}
                      </p>
                    </div>
                    <Button
                      onClick={() => addToCart(producto)}
                      disabled={producto.stock === 0}
                      className={producto.stock === 0 ? "opacity-50 cursor-not-allowed" : ""}
                    >
                      <ShoppingCart className="h-4 w-4 mr-2" />
                      Agregar
                    </Button>
                  </div>
                ))
              )}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Detalle de Venta</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* NUEVO: Tipo de Cliente */}
            <div>
              <label className="block text-sm font-medium mb-2">Tipo de Cliente:</label>
              <select
                value={tipoCliente}
                onChange={(e) => setTipoCliente(e.target.value)}
                className="w-full border rounded px-3 py-2"
              >
                <option value="COMUN">Cliente Común</option>
                <option value="REGISTRADO">Cliente Registrado</option>
              </select>
            </div>

            {/* NUEVO: Tipo de Comprobante */}
            <div>
              <label className="block text-sm font-medium mb-2">Tipo de Comprobante:</label>
              <select
                value={tipoComprobante}
                onChange={(e) => setTipoComprobante(e.target.value)}
                className="w-full border rounded px-3 py-2"
              >
                <option value="BOLETA">Boleta</option>
                <option value="FACTURA">Factura</option>
              </select>
            </div>

            {/* NUEVO: Campos para Cliente Registrado */}
            {tipoCliente === "REGISTRADO" && (
              <div className="space-y-3 p-3 bg-gray-50 rounded-lg border">
                <h4 className="font-medium text-sm">Datos del Cliente</h4>
                
                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="block text-xs font-medium mb-1">Nombre</label>
                    <Input
                      value={clienteData.nombre}
                      onChange={(e) => setClienteData({...clienteData, nombre: e.target.value})}
                      placeholder="Nombre"
                      className="text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium mb-1">Apellido Paterno</label>
                    <Input
                      value={clienteData.apellidoPat}
                      onChange={(e) => setClienteData({...clienteData, apellidoPat: e.target.value})}
                      placeholder="Apellido Paterno"
                      className="text-sm"
                    />
                  </div>
                </div>
                
                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="block text-xs font-medium mb-1">Apellido Materno</label>
                    <Input
                      value={clienteData.apellidoMat}
                      onChange={(e) => setClienteData({...clienteData, apellidoMat: e.target.value})}
                      placeholder="Apellido Materno"
                      className="text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium mb-1">DNI</label>
                    <Input
                      value={clienteData.dni}
                      onChange={(e) => setClienteData({...clienteData, dni: e.target.value.replace(/\D/g, '')})}
                      placeholder="DNI"
                      maxLength={8}
                      className="text-sm"
                    />
                  </div>
                </div>

                {/* NUEVO: Campo RUC solo para factura */}
                {tipoComprobante === "FACTURA" && (
                  <div>
                    <label className="block text-xs font-medium mb-1">RUC</label>
                    <Input
                      value={clienteData.ruc}
                      onChange={(e) => setClienteData({...clienteData, ruc: e.target.value.replace(/\D/g, '')})}
                      placeholder="RUC (11 dígitos)"
                      maxLength={11}
                      className="text-sm"
                    />
                  </div>
                )}
              </div>
            )}

            {/* Método de pago (existente) */}
            <div>
              <label className="block text-sm font-medium mb-2">Método de Pago:</label>
              <select
                value={metodoPago}
                onChange={(e) => setMetodoPago(e.target.value)}
                className="w-full border rounded px-3 py-2"
              >
                <option value="EFECTIVO">Efectivo</option>
                <option value="TARJETA">Tarjeta</option>
                <option value="YAPE">Yape</option>
                <option value="PLIN">Plin</option>
              </select>
            </div>

            {/* Carrito (existente) */}
            <div className="space-y-3 min-h-[200px] max-h-[300px] overflow-y-auto">
              {cart.length === 0 ? (
                <p className="text-center py-8 text-gray-500">Carrito vacío</p>
              ) : (
                cart.map((item) => (
                  <div
                    key={item.idProducto}
                    className="flex justify-between items-start p-3 bg-gray-50 rounded-lg border"
                  >
                    <div className="flex-1">
                      <p className="font-medium text-sm">{item.nombre}</p>
                      <div className="flex items-center gap-2 mt-2">
                        <Button
                          variant="outline"
                          size="icon"
                          className="h-6 w-6"
                          onClick={() => updateQuantity(item.idProducto, item.quantity - 1)}
                        >
                          <Minus className="h-3 w-3" />
                        </Button>
                        <span className="text-sm font-medium w-8 text-center">{item.quantity}</span>
                        <Button
                          variant="outline"
                          size="icon"
                          className="h-6 w-6"
                          onClick={() => updateQuantity(item.idProducto, item.quantity + 1)}
                        >
                          <Plus className="h-3 w-3" />
                        </Button>
                        <span className="text-xs text-gray-500">x S/ {item.precio.toFixed(2)}</span>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      <p className="font-semibold">
                        S/ {(item.quantity * item.precio).toFixed(2)}
                      </p>
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => removeFromCart(item.idProducto)}
                        className="h-8 w-8 text-red-600 hover:text-red-700 hover:bg-red-50"
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* Totales (existente) */}
            <div className="space-y-2 pt-4 border-t">
              <div className="flex justify-between">
                <span>Subtotal:</span>
                <span className="font-semibold">S/ {subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span>IGV (18%):</span>
                <span className="font-semibold">S/ {igv.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-xl font-bold pt-2 border-t">
                <span>Total:</span>
                <span>S/ {total.toFixed(2)}</span>
              </div>
            </div>

            <Button
              onClick={processSale}
              disabled={processingVenta || cart.length === 0}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white"
              size="lg"
            >
              <ShoppingCart className="h-5 w-5 mr-2" />
              {processingVenta ? "Procesando..." : "Procesar Venta"}
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Ventas;