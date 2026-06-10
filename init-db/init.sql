-- ============================================================
-- SCRIPT DE INICIALIZACIÓN AUTOMÁTICA PARA BOTICAFARMA (UTF-8)
-- ============================================================
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS boticafarma;
USE boticafarma;

SET FOREIGN_KEY_CHECKS = 0;

-- --- Estructura e Inserts: proveedor ---
DROP TABLE IF EXISTS `proveedor`;
CREATE TABLE `proveedor` (
  `idProveedor` int NOT NULL AUTO_INCREMENT,
  `nombre_proveedor` varchar(255) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`idProveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `proveedor` VALUES 
(1,'Laboratorios FarmaPerú','Av. Arequipa 1234, Lima','contacto@farmaperu.com'),
(2,'Distribuidora SaludTotal','Jr. Los Medicamentos 456, Callao','ventas@saludtotal.pe'),
(3,'Insumos Médicos Andinos','Calle Comercio 789, Cusco','info@andinos.com'),
(4,'Botica Central S.A.','Av. Grau 1010, Trujillo','boticacentral@gmail.com'),
(5,'Farmacéutica San Marcos','Av. Universitaria 555, Lima','proveedores@sanmarcos.pe'),
(6,'Teva Peru S.A','Av. la Molina Nro. 135','tevaPeru@gmail.com'),
(7,'Estado Peruano','Av. Salaverry 801 - Jesus Maria - Lima - Lima - Perú - 15072','atenciontramite@minsa.gob.pe');

-- --- Estructura e Inserts: almacen ---
DROP TABLE IF EXISTS `almacen`;
CREATE TABLE `almacen` (
  `idAlmacen` int NOT NULL AUTO_INCREMENT,
  `idProveedor` int NOT NULL,
  `latitud` decimal(10,7) NOT NULL,
  `longitud` decimal(10,7) NOT NULL,
  PRIMARY KEY (`idAlmacen`),
  KEY `fk_Almacen_Proveedor1_idx` (`idProveedor`),
  CONSTRAINT `fk_Almacen_Proveedor1` FOREIGN KEY (`idProveedor`) REFERENCES `proveedor` (`idProveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --- Estructura e Inserts: categoria ---
DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(255) NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `categoria` VALUES 
(1,'Analgésicos y Antiinflamatorios'),
(2,'Antibióticos'),
(3,'Antialérgicos y Antihistamínicos'),
(4,'Antigripales y Resfríos'),
(5,'Gastrointestinales'),
(6,'Vitaminas y Suplementos'),
(7,'Cuidado Personal'),
(8,'Cuidado del Bebé'),
(9,'Primeros Auxilios'),
(10,'Anticonceptivos y Salud Sexual'),
(11,'Equipos Médicos y Accesorios'),
(12,'Dermatológicos'),
(13,'Oftálmicos y Óticos'),
(14,'Sistema Nervioso y Tranquilizantes');

-- --- Estructura e Inserts: cliente ---
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `idCliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `apellido_pat` varchar(255) NOT NULL,
  `apellido_mat` varchar(255) NOT NULL,
  `DNI` int NOT NULL,
  `RUC` bigint DEFAULT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `cliente` VALUES (1,'Cliente','Boticafarma','Comun',0,NULL);

-- --- Estructura e Inserts: usuario ---
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `apat` varchar(255) NOT NULL,
  `amat` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('ADMIN','FARMACEUTICO','ALMACENERO') NOT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `usuario` VALUES 
(1,'Gabriel Emilio','Tang','Vivanco','admin','$2a$10$IzH1WoFA0yiBn1iPgvsSZuN2jqh/rAjtgv5M6qxfFDf71HlbWMqxG','ADMIN'),
(2,'Alexander Manuel','Soto','Hidalgo','alexsoto','$2a$10$z1WIUSbOXrJz4uB1x/kBCO42QEigZAaCFj2KTsi2wPry9xT861WVe','ADMIN'),
(3,'Jose Francisco','Mallqui','Meza','pokachu201','$2a$10$tZ.aEFKpuvt3a9XityYP3O5OrAgToSFFJ46AChOie3iiXdMtszRXm','FARMACEUTICO');

-- --- Estructura e Inserts: compra ---
DROP TABLE IF EXISTS `compra`;
CREATE TABLE `compra` (
  `idCompra` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idAlmacen` int DEFAULT NULL,
  `fecha` timestamp NOT NULL,
  `total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idCompra`),
  KEY `fk_Compra_Usuario1_idx` (`idUsuario`),
  KEY `fk_Compra_Almacen2_idx` (`idAlmacen`),
  CONSTRAINT `fk_Compra_Almacen2` FOREIGN KEY (`idAlmacen`) REFERENCES `almacen` (`idAlmacen`),
  CONSTRAINT `fk_Compra_Usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `compra` VALUES 
(1,1,NULL,'2025-11-11 07:37:56',2550.00),
(2,1,NULL,'2025-11-11 08:38:46',70000.00),
(3,1,NULL,'2025-11-12 00:57:21',5000.00),
(4,1,NULL,'2025-11-12 01:03:22',17500.00),
(5,1,NULL,'2025-11-12 01:04:34',50000.00),
(6,1,NULL,'2025-11-12 01:05:22',12500.00);

-- --- Estructura e Inserts: lote ---
DROP TABLE IF EXISTS `lote`;
CREATE TABLE `lote` (
  `idLote` int NOT NULL AUTO_INCREMENT,
  `idProducto` int NOT NULL,
  `idCompra` int NOT NULL,
  `codigo_lote` varchar(50) NOT NULL,
  `cantidad_inicial` int NOT NULL,
  `cantidad_actual` int NOT NULL,
  `fecha_ingreso` date NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `precio_compra` decimal(10,2) NOT NULL,
  `estado_lote` enum('DISPONIBLE','PROXIMO_A_VENCER','VENCIDO','AGOTADO') NOT NULL,
  PRIMARY KEY (`idLote`),
  KEY `fk_Lote_Producto_idx` (`idProducto`),
  KEY `fk_Lote_Compra_idx` (`idCompra`),
  CONSTRAINT `fk_Lote_Compra` FOREIGN KEY (`idCompra`) REFERENCES `compra` (`idCompra`),
  CONSTRAINT `fk_Lote_Producto` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `lote` VALUES 
(1,1,1,'LT-01-01-10112025-001',100,94,'2025-11-10','2026-12-31',25.50,'DISPONIBLE'),
(2,2,2,'LT-02-02-10112025-002',100,98,'2025-11-10','2027-12-25',700.00,'DISPONIBLE'),
(3,8,3,'LT-01-07-11112025-001',100,100,'2025-11-11','2027-05-05',50.00,'DISPONIBLE'),
(4,2,4,'LT-02-02-11112025-002',50,50,'2025-11-11','2027-12-29',350.00,'DISPONIBLE'),
(5,6,5,'LT-01-06-11112025-003',50,49,'2025-11-11','2027-05-05',1000.00,'DISPONIBLE'),
(6,6,6,'LT-01-06-11112025-004',25,25,'2025-11-11','2027-08-04',500.00,'DISPONIBLE');

-- --- Estructura e Inserts: marca ---
DROP TABLE IF EXISTS `marca`;
CREATE TABLE `marca` (
  `idMarca` int NOT NULL AUTO_INCREMENT,
  `nombre_marca` varchar(60) NOT NULL,
  `idProveedor` int NOT NULL,
  PRIMARY KEY (`idMarca`),
  KEY `fk_Marca_Proveedor1_idx` (`idProveedor`),
  CONSTRAINT `fk_Marca_Proveedor1` FOREIGN KEY (`idProveedor`) REFERENCES `proveedor` (`idProveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `marca` VALUES (1,'Panadol',1),(2,'Redoxon',2),(3,'Listerine',3),(4,'Ensure',4),(5,'Cetaphil',5),(6,'Gingisona B',6),(7,'Genérico',7);

