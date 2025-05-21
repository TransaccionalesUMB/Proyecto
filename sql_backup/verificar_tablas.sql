-- Script para verificar y corregir las tablas necesarias
USE datos_almacen;

-- Verificar si existe la tabla Categoria
CREATE TABLE IF NOT EXISTS Categoria (
    id_categoria VARCHAR(25) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Insertar categorías por defecto si no existen
INSERT IGNORE INTO Categoria (id_categoria, nombre) VALUES 
('CAT1', 'Categoría General'),
('CAT2', 'Electrónicos'),
('CAT3', 'Oficina'),
('CAT4', 'Muebles');

-- Verificar si existe la tabla Proveedor
CREATE TABLE IF NOT EXISTS Proveedor (
    id_proveedor VARCHAR(25) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100)
);

-- Insertar proveedores por defecto si no existen
INSERT IGNORE INTO Proveedor (id_proveedor, nombre, contacto) VALUES 
('PROV1', 'Proveedor General', 'Contacto General'),
('PROV2', 'Electrónicos S.A.', 'Juan Pérez'),
('PROV3', 'Oficina Total', 'María López'),
('PROV4', 'Muebles Modernos', 'Carlos Rodríguez');

-- Verificar si existe la tabla Almacen
CREATE TABLE IF NOT EXISTS Almacen (
    id_almacen INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200)
);

-- Insertar almacén por defecto si no existe
INSERT IGNORE INTO Almacen (id_almacen, nombre, ubicacion) VALUES 
(1, 'Almacén Principal', 'Ubicación Principal');

-- Verificar estructura de la tabla Producto
DESCRIBE Producto;

-- Verificar estructura de la tabla Stock
DESCRIBE Stock;
