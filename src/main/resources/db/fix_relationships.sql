-- Script para corregir las relaciones entre tablas y asegurar la consistencia de datos

-- 1. Verificar que las tablas existan y crear las que falten
-- Tabla Categoria (si no existe)
CREATE TABLE IF NOT EXISTS Categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla Proveedor (si no existe)
CREATE TABLE IF NOT EXISTS Proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT
);

-- Tabla Bodega (si no existe)
CREATE TABLE IF NOT EXISTS Bodega (
    id_bodega INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200),
    capacidad INT
);

-- Tabla UbicacionBodega (si no existe)
CREATE TABLE IF NOT EXISTS UbicacionBodega (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    id_bodega INT,
    pasillo VARCHAR(10),
    estante VARCHAR(10),
    nivel VARCHAR(10),
    FOREIGN KEY (id_bodega) REFERENCES Bodega(id_bodega)
);

-- 2. Insertar datos mínimos necesarios para el funcionamiento
-- Insertar categoría por defecto si no existe
INSERT IGNORE INTO Categoria (id_categoria, nombre, descripcion) 
VALUES (1, 'Categoría General', 'Categoría por defecto para productos sin clasificar');

-- Insertar proveedor por defecto si no existe
INSERT IGNORE INTO Proveedor (id_proveedor, nombre, contacto) 
VALUES (1, 'Proveedor General', 'Proveedor por defecto');

-- Insertar bodega por defecto si no existe
INSERT IGNORE INTO Bodega (id_bodega, nombre, ubicacion) 
VALUES (1, 'Bodega Principal', 'Ubicación Principal');

-- Insertar ubicación por defecto si no existe
INSERT IGNORE INTO UbicacionBodega (id_ubicacion, id_bodega, pasillo, estante, nivel) 
VALUES (1, 1, 'A', '01', '1');

-- 3. Corregir la tabla Producto para asegurar que los IDs de categoría y proveedor sean enteros
-- Primero, crear una copia de seguridad
CREATE TABLE IF NOT EXISTS Producto_Backup AS SELECT * FROM Producto;

-- Actualizar la estructura de la tabla Producto (si es necesario)
ALTER TABLE Producto 
MODIFY COLUMN id_categoria INT,
MODIFY COLUMN id_proveedor INT;

-- Actualizar referencias a categorías y proveedores
UPDATE Producto SET id_categoria = 1 WHERE id_categoria IS NULL OR id_categoria = '';
UPDATE Producto SET id_proveedor = 1 WHERE id_proveedor IS NULL OR id_proveedor = '';

-- 4. Corregir la tabla Lote para asegurar que el ID de proveedor sea entero
-- Primero, crear una copia de seguridad
CREATE TABLE IF NOT EXISTS Lote_Backup AS SELECT * FROM Lote;

-- Actualizar la estructura de la tabla Lote (si es necesario)
ALTER TABLE Lote 
MODIFY COLUMN id_proveedor INT;

-- Actualizar referencias a proveedores
UPDATE Lote SET id_proveedor = 1 WHERE id_proveedor IS NULL OR id_proveedor = '';

-- 5. Verificar y corregir la tabla Stock
-- Primero, crear una copia de seguridad
CREATE TABLE IF NOT EXISTS Stock_Backup AS SELECT * FROM Stock;

-- Asegurarse de que los registros de Stock tengan referencias válidas
UPDATE Stock SET id_bodega = 1 WHERE id_bodega IS NULL OR NOT EXISTS (SELECT 1 FROM Bodega WHERE Bodega.id_bodega = Stock.id_bodega);
UPDATE Stock SET id_ubicacion = 1 WHERE id_ubicacion IS NULL OR NOT EXISTS (SELECT 1 FROM UbicacionBodega WHERE UbicacionBodega.id_ubicacion = Stock.id_ubicacion);

-- Eliminar registros de Stock que referencian a productos inexistentes
DELETE FROM Stock WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE Producto.id_producto = Stock.id_producto);

-- 6. Verificar y corregir la tabla Lote
-- Eliminar registros de Lote que referencian a productos inexistentes
DELETE FROM Lote WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE Producto.id_producto = Lote.id_producto);

-- 7. Añadir índices para mejorar el rendimiento de las consultas
CREATE INDEX IF NOT EXISTS idx_producto_categoria ON Producto(id_categoria);
CREATE INDEX IF NOT EXISTS idx_producto_proveedor ON Producto(id_proveedor);
CREATE INDEX IF NOT EXISTS idx_stock_producto ON Stock(id_producto);
CREATE INDEX IF NOT EXISTS idx_stock_bodega ON Stock(id_bodega);
CREATE INDEX IF NOT EXISTS idx_stock_ubicacion ON Stock(id_ubicacion);
CREATE INDEX IF NOT EXISTS idx_stock_lote ON Stock(id_lote);
CREATE INDEX IF NOT EXISTS idx_lote_producto ON Lote(id_producto);
CREATE INDEX IF NOT EXISTS idx_lote_proveedor ON Lote(id_proveedor);
