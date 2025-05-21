-- Script para validar la estructura completa de la base de datos
USE datos_almacen;

-- Listar todas las tablas en la base de datos
SHOW TABLES;

-- Verificar estructura de la tabla Usuario
DESCRIBE Usuario;

-- Verificar estructura de la tabla Categoria
DESCRIBE Categoria;

-- Verificar estructura de la tabla Proveedor
DESCRIBE Proveedor;

-- Verificar estructura de la tabla Producto
DESCRIBE Producto;

-- Verificar estructura de la tabla Stock
DESCRIBE Stock;

-- Verificar estructura de la tabla Almacen
DESCRIBE Almacen;

-- Verificar datos de la tabla Usuario
SELECT * FROM Usuario LIMIT 5;

-- Verificar datos de la tabla Categoria
SELECT * FROM Categoria;

-- Verificar datos de la tabla Proveedor
SELECT * FROM Proveedor;

-- Verificar relaciones entre tablas
-- Producto - Categoria
SELECT p.id_producto, p.nombre AS producto, c.nombre AS categoria
FROM Producto p
LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria
LIMIT 5;

-- Producto - Proveedor
SELECT p.id_producto, p.nombre AS producto, pr.nombre AS proveedor
FROM Producto p
LEFT JOIN Proveedor pr ON p.id_proveedor = pr.id_proveedor
LIMIT 5;

-- Producto - Stock
SELECT p.id_producto, p.nombre AS producto, s.cantidad
FROM Producto p
LEFT JOIN Stock s ON p.id_producto = s.id_producto
LIMIT 5;
