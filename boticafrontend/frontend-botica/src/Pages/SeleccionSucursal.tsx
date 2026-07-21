import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Store, MapPin, ArrowRight, X, Pencil, Trash2 } from 'lucide-react'; // ============ CAMBIO: se añaden iconos ============
import { getSucursalesByBotica, crearSucursal, actualizarSucursal, eliminarSucursal } from '../services/sucursalService'; // ============ CAMBIO ============

interface Sucursal {
  idAlmacen: number;
  nombreSucursal: string;
  direccion: string;
}

const SeleccionSucursal = () => {
  const [sucursales, setSucursales] = useState<Sucursal[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalAbierto, setModalAbierto] = useState(false);
  const [nuevaSucursal, setNuevaSucursal] = useState({ nombreSucursal: '', direccion: '' });
  const [editandoId, setEditandoId] = useState<number | null>(null); // ============ AÑADIR: null = creando, número = editando esa sucursal ============
  const navigate = useNavigate();

  const idBotica = localStorage.getItem('idBotica') || '4'; 

  useEffect(() => {
    cargarSucursales();
  }, []);

  const cargarSucursales = async () => {
    try {
      const data = await getSucursalesByBotica(idBotica);
      setSucursales(data);
    } catch (err) {
      console.error('Error al cargar sucursales:', err);
    } finally {
      setLoading(false);
    }
  };

  const seleccionarSucursal = (idAlmacen: number, nombreSucursal: string) => {
    // 👈 IMPORTANTE: Guardamos ambas llaves para compatibilidad con servicios
    localStorage.setItem('idAlmacen', idAlmacen.toString());
    localStorage.setItem('sucursalActivaId', idAlmacen.toString());
    localStorage.setItem('sucursalActivaNombre', nombreSucursal);
    
    navigate(`/dashboard?almacenId=${idAlmacen}`);
  };

  const handleCrearSucursal = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await crearSucursal({
        ...nuevaSucursal,
        idBotica: Number(idBotica)
      });
      setModalAbierto(false);
      setNuevaSucursal({ nombreSucursal: '', direccion: '' });
      cargarSucursales(); // Recarga la lista con la nueva sucursal
    } catch (err) {
      console.error('Error al crear sucursal:', err);
    }
  };
  // ============ AÑADIR: abrir el modal en modo edición, precargando los datos ============
const abrirModalEdicion = (sucursal: Sucursal, e: React.MouseEvent) => {
    e.stopPropagation(); // evita que se dispare seleccionarSucursal al hacer click en editar
    setEditandoId(sucursal.idAlmacen);
    setNuevaSucursal({ nombreSucursal: sucursal.nombreSucursal, direccion: sucursal.direccion });
    setModalAbierto(true);
};

// ============ AÑADIR: guardar edición ============
const handleActualizarSucursal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (editandoId === null) return;
    try {
      await actualizarSucursal(editandoId, nuevaSucursal);
      setModalAbierto(false);
      setEditandoId(null);
      setNuevaSucursal({ nombreSucursal: '', direccion: '' });
      cargarSucursales();
    } catch (err) {
      console.error('Error al actualizar sucursal:', err);
    }
};

