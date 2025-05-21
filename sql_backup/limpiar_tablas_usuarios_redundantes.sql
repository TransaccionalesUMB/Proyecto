-- Script para limpiar tablas de usuarios redundantes
-- Mayo 2025

USE datos_almacen;

-- Primero hacemos un respaldo final por seguridad
CREATE TABLE IF NOT EXISTS users_backup_final AS SELECT * FROM users;

-- Eliminar tablas redundantes de usuarios
DROP TABLE IF EXISTS users_backup;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS usuarios_backup;
DROP TABLE IF EXISTS usuarios_backup_final;

-- Mensaje de confirmación
SELECT 'Limpieza de tablas de usuarios redundantes completada' AS mensaje;
