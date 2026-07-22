import axios from 'axios';
import { API_BASE_URL } from '../config/api';
const API_URL = `${API_BASE_URL}/api/almacenes`;
// const API_URL = 'http://localhost:8080/api/almacenes';

export const getSucursalesByBotica = async (idBotica: string | number) => {
  const token = localStorage.getItem('token');
  const response = await axios.get(`${API_URL}/botica/${idBotica}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return response.data;
};

export const crearSucursal = async (sucursalData: { nombreSucursal: string; direccion: string; idBotica: number }) => {
  const token = localStorage.getItem('token');
  const response = await axios.post(API_URL, sucursalData, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    }
  });
  return response.data;
};

// ============ AÑADIR: actualizar sucursal ============
export const actualizarSucursal = async (idAlmacen: number, sucursalData: { nombreSucursal: string; direccion: string }) => {
  const token = localStorage.getItem('token');
  const response = await axios.put(`${API_URL}/${idAlmacen}`, sucursalData, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    }
  });
  return response.data;
};
// ============ FIN ============

// ============ AÑADIR: eliminar sucursal ============
export const eliminarSucursal = async (idAlmacen: number) => {
  const token = localStorage.getItem('token');
  const response = await axios.delete(`${API_URL}/${idAlmacen}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return response.data;
};
// ============ FIN ============