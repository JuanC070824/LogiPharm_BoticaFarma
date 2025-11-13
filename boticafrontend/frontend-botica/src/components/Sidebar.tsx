import { FileText, Home, Package, ShoppingCart, Truck, Users } from "lucide-react"
import { Link, useLocation } from "react-router-dom"

export const Sidebar = () => {
  const location = useLocation()

  const menuItems = [
    { path: "/dashboard", icon: Home, label: "Dashboard" },
    { path: "/ventas", icon: ShoppingCart, label: "Ventas" },
    { path: "/inventario", icon: Package, label: "Inventario" },
    { path: "/reportes", icon: FileText, label: "Reportes" },
    { path: "/usuarios", icon: Users, label: "Usuarios" },
    { path: "/delivery", icon: Truck, label: "Delivery" },
  ]

  return (
    <aside className="w-64 bg-card border-r border-border">
      <div className="p-6">
        <h2 className="text-2xl font-bold text-primary">Botica Farma</h2>
      </div>
      <nav className="space-y-2 px-4">
        {menuItems.map((item) => {
          const Icon = item.icon
          const isActive = location.pathname === item.path
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? "bg-primary text-primary-foreground"
                  : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
              }`}
            >
              <Icon className="h-5 w-5" />
              <span>{item.label}</span>
            </Link>
          )
        })}
      </nav>
    </aside>
  )
}