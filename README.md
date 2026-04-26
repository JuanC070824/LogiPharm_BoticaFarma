# LogiPharm - BoticaFarma 🏥💊

**LogiPharm (BoticaFarma)** es un sistema integral de gestión de inventarios y ventas diseñado específicamente para farmacias y boticas. El objetivo principal es optimizar el control de medicamentos, gestionar las ventas diarias y garantizar un seguimiento preciso del stock disponible.

## 🚀 Características Principales

* **Gestión de Inventario:** Control detallado de medicamentos, incluyendo fechas de vencimiento, lotes y categorías.
* **Punto de Venta (POS):** Interfaz intuitiva para procesar ventas de forma rápida y eficiente.
* **Gestión de Usuarios:** Roles diferenciados para administradores y personal de ventas (RBAC).
* **Reportes:** Generación de informes sobre ventas y niveles críticos de stock.
* **Base de Datos Robusta:** Estructura optimizada para el manejo de productos farmacéuticos.

## 🛠️ Stack Tecnológico

* **Backend:** Java Spring Boot
* **Frontend:** React.js
* **Base de Datos:** MySQL
* **Gestión de Dependencias:** Maven / NPM

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:
* Java JDK 17 o superior.
* Node.js y npm.
* MySQL Server.
* Un IDE (IntelliJ IDEA recomendado).

## 🔧 Instalación y Configuración

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/JuanC070824/LogiPharm_BoticaFarma.git
    ```

2.  **Configurar la Base de Datos:**
    * Crea una base de datos en MySQL llamada `boticafarma`.
    * Actualiza el archivo `src/main/resources/application.properties` con tus credenciales de MySQL.

3.  **Ejecutar el Backend:**
    ```bash
    mvn spring-boot:run
    ```

4.  **Ejecutar el Frontend:**
    ```bash
    cd frontend
    npm install
    npm start
    (también npm run dev)
    ```

## 👥 Equipo de Desarrollo (Grupo 6)

Este proyecto es desarrollado por estudiantes de Ingeniería de Software de la **UNMSM**:

* Alexander Manuel Soto Hidalgo
* Juan Carlos Loayza Oroncoy
* Gabriel Tang
* Midwar Cacya
