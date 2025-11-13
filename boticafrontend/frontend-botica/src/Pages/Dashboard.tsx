import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { DollarSign, Package, TrendingUp, Users } from "lucide-react";
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';

const Dashboard = () => {
  const navigate = useNavigate();
  const nombre = localStorage.getItem('nombre');
  const rol = localStorage.getItem('rol');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const stats = [
    {
      title: "Ventas Hoy",
      value: "S/ 2,450.00",
      icon: DollarSign,
      change: "+12.5%",
      changeType: "positive" as const,
    },
    {
      title: "Productos en Stock",
      value: "1,245",
      icon: Package,
      change: "-5 productos",
      changeType: "neutral" as const,
    },
    {
      title: "Clientes Registrados",
      value: "328",
      icon: Users,
      change: "+18 este mes",
      changeType: "positive" as const,
    },
    {
      title: "Ventas del Mes",
      value: "S/ 45,890.00",
      icon: TrendingUp,
      change: "+23.1%",
      changeType: "positive" as const,
    },
  ];

  return (
    <div className="min-h-screen bg-background">
      
      {/* Contenido del Dashboard */}
      <div className="p-8">
        <h1 className="text-4xl font-bold text-foreground mb-8">Panel Principal</h1>
        
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
                <p className={`text-sm ${
                  stat.changeType === "positive" 
                    ? "text-success" 
                    : "text-muted-foreground"
                }`}>
                  {stat.change}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="bg-card border-border">
            <CardHeader>
              <CardTitle className="text-foreground">Productos Críticos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { name: "Paracetamol 500mg", stock: 15, status: "Bajo stock" },
                  { name: "Amoxicilina 250mg", stock: 8, status: "Bajo stock" },
                  { name: "Ibuprofeno 400mg", stock: 3, status: "Crítico" },
                ].map((product, index) => (
                  <div key={index} className="flex justify-between items-center py-2 border-b border-border last:border-0">
                    <div>
                      <p className="font-medium text-foreground">{product.name}</p>
                      <p className="text-sm text-muted-foreground">Stock: {product.stock} unidades</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      product.status === "Crítico" 
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

          <Card className="bg-card border-border">
            <CardHeader>
              <CardTitle className="text-foreground">Actividad Reciente</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { action: "Nueva venta registrada", time: "Hace 5 minutos", amount: "S/ 125.00" },
                  { action: "Producto agregado al inventario", time: "Hace 15 minutos", amount: null },
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