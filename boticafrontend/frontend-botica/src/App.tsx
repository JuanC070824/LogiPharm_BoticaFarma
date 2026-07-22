import { Header } from "@/components/Header";
import { Sidebar } from "@/components/Sidebar";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import * as React from "react";
import { HashRouter, Navigate, Route, Routes } from "react-router-dom"; import Dashboard from "./Pages/Dashboard";
import Delivery from "./Pages/Delivery";
import Inventario from "./Pages/Inventario";
import Login from "./Pages/Login";
import RegisterBotica from "./Pages/RegisterBotica"; // <--- Importado
import NotFound from "./Pages/NotFound";
import Reportes from "./Pages/Reportes";
import Usuarios from "./Pages/Usuarios";
import Ventas from "./Pages/Ventas";
import './index.css';
import logofarma from "./assets/logofarma.png";
import SeleccionSucursal from './Pages/SeleccionSucursal';
const queryClient = new QueryClient();

// Componente para proteger rutas
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const token = localStorage.getItem('token');

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

// Layout principal con Sidebar y Header
const MainLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="flex min-h-screen w-full bg-background relative overflow-hidden">
      {/* MARCA DE AGUA GLOBAL */}
      <div className="fixed inset-0 flex items-center justify-center pointer-events-none select-none z-0 opacity-[0.03] ml-64">
        <img
          src={logofarma}
          alt="Watermark"
          className="w-[400px] md:w-[600px] object-contain grayscale"
        />
      </div>

      <Sidebar />
      <div className="flex-1 flex flex-col relative z-10">
        <Header />
        <main className="flex-1 overflow-auto">
          {children}
        </main>
      </div>
    </div>
  );
};

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <HashRouter>
        <Routes>
          {/* Rutas públicas */}
          <Route path="/login" element={<Login />} />
          <Route path="/registro-botica" element={<RegisterBotica />} /> {/* <--- Nueva ruta */}
          <Route
            path="/sucursales"
            element={
              localStorage.getItem('rol') === 'ADMIN'
                ? <SeleccionSucursal />
                : <Navigate to="/dashboard" replace />
            }
          />
          {/* Rutas protegidas con Layout */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Dashboard />
                </MainLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/ventas"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Ventas />
                </MainLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/inventario"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Inventario />
                </MainLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/reportes"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Reportes />
                </MainLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/usuarios"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Usuarios />
                </MainLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/delivery"
            element={
              <ProtectedRoute>
                <MainLayout>
                  <Delivery />
                </MainLayout>
              </ProtectedRoute>
            }
          />

          {/* Redireccionar raíz a login */}
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Página 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </HashRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;