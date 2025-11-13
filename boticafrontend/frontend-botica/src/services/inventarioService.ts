// src/services/inventarioService.ts

const API_URL = 'http://localhost:8080/boticafarma';

const getAuthHeaders = () => {
  const token = localStorage.getItem('token');

  console.log('Token en localStorage:', token ? 'Existe' : 'NO EXISTE');
  console.log('Token completo:', token);

  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : '',
  };
};

// ========== PRODUCTOS ==========
export const obtenerProductos = async (page = 0, size = 10) => {
  const response = await fetch(
    `${API_URL}/productos?page=${page}&size=${size}`,
    { headers: getAuthHeaders() }
  );
  return response.json();
};

export const buscarProductos = async (
  nombre?: string,
  idCategoria?: number,
  idMarca?: number,
  page = 0,
  size = 10
) => {
  const params = new URLSearchParams();
  if (nombre) params.append('nombre', nombre);
  if (idCategoria) params.append('idCategoria', idCategoria.toString());
  if (idMarca) params.append('idMarca', idMarca.toString());
  params.append('page', page.toString());
  params.append('size', size.toString());

  const response = await fetch(
    `${API_URL}/productos/buscar?${params.toString()}`,
    { headers: getAuthHeaders() }
  );
  return response.json();
};

export const crearProducto = async (producto: any) => {
  const response = await fetch(`${API_URL}/productos`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(producto),
  });
  return response.json();
};

export const actualizarProducto = async (id: number, producto: any) => {
  const response = await fetch(`${API_URL}/productos/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(producto),
  });
  return response.json();
};

export const eliminarProducto = async (id: number) => {
  const response = await fetch(`${API_URL}/productos/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  return response.json();
};

// ========== PROVEEDORES ==========
export const obtenerProveedores = async () => {
  const response = await fetch(`${API_URL}/proveedores`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};

export const crearProveedor = async (proveedor: any) => {
  const response = await fetch(`${API_URL}/proveedores`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(proveedor),
  });
  return response.json();
};

export const actualizarProveedor = async (id: number, proveedor: any) => {
  const response = await fetch(`${API_URL}/proveedores/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(proveedor),
  });
  return response.json();
};

export const eliminarProveedor = async (id: number) => {
  const response = await fetch(`${API_URL}/proveedores/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  return response.json();
};

// ========== CATEGORÍAS ==========
export const obtenerCategorias = async () => {
  const response = await fetch(`${API_URL}/categorias`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};

export const crearCategoria = async (categoria: any) => {
  const response = await fetch(`${API_URL}/categorias`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(categoria),
  });
  return response.json();
};

export const eliminarCategoria = async (id: number) => {
  const response = await fetch(`${API_URL}/categorias/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  return response.json();
};

// ========== MARCAS ==========
export const obtenerMarcas = async () => {
  const response = await fetch(`${API_URL}/marcas`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};

export const obtenerMarcasPorProveedor = async (idProveedor: number) => {
  const response = await fetch(`${API_URL}/marcas/proveedor/${idProveedor}`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};

export const crearMarca = async (marca: any) => {
  const response = await fetch(`${API_URL}/marcas`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(marca),
  });
  return response.json();
};

export const eliminarMarca = async (id: number) => {
  const response = await fetch(`${API_URL}/marcas/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  return response.json();
};

// ========== LOTES ==========

export const crearLote = async (lote: any) => {
  console.log('📦 Enviando lote:', lote);
  console.log('🔑 Headers:', getAuthHeaders());
  
  const response = await fetch(`${API_URL}/lotes`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(lote),
  });
  
  console.log('📥 Status:', response.status);
  
  return response.json();
};
/*export const crearLote = async (lote: any) => {
  const response = await fetch(`${API_URL}/lotes`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(lote),
  });
  return response.json();
};*/

export const obtenerLotesPorProducto = async (idProducto: number) => {
  const response = await fetch(`${API_URL}/lotes/producto/${idProducto}`, {
    headers: getAuthHeaders(),
  });
  return response.json();
};