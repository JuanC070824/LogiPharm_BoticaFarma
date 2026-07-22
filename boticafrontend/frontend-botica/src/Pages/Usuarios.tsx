import { useEffect, useState } from 'react';
import { getSucursalesByBotica } from '../services/sucursalService';
import { API_BASE_URL } from '../config/api';
const API_URL = `${API_BASE_URL}/api/usuarios`;
//const API_URL = 'http://localhost:8080/api/usuarios';

interface Usuario {
  idUsuario: number;
  nombre: string;
  apat: string;
  amat: string;
  username: string;
  rol: string;
  nombreCompleto: string;
  idAlmacen?: number; // ============ AÑADIR ============
}
interface Cliente {
  idCliente: number;
  nombre: string;
  apellidoPat: string;
  apellidoMat: string;
  dni: number;
  ruc?: number;
  nombreCompleto: string;
}

export default function Usuarios() {
  const [activeTab, setActiveTab] = useState<'usuarios' | 'clientes'>('clientes');
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalType, setModalType] = useState<'usuario' | 'cliente'>('usuario');
  const [editingId, setEditingId] = useState<number | null>(null);
  
  const [usuarioForm, setUsuarioForm] = useState({
    nombre: '', apat: '', amat: '', username: '', password: '', rol: 'FARMACEUTICO', idAlmacen: ''
  });
  const [sucursales, setSucursales] = useState<{idAlmacen: number, nombreSucursal: string}[]>([]);

  const [clienteForm, setClienteForm] = useState({
    nombre: '', apellidoPat: '', apellidoMat: '', dni: '', ruc: ''
  });

  const userRole = localStorage.getItem('rol') || 'FARMACEUTICO';
  const isAdmin = userRole === 'ADMIN';

  useEffect(() => {
    if (isAdmin) {
      setActiveTab('usuarios');
      cargarUsuarios();
      // ============ AÑADIR: cargar sucursales al entrar como ADMIN ============
      cargarSucursalesParaForm();
      // ============ FIN ============
    }
    cargarClientes();
  }, []);

  const getAuthHeaders = () => ({
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    }
  });

  const cargarUsuarios = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_URL}/lista`, {
        method: 'GET',
        ...getAuthHeaders()
      });
      const data = await response.json();
      setUsuarios(data);
    } catch (error) {
      alert('Error al cargar usuarios');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };
  // ============ función para traer las sucursales de la Botica actual ============
  const cargarSucursalesParaForm = async () => {
    try {
      const idBotica = localStorage.getItem('idBotica') || '4';
      const data = await getSucursalesByBotica(idBotica);
      setSucursales(data);
    } catch (error) {
      console.error('Error al cargar sucursales:', error);
    }
  };

  const cargarClientes = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_URL}/clientes`, {
        method: 'GET',
        ...getAuthHeaders()
      });
      const data = await response.json();
      setClientes(data);
    } catch (error) {
      alert('Error al cargar clientes');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const abrirModalUsuario = (usuario?: Usuario) => {
    if (usuario) {
      setUsuarioForm({
        nombre: usuario.nombre,
        apat: usuario.apat,
        amat: usuario.amat,
        username: usuario.username,
        password: '',
        rol: usuario.rol,
        idAlmacen: usuario.idAlmacen ? usuario.idAlmacen.toString() : '' // ============ AÑADIR ============
      });
      setEditingId(usuario.idUsuario);
    } else {
      setUsuarioForm({ nombre: '', apat: '', amat: '', username: '', password: '', rol: 'FARMACEUTICO', idAlmacen: '' }); // ============ CAMBIO: se añadió idAlmacen: '' ============
      setEditingId(null);
    }
    setModalType('usuario');
    setModalOpen(true);
  };
const guardarUsuario = async () => {
  try {
    // AGREGAR: Preparar datos - si es REPARTIDOR, llenar campos dummy
    let datosAEnviar: any = {
      ...usuarioForm,
      idAlmacen: usuarioForm.idAlmacen ? Number(usuarioForm.idAlmacen) : null
    };
    if (usuarioForm.rol === 'REPARTIDOR') {
      datosAEnviar = {
        ...datosAEnviar,
        username: 'dummy', // Backend generará UUID real
        password: 'dummy'  // Backend generará contraseña real
      };
    }

    const url = editingId ? `${API_URL}/actualizar/${editingId}` : `${API_URL}/crear`;
    const method = editingId ? 'PUT' : 'POST';
    
    const response = await fetch(url, {
      method,
      ...getAuthHeaders(),
      body: JSON.stringify(datosAEnviar) 
    });

    if (!response.ok) throw new Error('Error en la petición');
    
    alert(editingId ? 'Usuario actualizado correctamente' : 'Usuario creado correctamente');
    cargarUsuarios();
    setModalOpen(false);
  } catch (error) {
    alert('Error al guardar usuario');
    console.error(error);
  }
};

  const eliminarUsuario = async (id: number) => {
    if (!confirm('¿Está seguro de eliminar este usuario?')) return;
    try {
      const response = await fetch(`${API_URL}/eliminar/${id}`, {
        method: 'DELETE',
        ...getAuthHeaders()
      });

      if (!response.ok) throw new Error('Error al eliminar');

      alert('Usuario eliminado correctamente');
      cargarUsuarios();
    } catch (error) {
      alert('Error al eliminar usuario');
      console.error(error);
    }
  };

  const abrirModalCliente = (cliente?: Cliente) => {
    if (cliente) {
      setClienteForm({
        nombre: cliente.nombre,
        apellidoPat: cliente.apellidoPat,
        apellidoMat: cliente.apellidoMat,
        dni: cliente.dni.toString(),
        ruc: cliente.ruc?.toString() || ''
      });
      setEditingId(cliente.idCliente);
    } else {
      setClienteForm({ nombre: '', apellidoPat: '', apellidoMat: '', dni: '', ruc: '' });
      setEditingId(null);
    }
    setModalType('cliente');
    setModalOpen(true);
  };

  const guardarCliente = async () => {
    try {
      const datos = {
        ...clienteForm,
        dni: parseInt(clienteForm.dni),
        ruc: clienteForm.ruc ? parseInt(clienteForm.ruc) : null
      };

      const url = editingId ? `${API_URL}/clientes/actualizar/${editingId}` : `${API_URL}/clientes/crear`;
      const method = editingId ? 'PUT' : 'POST';

      const response = await fetch(url, {
        method,
        ...getAuthHeaders(),
        body: JSON.stringify(datos)
      });

      if (!response.ok) throw new Error('Error en la petición');

      alert(editingId ? 'Cliente actualizado correctamente' : 'Cliente creado correctamente');
      cargarClientes();
      setModalOpen(false);
    } catch (error) {
      alert('Error al guardar cliente');
      console.error(error);
    }
  };

  const eliminarCliente = async (id: number) => {
    if (!confirm('¿Está seguro de eliminar este cliente?')) return;
    try {
      const response = await fetch(`${API_URL}/clientes/eliminar/${id}`, {
        method: 'DELETE',
        ...getAuthHeaders()
      });

      if (!response.ok) throw new Error('Error al eliminar');

      alert('Cliente eliminado correctamente');
      cargarClientes();
    } catch (error) {
      alert('Error al eliminar cliente');
      console.error(error);
    }
  };

  return (
    <div className="p-6 space-y-6 bg-white min-h-screen">
      <h1 className="text-3xl font-bold text-gray-800">Gestión de Usuarios y Clientes</h1>

      {/* TABS */}
      <div className="border-b border-gray-200">
        <div className="flex space-x-8">
          {isAdmin && (
            <button
              onClick={() => setActiveTab('usuarios')}
              className={`py-2 px-4 font-medium ${
                activeTab === 'usuarios'
                  ? 'border-b-2 border-blue-500 text-blue-600'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              👥 Usuarios del Sistema
            </button>
          )}
          <button
            onClick={() => setActiveTab('clientes')}
            className={`py-2 px-4 font-medium ${
              activeTab === 'clientes'
                ? 'border-b-2 border-blue-500 text-blue-600'
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            🧑‍🤝‍🧑 Clientes
          </button>
        </div>
      </div>

      {/* TAB USUARIOS DEL SISTEMA */}
      {isAdmin && activeTab === 'usuarios' && (
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-semibold">Usuarios del Sistema</h2>
            <button
              onClick={() => abrirModalUsuario()}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center gap-2"
            >
              ➕ Nuevo Usuario
            </button>
          </div>

          {loading ? (
            <p className="text-center py-4">Cargando...</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="border p-3 text-left">ID</th>
                    <th className="border p-3 text-left">Nombre Completo</th>
                    <th className="border p-3 text-left">Username</th>
                    <th className="border p-3 text-left">Rol</th>
                    <th className="border p-3 text-left">Sucursal</th>
                    <th className="border p-3 text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {usuarios.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="border p-3 text-center text-gray-500">
                        No hay usuarios registrados
                      </td>
                    </tr>
                  ) : (
                    usuarios.map((usuario) => (
                      <tr key={usuario.idUsuario} className="hover:bg-gray-50">
                        <td className="border p-3">{usuario.idUsuario}</td>
                        <td className="border p-3">{usuario.nombreCompleto}</td>
                        <td className="border p-3">{usuario.username}</td>
                        <td className="border p-3">
                          <span className={`px-2 py-1 rounded text-xs font-semibold ${
                            usuario.rol === 'ADMIN' ? 'bg-red-100 text-red-800' :
                            usuario.rol === 'FARMACEUTICO' ? 'bg-blue-100 text-blue-800' :
                            'bg-green-100 text-green-800'
                          }`}>
                            {usuario.rol}
                          </span>
                        </td>
                        {/* ============ CAMBIO: el td de Sucursal ahora es hermano del td de Rol, no está anidado dentro ============ */}
                        <td className="border p-3">
                          {usuario.rol === 'ADMIN'
                            ? 'Todas'
                            : (sucursales.find(s => s.idAlmacen === usuario.idAlmacen)?.nombreSucursal || '—')}
                        </td>
                        {/* ============ FIN ============ */}
                        <td className="border p-3 text-center space-x-2">
                          <button
                            onClick={() => abrirModalUsuario(usuario)}
                            className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                            title="Editar"
                          >
                            ✏️
                          </button>
                          <button
                            onClick={() => eliminarUsuario(usuario.idUsuario)}
                            className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                            title="Eliminar"
                          >
                            🗑️
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* TAB CLIENTES */}
      {activeTab === 'clientes' && (
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-semibold">Clientes Registrados</h2>
            <button
              onClick={() => abrirModalCliente()}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center gap-2"
            >
              ➕ Nuevo Cliente
            </button>
          </div>

          {loading ? (
            <p className="text-center py-4">Cargando...</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="border p-3 text-left">ID</th>
                    <th className="border p-3 text-left">Nombre Completo</th>
                    <th className="border p-3 text-left">DNI</th>
                    <th className="border p-3 text-left">RUC</th>
                    <th className="border p-3 text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {clientes.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="border p-3 text-center text-gray-500">
                        No hay clientes registrados
                      </td>
                    </tr>
                  ) : (
                    clientes.map((cliente) => (
                      <tr key={cliente.idCliente} className="hover:bg-gray-50">
                        <td className="border p-3">{cliente.idCliente}</td>
                        <td className="border p-3">{cliente.nombreCompleto}</td>
                        <td className="border p-3">{cliente.dni}</td>
                        <td className="border p-3">{cliente.ruc || '-'}</td>
                        <td className="border p-3 text-center space-x-2">
                          <button
                            onClick={() => abrirModalCliente(cliente)}
                            className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                            title="Editar"
                          >
                            ✏️
                          </button>
                          <button
                            onClick={() => eliminarCliente(cliente.idCliente)}
                            className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                            title="Eliminar"
                          >
                            🗑️
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* MODAL */}
      {modalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md max-h-[90vh] overflow-y-auto">
            <h3 className="text-xl font-bold mb-4">
              {editingId ? 'Editar' : 'Nuevo'} {modalType === 'usuario' ? 'Usuario' : 'Cliente'}
            </h3>

              {modalType === 'usuario' ? (
                <div className="space-y-4">
                  {/*ORDEN CORRECTO - PRIMERO: Datos personales */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
                    <input
                      type="text"
                      placeholder="Nombre"
                      className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      value={usuarioForm.nombre}
                      onChange={(e) => setUsuarioForm({...usuarioForm, nombre: e.target.value})}
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Apellido Paterno</label>
                    <input
                      type="text"
                      placeholder="Apellido Paterno"
                      className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      value={usuarioForm.apat}
                      onChange={(e) => setUsuarioForm({...usuarioForm, apat: e.target.value})}
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Apellido Materno</label>
                    <input
                      type="text"
                      placeholder="Apellido Materno"
                      className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      value={usuarioForm.amat}
                      onChange={(e) => setUsuarioForm({...usuarioForm, amat: e.target.value})}
                    />
                  </div>

                  {/*SEGUNDO: Rol selector */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Rol</label>
                    <select
                      className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      value={usuarioForm.rol}
                      onChange={(e) => setUsuarioForm({...usuarioForm, rol: e.target.value})}
                    >
                      <option value="ADMIN">ADMIN</option>
                      <option value="FARMACEUTICO">FARMACEUTICO</option>
                      <option value="REPARTIDOR">REPARTIDOR</option>
                    </select>
                  </div>
                  {/* ============ AÑADIR: selector de sucursal, solo visible si el rol no es ADMIN ============ */}
                    {usuarioForm.rol !== 'ADMIN' && (
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Sucursal asignada</label>
                        <select
                          className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          value={usuarioForm.idAlmacen}
                          onChange={(e) => setUsuarioForm({...usuarioForm, idAlmacen: e.target.value})}
                          required
                        >
                          <option value="">-- Selecciona una sucursal --</option>
                          {sucursales.map((s) => (
                            <option key={s.idAlmacen} value={s.idAlmacen}>{s.nombreSucursal}</option>
                          ))}
                        </select>
                      </div>
                    )}
                    {/* ============ FIN ============ */}
                  {/*TERCERO: Condicional - Username/Password SOLO si NO es REPARTIDOR */}
                  {usuarioForm.rol !== 'REPARTIDOR' && (
                    <>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                        <input
                          type="text"
                          placeholder="Username"
                          className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          value={usuarioForm.username}
                          onChange={(e) => setUsuarioForm({...usuarioForm, username: e.target.value})}
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Contraseña {editingId && '(dejar vacío para no cambiar)'}
                        </label>
                        <input
                          type="password"
                          placeholder="Contraseña"
                          className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          value={usuarioForm.password}
                          onChange={(e) => setUsuarioForm({...usuarioForm, password: e.target.value})}
                        />
                      </div>
                    </>
                  )}

                  {/*CUARTO: Nota si es REPARTIDOR */}
                  {usuarioForm.rol === 'REPARTIDOR' && (
                    <div className="p-3 bg-blue-50 border border-blue-200 rounded text-sm text-blue-800">
                      Username y contraseña se generan automáticamente para Repartidores
                    </div>
                  )}

                  {/* BOTONES al final */}
                  <div className="flex space-x-2 pt-4">
                    <button
                      onClick={guardarUsuario}
                      className="flex-1 bg-blue-500 text-white p-2 rounded hover:bg-blue-600 font-medium"
                    >
                      {editingId ? 'Actualizar' : 'Crear'}
                    </button>
                    <button
                      onClick={() => setModalOpen(false)}
                      className="flex-1 bg-gray-300 text-gray-700 p-2 rounded hover:bg-gray-400 font-medium"
                    >
                      Cancelar
                    </button>
                  </div>
                </div>
              ) : (
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
                  <input
                    type="text"
                    placeholder="Nombre"
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={clienteForm.nombre}
                    onChange={(e) => setClienteForm({...clienteForm, nombre: e.target.value})}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Apellido Paterno</label>
                  <input
                    type="text"
                    placeholder="Apellido Paterno"
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={clienteForm.apellidoPat}
                    onChange={(e) => setClienteForm({...clienteForm, apellidoPat: e.target.value})}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Apellido Materno</label>
                  <input
                    type="text"
                    placeholder="Apellido Materno"
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={clienteForm.apellidoMat}
                    onChange={(e) => setClienteForm({...clienteForm, apellidoMat: e.target.value})}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">DNI</label>
                  <input
                    type="number"
                    placeholder="DNI"
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={clienteForm.dni}
                    onChange={(e) => setClienteForm({...clienteForm, dni: e.target.value})}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">RUC (Opcional - 11 dígitos)</label>
                  <input
                    type="number"
                    placeholder="RUC (11 dígitos)"
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={clienteForm.ruc}
                    onChange={(e) => setClienteForm({...clienteForm, ruc: e.target.value})}
                    min="10000000000"
                    max="99999999999"
                  />
                </div>
                <div className="flex space-x-2 pt-4">
                  <button
                    onClick={guardarCliente}
                    className="flex-1 bg-blue-500 text-white p-2 rounded hover:bg-blue-600 font-medium"
                  >
                    {editingId ? 'Actualizar' : 'Crear'}
                  </button>
                  <button
                    onClick={() => setModalOpen(false)}
                    className="flex-1 bg-gray-300 text-gray-700 p-2 rounded hover:bg-gray-400 font-medium"
                  >
                    Cancelar
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}