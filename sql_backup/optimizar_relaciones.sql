-- Script para optimizar las relaciones en la base de datos
-- Mayo 2025

USE datos_almacen;

-- =============================================
-- VERIFICACIÓN DE RELACIONES EXISTENTES
-- =============================================

-- Verificar la estructura actual
SELECT 'Estructura actual de las tablas principales' AS mensaje;

-- Verificar relaciones existentes
SELECT 
    TABLE_NAME AS tabla, 
    COLUMN_NAME AS columna,
    REFERENCED_TABLE_NAME AS tabla_referenciada,
    REFERENCED_COLUMN_NAME AS columna_referenciada
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    REFERENCED_TABLE_SCHEMA = 'datos_almacen'
    AND TABLE_SCHEMA = 'datos_almacen'
    AND (TABLE_NAME IN ('almacen', 'sucursal', 'bodega', 'ubicacion_bodega', 'stock')
         OR REFERENCED_TABLE_NAME IN ('almacen', 'sucursal', 'bodega', 'ubicacion_bodega', 'stock'));

-- =============================================
-- OPTIMIZACIÓN DE RELACIONES
-- =============================================

-- 1. Asegurar que la jerarquía almacen -> sucursal -> bodega esté correctamente establecida
-- La relación sucursal -> almacen ya existe
-- La relación bodega -> sucursal ya existe

-- 2. Verificar si hay datos en la tabla almacen
SELECT COUNT(*) AS registros_en_almacen FROM almacen;

-- 3. Si no hay datos en almacen, crear un registro predeterminado
-- Esto es útil si el sistema está diseñado para tener un único almacén principal
INSERT INTO almacen (id_almacen, nombre, ubicacion)
SELECT 1, 'Almacén Principal', 'Ubicación Central'
WHERE NOT EXISTS (SELECT 1 FROM almacen WHERE id_almacen = 1);

-- 4. Asegurar que todas las sucursales estén asociadas a un almacén
-- Esto evita tener sucursales "huérfanas"
UPDATE sucursal SET id_almacen = 1 WHERE id_almacen IS NULL;

-- 5. Asegurar que todas las bodegas estén asociadas a una sucursal
-- Esto evita tener bodegas "huérfanas"
-- Primero verificamos si hay alguna sucursal
SELECT COUNT(*) AS registros_en_sucursal FROM sucursal;

-- Si no hay sucursales, crear una predeterminada
INSERT INTO sucursal (id_sucursal, nombre, id_almacen)
SELECT 1, 'Sucursal Principal', 1
WHERE NOT EXISTS (SELECT 1 FROM sucursal WHERE id_sucursal = 1);

-- Actualizar bodegas sin sucursal
UPDATE bodega SET id_sucursal = 1 WHERE id_sucursal IS NULL;

-- 6. Verificar la relación entre stock y bodega
-- Asegurar que todos los registros de stock estén asociados a una bodega
-- Primero verificamos si hay alguna bodega
SELECT COUNT(*) AS registros_en_bodega FROM bodega;

-- Si no hay bodegas, crear una predeterminada
INSERT INTO bodega (id_bodega, nombre, id_sucursal)
SELECT 1, 'Bodega Principal', 1
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 1);

-- Actualizar registros de stock sin bodega
UPDATE stock SET id_bodega = 1 WHERE id_bodega IS NULL;

-- Mensaje de confirmación
SELECT 'Relaciones optimizadas correctamente' AS mensaje;
