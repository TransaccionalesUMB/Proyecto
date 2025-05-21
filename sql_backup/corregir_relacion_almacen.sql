-- Script para corregir la relación del almacén
-- Mayo 2025

USE datos_almacen;

-- Primero, eliminar la relación incorrecta que acabamos de crear
ALTER TABLE sucursal
DROP FOREIGN KEY fk_sucursal_almacen;

ALTER TABLE sucursal
DROP COLUMN id_almacen;

DROP INDEX idx_sucursal_almacen ON sucursal;

-- Ahora, establecer la relación correcta: bodega pertenece a almacen
ALTER TABLE bodega
ADD COLUMN id_almacen INT AFTER id_sucursal,
ADD CONSTRAINT fk_bodega_almacen FOREIGN KEY (id_almacen) REFERENCES almacen(id_almacen);

-- Crear un índice para mejorar el rendimiento
CREATE INDEX idx_bodega_almacen ON bodega(id_almacen);

-- Mensaje de confirmación
SELECT 'Relación corregida: bodega ahora está relacionada directamente con almacen' AS mensaje;