// ============ AÑADIR: eliminar sucursal ============
const handleEliminarSucursal = async (idAlmacen: number, e: React.MouseEvent) => {
    e.stopPropagation(); // evita que se dispare seleccionarSucursal
    if (!confirm('¿Seguro que deseas eliminar esta sucursal? Esta acción no se puede deshacer.')) return;
    try {
      await eliminarSucursal(idAlmacen);
      cargarSucursales();
    } catch (err: any) {
      alert(err.response?.data?.message || 'No se pudo eliminar la sucursal. Verifica que no tenga empleados o inventario asignado.');
      console.error('Error al eliminar sucursal:', err);
    }
};
// ============ FIN ============



  return (
    <div className="min-h-screen w-full bg-slate-900 text-white p-6 md:p-12 flex flex-col items-center">
      
      {/* Encabezado */}
      <div className="max-w-5xl w-full text-center mb-10">
        <h1 className="text-3xl md:text-4xl font-bold mb-2">Selecciona tu Sucursal</h1>
        <p className="text-slate-400">Elige la sede sobre la cual deseas operar o administrar en este momento</p>
      </div>

      {/* Grid de Tarjetas de Sucursales */}
      <div className="max-w-5xl w-full grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        
        {sucursales.map((sucursal) => (
          <div
            key={sucursal.idAlmacen}
            onClick={() => seleccionarSucursal(sucursal.idAlmacen, sucursal.nombreSucursal)}
            className="group relative bg-slate-800/80 border border-slate-700/60 rounded-2xl p-6 hover:border-blue-500/80 hover:bg-slate-800 transition-all duration-300 cursor-pointer shadow-lg hover:shadow-blue-500/10 flex flex-col justify-between"
          >
            {/* ============ AÑADIR: botones editar/eliminar, arriba a la derecha de la tarjeta ============ */}
            <div className="absolute top-4 right-4 flex gap-2 z-10">
              <button
                onClick={(e) => abrirModalEdicion(sucursal, e)}
                className="p-1.5 rounded-lg bg-slate-700/80 hover:bg-blue-600 text-slate-300 hover:text-white transition-colors"
                title="Editar sucursal"
              >
                <Pencil className="w-3.5 h-3.5" />
              </button>
              <button
                onClick={(e) => handleEliminarSucursal(sucursal.idAlmacen, e)}
                className="p-1.5 rounded-lg bg-slate-700/80 hover:bg-red-600 text-slate-300 hover:text-white transition-colors"
                title="Eliminar sucursal"
              >
                <Trash2 className="w-3.5 h-3.5" />
              </button>
            </div>
            {/* ============ FIN ============ */}

    <div>
      <div className="w-12 h-12 bg-blue-600/20 text-blue-400 rounded-xl flex items-center justify-center mb-4 group-hover:bg-blue-600 group-hover:text-white transition-all">
        <Store className="w-6 h-6" />
      </div>
      <h2 className="text-xl font-bold mb-1 group-hover:text-blue-400 transition-colors">
        {sucursal.nombreSucursal}
      </h2>
      <div className="flex items-center text-slate-400 text-sm gap-1.5 mb-4">
        <MapPin className="w-4 h-4 shrink-0" />
        <span>{sucursal.direccion || 'Sin dirección registrada'}</span>
      </div>
    </div>

    <div className="flex items-center justify-between pt-4 border-t border-slate-700/50 text-xs font-semibold text-blue-400">
      <span>Ingresar al panel</span>
      <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
    </div>
  </div>
))}

        {/* Botón "+" que ahora abre el modal */}
          <button
            onClick={() => {
              setEditandoId(null); // ============ AÑADIR: aseguramos modo "crear", no edición ============
              setNuevaSucursal({ nombreSucursal: '', direccion: '' });
              setModalAbierto(true);
            }}
            className="h-full min-h-[180px] bg-slate-800/30 border-2 border-dashed border-slate-700 hover:border-blue-500 hover:bg-blue-600/5 rounded-2xl p-6 flex flex-col items-center justify-center text-slate-400 hover:text-blue-400 transition-all group cursor-pointer"
          >
          <div className="w-14 h-14 rounded-full bg-slate-800 border border-slate-700 group-hover:border-blue-500 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
            <Plus className="w-8 h-8 text-blue-500" />
          </div>
          <span className="font-semibold text-sm">Agregar Nueva Sucursal</span>
        </button>

      </div>

      {/* Modal para agregar sucursal */}
      {modalAbierto && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-slate-800 border border-slate-700 rounded-2xl max-w-md w-full p-6 relative text-white shadow-2xl">
          <button 
            onClick={() => {
              setModalAbierto(false);
              setEditandoId(null); // ============ AÑADIR ============
              setNuevaSucursal({ nombreSucursal: '', direccion: '' });
            }} 
            className="absolute top-4 right-4 text-slate-400 hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
            <h2 className="text-xl font-bold mb-4">
              {editandoId !== null ? 'Editar Sucursal' : 'Registrar Nueva Sucursal'} {/* ============ CAMBIO: título dinámico ============ */}
            </h2>
            
            <form onSubmit={editandoId !== null ? handleActualizarSucursal : handleCrearSucursal} className="space-y-4"> {/* ============ CAMBIO: elige la función según el modo ============ */}              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">Nombre de la Sucursal</label>
                <input
                  type="text"
                  required
                  placeholder="Ej: Sede Principal / Los Olivos"
                  value={nuevaSucursal.nombreSucursal}
                  onChange={(e) => setNuevaSucursal({ ...nuevaSucursal, nombreSucursal: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-blue-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">Dirección</label>
                <input
                  type="text"
                  required
                  placeholder="Ej: Av. Universitaria 1234"
                  value={nuevaSucursal.direccion}
                  onChange={(e) => setNuevaSucursal({ ...nuevaSucursal, direccion: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-blue-500"
                />
              </div>

              <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => {
                  setModalAbierto(false);
                  setEditandoId(null); // ============ AÑADIR ============
                  setNuevaSucursal({ nombreSucursal: '', direccion: '' });
                }}
                className="px-4 py-2 text-sm rounded-xl bg-slate-700 text-slate-300 hover:bg-slate-600"
              >
                Cancelar
              </button>
                <button
                  type="submit"
                  className="px-4 py-2 text-sm rounded-xl bg-blue-600 text-white hover:bg-blue-500 font-semibold"
                >
                  {editandoId !== null ? 'Actualizar Sucursal' : 'Guardar Sucursal'} {/* ============ CAMBIO: texto dinámico ============ */}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default SeleccionSucursal;