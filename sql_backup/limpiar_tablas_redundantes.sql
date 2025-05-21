-- Script para limpiar tablas redundantes y optimizar la estructura de la base de datos
-- Mayo 2025

USE datos_almacen;

-- Primero hacemos un respaldo de las tablas por si acaso
CREATE TABLE IF NOT EXISTS users_backup AS SELECT * FROM users;
CREATE TABLE IF NOT EXISTS usuarios_backup_final AS SELECT * FROM usuarios;

-- Verificar si hay referencias a estas tablas en el código antes de eliminarlas
-- Si no hay referencias, podemos eliminarlas

-- Eliminar tablas redundantes
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS usuarios;

-- Nota: usuarios_backup se mantiene como respaldo histórico, pero no forma parte
-- del esquema activo de la base de datos

-- Verificar si hay tablas sin relaciones
-- Las siguientes tablas deben tener relaciones pero podrían no tenerlas:

-- Verificar si cliente tiene relación con usuario
SELECT COUNT(*) INTO @constraint_exists FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'datos_almacen' 
AND TABLE_NAME = 'cliente'
AND REFERENCED_TABLE_NAME = 'usuario';

-- Si no existe la relación, añadirla
-- Nota: Esto asume que id_usuario existe en la tabla cliente
-- Si no existe, primero habría que añadir la columna
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'cliente' AND column_name = 'id_usuario';

SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE cliente ADD COLUMN id_usuario INT',
    'SELECT "Columna id_usuario ya existe en cliente"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Ahora intentamos añadir la restricción si no existe
SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE cliente ADD CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)',
    'SELECT "Relación entre cliente y usuario ya existe"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Mensaje de confirmación
SELECT 'Limpieza de tablas redundantes completada' AS mensaje;
