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
//LINK SUPREMO BOTICA FARMA
const BOTICA_LINK = "https://www.google.com/maps/place/Botica+Farma,+Av.+Universitaria+653,+San+Mart%C3%ADn+de+Porres+15103/data=!4m2!3m1!1s0x9105ceea709046fb:0xc883af3a0c47d2cf?utm_source=mstt_1&entry=gps";

const extraerCoordenadas = (link: string): string | null => {
  const match = link.match(/@(-?\d+\.\d+),(-?\d+\.\d+)/);
  if (match) {
    return `${match[1]},${match[2]}`;
  }
  return null;
};


const Ventas = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [productos, setProductos] = useState<Producto[]>([]);
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [metodoPago, setMetodoPago] = useState<string>("EFECTIVO");
  const [processingVenta, setProcessingVenta] = useState(false);
  const [repartidores, setRepartidores] = useState<any[]>([]);
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

  const [tipoVenta, setTipoVenta] = useState<string>("MOSTRADOR");
  const [mostrarModalDelivery, setMostrarModalDelivery] = useState(false);
  const [deliveryData, setDeliveryData] = useState({
    idRepartidor: "",
    distanciaKm: 0,
    costoEnvio: 0,
    linkCliente: ""   // Nuevo campo para link de ubicación del cliente
  });
  const [qrCode, setQrCode] = useState<string>("");

  useEffect(() => {
    cargarProductos();
  }, []);

  useEffect(() => {
  cargarRepartidores();
}, []);


const cargarRepartidores = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/usuarios/lista', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      }
    });
    const usuarios = await response.json();
    // Filtrar solo REPARTIDORES
    const reps = usuarios.filter((u: any) => u.rol === 'REPARTIDOR');
    setRepartidores(reps);
  } catch (error) {
    console.error("Error al cargar repartidores:", error);
  }
};
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

const total = cart.reduce((sum, item) => sum + (item.precio * item.quantity), 0);
const subtotal = total / 1.18;
const igv = total - subtotal;

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
        tipoVenta: tipoVenta,
        cliente: clientePayload,
        delivery: tipoVenta === "DELIVERY" ? deliveryData : null, // Datos de delivery si aplica
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
          setTipoVenta("MOSTRADOR");
          setDeliveryData({
            idRepartidor: "",
            distanciaKm: 0,
            costoEnvio: 0,
            linkCliente: ""
          });
          setQrCode("");
          setMostrarModalDelivery(false); 
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
            {/*Tipo de Cliente */}
            <div>
              <label className="block text-sm font-medium mb-3">Tipo de Venta:</label>
              <div className="flex gap-4">
                <label className="flex items-center cursor-pointer">
                  <input
                    type="radio"
                    name="tipoVenta"
                    value="MOSTRADOR"
                    checked={tipoVenta === "MOSTRADOR"}
                    onChange={(e) => {
                      setTipoVenta(e.target.value);
                      setMostrarModalDelivery(false);
                    }}
                    className="mr-2"
                  />
                  <span className="text-sm">Mostrador</span>
                </label>
                <label className="flex items-center cursor-pointer">
                  <input
                    type="radio"
                    name="tipoVenta"
                    value="DELIVERY"
                    checked={tipoVenta === "DELIVERY"}
                    onChange={(e) => {
                      setTipoVenta(e.target.value);
                      setMostrarModalDelivery(true);
                    }}
                    className="mr-2"
                  />
                  <span className="text-sm">Delivery</span>
                </label>
              </div>
            </div>
            {/*Tipo de Cliente */}
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
      {/*Modal Delivery */}
      {mostrarModalDelivery && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg w-96 p-6 max-h-[90vh] overflow-y-auto">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-bold">Datos del Delivery</h3>
              <button 
                onClick={() => setMostrarModalDelivery(false)} 
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            
            <div className="space-y-4">
              {/* Repartidor */}
              <div>
                <label className="block text-sm font-medium mb-2">Repartidor:</label>
                <select
                  value={deliveryData.idRepartidor}
                  onChange={(e) => setDeliveryData({...deliveryData, idRepartidor: e.target.value})}
                  className="w-full border rounded px-3 py-2"
                >
                  <option value="">Seleccionar repartidor</option>
                    {repartidores.map((rep: any) => (
                        <option key={rep.idUsuario} value={rep.idUsuario}>
                          {rep.nombreCompleto}
                        </option>
                      ))}
                </select>
              </div>

              {/* Distancia */}
              <div>
                <label className="block text-sm font-medium mb-2">Distancia (km):</label>
                <Input
                  type="number"
                  step="0.1"
                  value={deliveryData.distanciaKm}
                  onChange={(e) => setDeliveryData({...deliveryData, distanciaKm: parseFloat(e.target.value) || 0})}
                  placeholder="0.0"
                />
              </div>

              {/* Costo Envío */}
              <div>
                <label className="block text-sm font-medium mb-2">Costo Envío (S/):</label>
                <Input
                  type="number"
                  step="0.01"
                  value={deliveryData.costoEnvio}
                  onChange={(e) => setDeliveryData({...deliveryData, costoEnvio: parseFloat(e.target.value) || 0})}
                  placeholder="0.00"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Link Ubicación Cliente (Google Maps):</label>
                <textarea
                  value={deliveryData.linkCliente || ""}
                  onChange={(e) => setDeliveryData({...deliveryData, linkCliente: e.target.value})}
                  placeholder="Pega aquí el link de Google Maps del cliente"
                  className="w-full border rounded px-3 py-2 text-sm"
                  rows={3}
                />
              </div>
              {/* Botón Generar QR */}
              <Button
                onClick={() => {
                  if (!deliveryData.idRepartidor || deliveryData.distanciaKm === 0 || !deliveryData.linkCliente) {
                    toast.error("Completa todos los datos (repartidor, distancia y link cliente)");
                    return;
                  }
                  
                  const coordsCliente = extraerCoordenadas(deliveryData.linkCliente);
                  if (!coordsCliente) {
                    toast.error("Link inválido. Debe contener coordenadas (@lat,lng)");
                    return;
                  }
                  const rutaLink = `https://www.google.com/maps/dir/?api=1&destination=${coordsCliente}`;
                  
                  const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(rutaLink)}`;
                  setQrCode(qrUrl);
                  toast.success("QR generado correctamente");
                }}
                className="w-full bg-green-600 hover:bg-green-700 text-white"
              >
                Generar QR
              </Button>

              {/* QR Mostrado */}
              {qrCode && (
                <div className="p-4 bg-gray-50 rounded-lg border text-center">
                  <h4 className="font-medium mb-3">QR Generado:</h4>
                  <img src={qrCode} alt="QR Code" className="w-40 h-40 mx-auto" />
                  <p className="text-xs text-gray-500 mt-2">Escanea con el repartidor</p>
                  <Button
                    variant="outline"
                    size="sm"
                    className="mt-2"
                    onClick={() => {
                      const link = document.createElement("a");
                      link.href = qrCode;
                      link.download = "qr-delivery.png";
                      link.click();
                    }}
                  >
                    Descargar QR
                  </Button>
                </div>
              )}

              {/* Botón Cerrar */}
              <div className="flex gap-2 pt-4">
                <Button
                  variant="outline"
                  className="flex-1"
                  onClick={() => setMostrarModalDelivery(false)}
                >
                  Cerrar
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Ventas;