-- Script para eliminar la tabla redundante 'usuarios'
-- Creado: 13/05/2025

-- Primero hacemos una copia de seguridad de la estructura por si acaso
CREATE TABLE IF NOT EXISTS usuarios_backup LIKE usuarios;

-- Verificamos que la tabla 'usuarios' esté vacía antes de eliminarla
SELECT COUNT(*) AS registros_en_usuarios FROM usuarios;

-- Eliminamos la tabla redundante
DROP TABLE IF EXISTS usuarios;

-- Mensaje de confirmación
SELECT 'Tabla usuarios eliminada correctamente' AS mensaje;
