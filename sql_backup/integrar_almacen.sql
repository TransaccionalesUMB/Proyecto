-- Script para integrar la tabla almacen en el esquema
-- Mayo 2025

USE datos_almacen;

-- Primero, vamos a verificar si hay datos en la tabla almacen
SELECT COUNT(*) AS registros_en_almacen FROM almacen;

-- Vamos a establecer la relación entre almacen y sucursal
-- Esto asume que almacen es un nivel superior a sucursal (un almacén puede tener varias sucursales)
ALTER TABLE sucursal
ADD COLUMN id_almacen INT,
ADD CONSTRAINT fk_sucursal_almacen FOREIGN KEY (id_almacen) REFERENCES almacen(id_almacen);

-- Creamos un índice para mejorar el rendimiento
CREATE INDEX idx_sucursal_almacen ON sucursal(id_almacen);

-- Mensaje de confirmación
SELECT 'Relación entre almacen y sucursal establecida correctamente' AS mensaje;