-- --- Estructura e Inserts: producto ---
DROP TABLE IF EXISTS `producto`;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `idCategoria` int NOT NULL,
  `idMarca` int NOT NULL,
  `nombre_producto` varchar(255) NOT NULL,
  `Precio` decimal(10,2) NOT NULL,
  `Stock` int NOT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `fk_Producto_Categoria_idx` (`idCategoria`),
  KEY `fk_Producto_Marca1_idx` (`idMarca`),
  CONSTRAINT `fk_Producto_Categoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`),
  CONSTRAINT `fk_Producto_Marca1` FOREIGN KEY (`idMarca`) REFERENCES `marca` (`idMarca`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `producto` VALUES 
(1,1,1,'Panadol Forte 500mg',5.50,94),
(2,2,2,'Redoxon Vitamina C 1g',8.90,148),
(3,4,3,'Listerine Cool Mint 500ml',12.50,0),
(4,5,4,'Ensure Nutricional 400g',25.00,0),
(5,3,5,'Cetaphil Crema Hidratante 250ml',35.00,0),
(6,1,6,'Gingisona SPRAY',26.00,74),
(7,1,6,'Gingisona PASTILLA MASTICABLE',3.50,0),
(8,1,7,'Paracetamol 500g',1.00,100);

-- --- Estructura e Inserts: detalle_compra ---
DROP TABLE IF EXISTS `detalle_compra`;
CREATE TABLE `detalle_compra` (
  `idDetalle_compra` int NOT NULL AUTO_INCREMENT,
  `idCompra` int NOT NULL,
  `idProducto` int NOT NULL,
  `idLote` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idDetalle_compra`),
  KEY `fk_Detalle_compra_Compra1_idx` (`idCompra`),
  KEY `fk_Detalle_compra_Producto1_idx` (`idProducto`),
  KEY `fk_Detalle_compra_Lote_idx` (`idLote`),
  CONSTRAINT `fk_Detalle_compra_Compra1` FOREIGN KEY (`idCompra`) REFERENCES `compra` (`idCompra`),
  CONSTRAINT `fk_Detalle_compra_Lote` FOREIGN KEY (`idLote`) REFERENCES `lote` (`idLote`),
  CONSTRAINT `fk_Detalle_compra_Producto1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --- Estructura e Inserts: detalle_venta ---
DROP TABLE IF EXISTS `detalle_venta`;
CREATE TABLE `detalle_venta` (
  `idDetalle_venta` int NOT NULL AUTO_INCREMENT,
  `idVenta` int NOT NULL,
  `idProducto` int NOT NULL,
  `idCliente` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idDetalle_venta`),
  KEY `fk_Detalle_venta_Venta1_idx` (`idVenta`),
  KEY `fk_Detalle_venta_Producto1_idx` (`idProducto`),
  KEY `fk_Detalle_venta_Cliente1_idx` (`idCliente`),
  CONSTRAINT `fk_Detalle_venta_Cliente1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `fk_Detalle_venta_Producto1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `fk_Detalle_venta_Venta1` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `detalle_venta` VALUES 
(1,2,1,1,2,5.50,11.00),(2,2,2,1,1,8.90,8.90),(3,3,6,1,1,26.00,26.00),
(4,4,1,1,1,5.50,5.50),(5,4,2,1,1,8.90,8.90),(6,5,1,1,1,5.50,5.50),
(7,6,1,1,1,5.50,5.50),(8,7,1,1,1,5.50,5.50);

-- --- Estructura e Inserts: detalle_venta_lote ---
DROP TABLE IF EXISTS `detalle_venta_lote`;
CREATE TABLE `detalle_venta_lote` (
  `idDetalle_venta_lote` int NOT NULL AUTO_INCREMENT,
  `idDetalle_venta` int NOT NULL,
  `idLote` int NOT NULL,
  `cantidad_descontada` int NOT NULL,
  PRIMARY KEY (`idDetalle_venta_lote`),
  KEY `fk_DVL_Detalle_venta_idx` (`idDetalle_venta`),
  KEY `fk_DVL_Lote_idx` (`idLote`),
  CONSTRAINT `fk_DVL_Detalle_venta` FOREIGN KEY (`idDetalle_venta`) REFERENCES `detalle_venta` (`idDetalle_venta`),
  CONSTRAINT `fk_DVL_Lote` FOREIGN KEY (`idLote`) REFERENCES `lote` (`idLote`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `detalle_venta_lote` VALUES (1,1,1,2),(2,2,2,1),(3,3,5,1),(4,4,1,1),(5,5,2,1),(6,6,1,1),(7,7,1,1),(8,8,1,1);

-- --- Estructura e Inserts: producto_almacen ---
DROP TABLE IF EXISTS `producto_almacen`;
CREATE TABLE `producto_almacen` (
  `idProducto_Almacen` int NOT NULL AUTO_INCREMENT,
  `idProducto` int NOT NULL,
  `idAlmacen` int NOT NULL,
  PRIMARY KEY (`idProducto_Almacen`),
  KEY `fk_Producto_Almacen_Producto1_idx` (`idProducto`),
  KEY `fk_Producto_Almacen_Almacen1_idx` (`idAlmacen`),
  CONSTRAINT `fk_Producto_Almacen_Almacen1` FOREIGN KEY (`idAlmacen`) REFERENCES `almacen` (`idAlmacen`),
  CONSTRAINT `fk_Producto_Almacen_Producto1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --- Estructura e Inserts: venta ---
DROP TABLE IF EXISTS `venta`;
CREATE TABLE `venta` (
  `idVenta` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `fecha` timestamp NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `metodopago` enum('EFECTIVO','TARJETA','OTRO') NOT NULL,
  `tipo_venta` enum('DELIVERY','MOSTRADOR') DEFAULT NULL,
  PRIMARY KEY (`idVenta`),
  KEY `fk_Venta_Usuario1_idx` (`idUsuario`),
  CONSTRAINT `fk_Venta_Usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `venta` VALUES 
(2,1,'2025-11-12 03:29:48',19.90,'EFECTIVO',NULL),(3,1,'2025-11-12 03:34:45',26.00,'EFECTIVO',NULL),
(4,1,'2025-11-12 08:00:34',14.40,'EFECTIVO',NULL),(5,1,'2025-11-26 06:39:01',5.50,'EFECTIVO',NULL),
(6,1,'2025-11-28 12:23:04',5.50,'EFECTIVO',NULL),(7,1,'2025-11-28 12:40:48',5.50,'EFECTIVO',NULL);

SET FOREIGN_KEY_CHECKS = 1;