// src/services/ventasService.ts

const API_URL = 'http://localhost:8080/boticafarma';

const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : '',
  };
};

export const procesarVenta = async (ventaData: any) => {
  const response = await fetch(`${API_URL}/ventas`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(ventaData),
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