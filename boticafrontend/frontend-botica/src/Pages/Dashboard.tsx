import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { DollarSign, Package, TrendingUp, Users } from "lucide-react";
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';
// 1. IMPORTAR EL COMPONENTE
import ReporteCard from '../components/ReporteCard'; // <--- NUEVO: Ajusta la ruta si es necesario

const Dashboard = () => {
  const navigate = useNavigate();
  const nombre = localStorage.getItem('nombre');
  const rol = localStorage.getItem('rol');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const stats = [
    // ... (Tus estadísticas se quedan igual)
    {
      title: "Ventas Hoy",
      value: "S/ 750.00",
      icon: DollarSign,
      change: "+12.5%",
      changeType: "positive" as const,
    },
    {
      title: "Productos en Stock",
      value: "429",
      icon: Package,
      change: "-5 productos",
      changeType: "neutral" as const,
    },
    {
      title: "Clientes Registrados",
      value: "3",
      icon: Users,
      change: "+3 este mes",
      changeType: "positive" as const,
    },
    {
      title: "Ventas del Mes",
      value: "S/ 810.30",
      icon: TrendingUp,
      //change: "+23.1%",
      changeType: "positive" as const,
    },
  ];

  return (
    <div className="min-h-screen bg-background">

      {/* Contenido del Dashboard */}
      <div className="p-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-4xl font-bold text-foreground">Panel Principal</h1>
          {/* Opcional: Botón de Logout aquí si no lo tienes en el Sidebar */}
          {/* <button onClick={handleLogout} className="text-red-500">Cerrar Sesión</button> */}
        </div>

        {/* SECCIÓN 1: ESTADÍSTICAS (Sin cambios) */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {stats.map((stat, index) => (
            <Card key={index} className="bg-card border-border">
              <CardHeader className="flex flex-row items-center justify-between pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  {stat.title}
                </CardTitle>
                <stat.icon className="h-5 w-5 text-primary" />
              </CardHeader>
              <CardContent>
                <div className="text-3xl font-bold text-foreground mb-1">
                  {stat.value}
                </div>
                <p className={`text-sm ${stat.changeType === "positive"
                    ? "text-success"
                    : "text-muted-foreground"
                  }`}>
                  {stat.change}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* SECCIÓN 2: CONTENIDO PRINCIPAL */}
        {/* CAMBIO AQUÍ: Cambiamos lg:grid-cols-2 a lg:grid-cols-3 para hacer espacio */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">  {/* <--- CAMBIO */}

          {/* 2. INTEGRAR LA TARJETA DE REPORTE */}
          {/* Solo se muestra si el rol es ADMIN */}
          {rol === 'ADMIN' && (
            <div className="lg:col-span-1">
              <ReporteCard />
            </div>
          )}

          {/* TUS TABLAS EXISTENTES */}
          {/* Nota: Si no es admin, las tablas se estirarán para llenar el espacio, lo cual es bueno. */}

          {/* Tabla de Productos Críticos */}
          <Card className="bg-card border-border lg:col-span-1"> {/* Opcional: forzar col-span si quieres controlar el ancho */}
            <CardHeader>
              <CardTitle className="text-foreground">Productos Críticos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { name: "Ensure Nutricional 400g", stock: 10, status: "Bajo stock" },
                  { name: "Listerine Cool Mint 500ml", stock: 0, status: "Sin stock" },
                  { name: "Cetaphil Crema Hidratante 250ml", stock: 0, status: "Sin Stock" },
                ].map((product, index) => (
                  <div key={index} className="flex justify-between items-center py-2 border-b border-border last:border-0">
                    <div>
                      <p className="font-medium text-foreground">{product.name}</p>
                      <p className="text-sm text-muted-foreground">Stock: {product.stock} unidades</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${product.status === "Crítico"
                        ? "bg-destructive/20 text-destructive"
                        : "bg-warning/20 text-warning"
                      }`}>
                      {product.status}
                    </span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Tabla de Actividad Reciente */}
          <Card className="bg-card border-border lg:col-span-1">
            <CardHeader>
              <CardTitle className="text-foreground">Actividad Reciente</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { action: "Nueva venta registrada", time: "Hace 5 minutos", amount: "S/ 750.00" },
                  { action: "Producto agregado", time: "Hace 15 min", amount: null }, // Acorté el texto para que quepa mejor
                  { action: "Cliente registrado", time: "Hace 1 hora", amount: null },
                  { action: "Venta completada", time: "Hace 2 horas", amount: "S/ 89.50" },
                ].map((activity, index) => (
                  <div key={index} className="flex justify-between items-start py-2 border-b border-border last:border-0">
                    <div className="flex-1">
                      <p className="font-medium text-foreground">{activity.action}</p>
                      <p className="text-sm text-muted-foreground">{activity.time}</p>
                    </div>
                    {activity.amount && (
                      <span className="text-success font-medium">{activity.amount}</span>
                    )}
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;