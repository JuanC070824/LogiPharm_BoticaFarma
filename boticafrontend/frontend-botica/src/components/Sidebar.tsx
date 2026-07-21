import { FileText, Home, Package, ShoppingCart, Users } from "lucide-react"
import { Link, useLocation } from "react-router-dom"
import logoFarma from "../assets/boticalogo.png";
export const Sidebar = () => {
  const location = useLocation()

  const menuItems = [
    { path: "/dashboard", icon: Home, label: "Dashboard" },
    { path: "/ventas", icon: ShoppingCart, label: "Ventas" },
    { path: "/inventario", icon: Package, label: "Inventario" },
    { path: "/reportes", icon: FileText, label: "Reportes" },
    { path: "/usuarios", icon: Users, label: "Usuarios" },
    { path: "/sucursales", icon: Package, label: "Sucursales" },
    //{ path: "/delivery", icon: Truck, label: "Delivery" },
  ]

  return (
    <aside className="w-64 bg-slate-900 border-r border-slate-800 flex flex-col min-h-screen">

      {/* SECCIÓN DEL LOGO ACTUALIZADA */}
      <div className="p-6 flex flex-col items-center">
        <Link to="/dashboard" className="transition-transform hover:scale-105">
          <div className="bg-white p-2 rounded-lg shadow-md mb-2">
            <img
              src={logoFarma}
              alt="Botica Farma Logo"
              className="h-12 w-auto object-contain"
            />
          </div>
        </Link>
        <p className="text-[10px] text-blue-400 font-bold tracking-[0.2em] uppercase">
          LogiPharm
        </p>
      </div>

      <nav className="space-y-2 px-4 flex-1">
        {/* ... (Todo el mapeo de menuItems se queda EXACTAMENTE igual) */}
        {menuItems.map((item) => {
          const Icon = item.icon
          const isActive = location.pathname === item.path
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 ${isActive
                ? "bg-blue-600 text-white shadow-lg shadow-blue-900/20"
                : "text-slate-400 hover:bg-slate-800 hover:text-slate-100"
                }`}
            >
              <Icon className={`h-5 w-5 ${isActive ? "text-white" : "text-slate-400"}`} />
              <span className="font-medium">{item.label}</span>
            </Link>
          )
        })}
      </nav>

      <div className="p-4 border-t border-slate-800">
        <div className="px-4 py-2 text-xs text-slate-500">
          v1.0.0 - 2026
        </div>
      </div>
    </aside>
  )
}