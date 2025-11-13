// src/types/inventario.ts

export interface Producto {
  idProducto: number;
  nombre_producto: string;
  precio: number;
  stock: number;
  idCategoria: number;
  nombreCategoria: string;
  idMarca: number;
  nombreMarca: string;
  idProveedor: number;
  nombreProveedor: string;
}

export interface CreateProducto {
  nombre_producto: string;
  precio: number;
  stock: number;
  idCategoria: number;
  idMarca: number;
}

export interface Proveedor {
  idProveedor: number;
  nombreProveedor: string;
  direccion: string;
  email: string;
}

export interface Categoria {
  idCategoria: number;
  nombre_categoria: string;
}

export interface Marca {
  idMarca: number;
  nombreMarca: string;
  idProveedor: number;
  nombreProveedor: string;
}