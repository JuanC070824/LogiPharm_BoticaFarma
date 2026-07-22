const getIdAlmacen = () => localStorage.getItem('idAlmacen');
import { API_BASE_URL } from '../config/api';
const API_URL = `${API_BASE_URL}/boticafarma`;

//const API_URL = 'http://localhost:8080/boticafarma';

const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : '',
  };
};

export const procesarVenta = async (ventaData: any) => {
  const idAlmacenActivo = getIdAlmacen();
  const ventaConAlmacen = {
    ...ventaData,
    idAlmacen: idAlmacenActivo ? Number(idAlmacenActivo) : null
  };

  const response = await fetch(`${API_URL}/ventas`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(ventaConAlmacen),
  });
  return response.json();
};

export const obtenerVentas = async () => {
  const response = await fetch(`${API_URL}/ventas`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};

export const obtenerVentaPorId = async (idVenta: number) => {
  const response = await fetch(`${API_URL}/ventas/${idVenta}`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};