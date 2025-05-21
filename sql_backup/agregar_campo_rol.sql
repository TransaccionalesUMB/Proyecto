-- Añadir campo de rol a la tabla users con valor por defecto 'CLIENTE'
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'CLIENTE';

-- Actualizar usuarios existentes para asignar el rol ADMIN al primer usuario (suponiendo que es el administrador)
UPDATE users SET role = 'ADMIN' WHERE id = 1;

-- Verificar la estructura actualizada
DESCRIBE users;

-- Verificar los datos actualizados
SELECT id, email, role FROM users;
