import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  token?: string;
  rol?: string;
  nombre?: string;
  username?: string;
  idBotica?: number;
  idAlmacen?: number;
}

export interface RegisterBoticaDTO {
  nombreBotica: string;
  ruc?: string;
  nombreSucursal?: string;
  direccion?: string;
  nombre: string;
  apat: string;
  amat: string;
  username: string;
  email?: string;
  password: string;
}

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  try {
    const response = await axios.post(`${API_BASE_URL}/boticafarma/login`, credentials);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return error.response.data;
    }
    throw error;
  }
};

export const registerBotica = async (data: any) => {
  const response = await fetch("http://localhost:8080/api/auth/register-botica", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Error en el registro");
  }

  return await response.json();
};

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('rol');
  localStorage.removeItem('nombre');
  localStorage.removeItem('username');
  localStorage.removeItem('idBotica');
  localStorage.removeItem('idAlmacen');
};