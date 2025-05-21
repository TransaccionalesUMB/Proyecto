-- Script para actualizar los tipos de datos de INT a VARCHAR en la base de datos
-- Este script modifica las columnas id_categoria y id_proveedor en varias tablas

-- 1. Modificar la tabla Categoria
ALTER TABLE Categoria 
MODIFY COLUMN id_categoria VARCHAR(20) NOT NULL;

-- 2. Modificar la tabla Proveedor
ALTER TABLE Proveedor 
MODIFY COLUMN id_proveedor VARCHAR(20) NOT NULL;

-- 3. Modificar la tabla Producto
ALTER TABLE Producto 
MODIFY COLUMN id_categoria VARCHAR(20),
MODIFY COLUMN id_proveedor VARCHAR(20);

-- 4. Modificar la tabla Lote
ALTER TABLE Lote 
MODIFY COLUMN id_proveedor VARCHAR(20);

-- Nota: Asegúrese de hacer una copia de seguridad de la base de datos antes de ejecutar este script
-- Ejecute este script con cuidado, ya que modificará la estructura de las tablas
