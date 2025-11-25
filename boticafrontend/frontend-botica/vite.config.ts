// vite.config.ts
import react from "@vitejs/plugin-react";
import path from "path";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    // Usamos la sintaxis de array para el alias, lo cual puede ser más robusto
    alias: [
      { 
        find: "@", 
        replacement: path.resolve(__dirname, "./src") 
      }
    ],
    // Asegura que Vite pueda encontrar las extensiones comunes de React/TS
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json'],
  }
});