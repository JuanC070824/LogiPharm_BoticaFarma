import fondobotica from "@/assets/fondobotica.png";
import logofarma from "@/assets/logofarma.png";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Building, Home, Lock, Mail, User } from "lucide-react";
import type { FormEvent } from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerBotica } from '../services/authService';

const RegisterBotica = () => {
  // Nombres alineados exactamente con RegistroBoticaDTO
  const [formData, setFormData] = useState({
    nombreBotica: '',
    ruc: '',
    nombreSede: 'Sede Central',
    direccionLocal: '',
    nombreAdmin: '',
    apatAdmin: '',
    amatAdmin: '',
    username: '',
    correo: '',
    password: ''
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await registerBotica(formData);
      alert('Botica registrada con éxito. Ya puedes iniciar sesión.');
      navigate('/login');
    } catch (err: any) {
      setError(err.message || 'Error al registrar la botica');
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

      {/* Tarjeta de Registro */}
      <div className="relative z-20 w-full max-w-xl mx-auto px-4 my-8">
        <div className="backdrop-blur-lg bg-white/15 border border-white/20 rounded-3xl shadow-2xl p-6 md:p-8">
          
          <div className="flex justify-center mb-4">
            <div className="w-16 h-16 bg-white rounded-full p-2 shadow-lg">
              <img src={logofarma} alt="BoticaFarma Logo" className="w-full h-full object-contain" />
            </div>
          </div>

          <h1 className="text-3xl font-bold text-white text-center mb-1">Registro de Botica</h1>
          <p className="text-white/80 text-center mb-6 text-sm">Crea tu cuenta de administrador y registra tu farmacia</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <p className="text-white font-semibold text-xs uppercase tracking-wider">Datos de la Botica</p>
            
            <div className="relative">
              <Input
                name="nombreBotica"
                placeholder="Nombre de la Botica"
                onChange={handleChange}
                className="w-full bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-10 py-5 text-sm"
                required
              />
              <Building className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/70" />
            </div>

            <div className="grid grid-cols-2 gap-2">
              <Input
                name="ruc"
                placeholder="RUC (11 dígitos)"
                onChange={handleChange}
                className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full py-5 text-sm"
                required
              />
              <Input
                name="nombreSede"
                placeholder="Sede Central"
                defaultValue="Sede Central"
                onChange={handleChange}
                className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full py-5 text-sm"
              />
            </div>

            <div className="relative">
              <Input
                name="direccionLocal"
                placeholder="Dirección del local"
                onChange={handleChange}
                className="w-full bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-10 py-5 text-sm"
                required
              />
              <Home className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/70" />
            </div>

            <p className="text-white font-semibold text-xs uppercase tracking-wider pt-2">Datos del Administrador</p>

            <div className="grid grid-cols-3 gap-2">
              <Input name="nombreAdmin" placeholder="Nombre" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full py-5 text-sm" required />
              <Input name="apatAdmin" placeholder="A. Paterno" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full py-5 text-sm" required />
              <Input name="amatAdmin" placeholder="A. Materno" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full py-5 text-sm" required />
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div className="relative">
                <Input name="username" placeholder="Usuario" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-9 py-5 text-sm" required />
                <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/70" />
              </div>
              <div className="relative">
                <Input name="correo" type="email" placeholder="Correo" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-9 py-5 text-sm" required />
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/70" />
              </div>
            </div>

            <div className="relative">
              <Input name="password" type="password" placeholder="Contraseña" onChange={handleChange} className="bg-white/10 border-white/30 text-white placeholder:text-white/60 rounded-full pl-10 py-5 text-sm" required />
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/70" />
            </div>

            {error && (
              <div className="bg-red-500/20 border border-red-500/50 text-white px-3 py-2 rounded-lg text-xs">
                {error}
              </div>
            )}

            <Button
              type="submit"
              disabled={loading}
              className="w-full bg-white text-[#1b3a79] hover:bg-white/90 rounded-full py-5 text-base font-semibold transition-all mt-2"
            >
              {loading ? 'Registrando...' : 'Registrar Botica'}
            </Button>

            <div className="text-center mt-3 text-xs text-white">
              ¿Ya tienes cuenta?{" "}
              <button type="button" onClick={() => navigate('/login')} className="font-semibold underline">Inicia Sesión</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterBotica;