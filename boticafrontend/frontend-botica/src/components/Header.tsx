import { LogOut } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { logout } from '../services/authService'

export const Header = () => {
  const navigate = useNavigate()
  const nombre = localStorage.getItem('nombre')
  const rol = localStorage.getItem('rol')

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="h-16 border-b border-border bg-card px-6 flex items-center justify-between">
      <div className="text-xl font-semibold text-foreground">
        LogiPharm
      </div>
      <div className="flex items-center gap-4">
        <div className="text-sm text-muted-foreground">
          <span className="font-medium text-foreground">Bienvenido, {nombre}</span> ({rol})
        </div>
        <button
          onClick={handleLogout}
          className="flex items-center gap-2 px-4 py-2 bg-destructive text-destructive-foreground rounded-md hover:bg-destructive/90 transition-colors"
        >
          <LogOut className="h-4 w-4" />
          Cerrar Sesión
        </button>
      </div>
    </header>
  )
}