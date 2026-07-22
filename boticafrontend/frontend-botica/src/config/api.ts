// Detecta la variable de entorno de Vite o usa localhost si estás programando local
export const API_BASE_URL = (import.meta as any).env?.VITE_API_URL || 'http://localhost:8080';