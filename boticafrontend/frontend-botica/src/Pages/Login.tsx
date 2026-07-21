import fondobotica from "@/assets/fondobotica.png";
import logofarma from "@/assets/logofarma.png";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Lock, User } from "lucide-react";
import type { FormEvent } from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await login({ username, password });
      
      if (response.success && response.token) {
        localStorage.setItem('token', response.token);
        localStorage.setItem('rol', response.rol || '');
        localStorage.setItem('nombre', response.nombre || '');
        localStorage.setItem('username', response.username || '');
        
        if (response.idBotica) {
          localStorage.setItem('idBotica', response.idBotica.toString());
        }
        if (response.idAlmacen) {
          localStorage.setItem('idAlmacen', response.idAlmacen.toString());
        }
        console.log('ROL RECIBIDO:', JSON.stringify(response.rol));
        if (response.rol === 'ADMIN') {
          navigate('/sucursales'); // el ADMIN elige sucursal libremente
        } else {
          navigate('/dashboard'); // FARMACEUTICO/REPARTIDOR van directo, ya tienen sucursal fija
        }
      } else {
        setError(response.message || 'Error al iniciar sesión');
      }
    } catch (err) {
      setError('Error de conexión con el servidor');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen w-full flex items-center justify-center p-4">
      {/* Fondo con imagen de farmacia */}
      <div 
        className="absolute inset-0 z-0"
        style={{
          backgroundImage: `url(${fondobotica})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      
      {/* Overlay con gradiente azul */}
      <div 
        className="absolute inset-0 z-10"
        style={{
          background: `linear-gradient(135deg, rgba(27, 58, 121, 0.85) 0%, rgba(45, 90, 160, 0.85) 100%)`,
        }}
      />

      {/* Contenido del login */}
      <div className="relative z-20 w-full max-w-md mx-auto px-4">
        <div className="backdrop-blur-lg bg-white/15 border border-white/20 rounded-3xl shadow-2xl p-8 md:p-12">
          {/* Logo */}
          <div className="flex justify-center mb-6">
            <div className="w-24 h-24 bg-white rounded-full p-2 shadow-lg">
              <img src={logofarma} alt="BoticaFarma Logo" className="w-full h-full object-contain" />
            </div>
          </div>
          
          {/* Title */}
          <h1 className="text-4xl md:text-5xl font-bold text-white text-center mb-3">
            LogiPharm
          </h1>
          <p className="text-white/90 text-center mb-8 text-sm md:text-base">
            Sistema de gestión para BoticaFarma
          </p>
          
          {/* Form */}
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Username Input */}
            <div className="relative">
              <Input
                type="text"
                placeholder="Usuario"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-12 pr-4 py-6 text-base focus:bg-white/20 focus:border-white/50 transition-all"
                required
              />
              <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-white/70" />
            </div>
            
            {/* Password Input */}
            <div className="relative">
              <Input
                type="password"
                placeholder="Contraseña"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-12 pr-4 py-6 text-base focus:bg-white/20 focus:border-white/50 transition-all"
                required
              />
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-white/70" />
            </div>
            
            {/* Error Message */}
            {error && (
              <div className="bg-red-500/20 border border-red-500/50 text-white px-4 py-3 rounded-lg text-sm">
                {error}
              </div>
            )}
            
            {/* Forgot Password */}
            
            <div className="text-right text-sm">
              <a href="#" className="text-white hover:text-white/80 transition-colors">
                          
              </a>
            </div>
            
            {/* Login Button */}
            <Button
              type="submit"
              disabled={loading}
              className="w-full bg-white text-[#1b3a79] hover:bg-white/90 rounded-full py-6 text-lg font-semibold transition-all disabled:opacity-50"
            >
              {loading ? 'Ingresando...' : 'Loguearse'}
            </Button>
            {/* Enlace para Registro de Nueva Botica */}
            <div className="text-center mt-6 text-sm text-white/90">
              ¿Eres dueño de una botica?{" "}
              <button
                type="button"
                onClick={() => navigate('/registro-botica')}
                className="font-semibold text-white underline hover:text-white/80 transition-colors"
              >
                Regístrala aquí
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;