import { Link } from "react-router-dom"

const NotFound = () => {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <h1 className="text-6xl font-bold text-primary mb-4">404</h1>
        <p className="text-xl text-muted-foreground mb-8">Página no encontrada</p>
        <Link
          to="/dashboard"
          className="px-6 py-3 bg-primary text-primary-foreground rounded-md hover:bg-primary/90 transition-colors"
        >
          Volver al Dashboard
        </Link>
      </div>
    </div>
  )
}

export default NotFound