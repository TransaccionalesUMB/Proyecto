-- Script para corregir las relaciones entre tablas después de actualizar los tipos de datos
-- Este script actualiza las consultas y relaciones para que funcionen con los nuevos tipos de datos

-- 1. Eliminar las referencias a ubicación y lote en la tabla stock
-- Primero verificamos si existen estas columnas y luego las eliminamos si es necesario

-- Verificar y eliminar la columna id_ubicacion si existe
SET @exist := (SELECT COUNT(*) 
               FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_SCHEMA = 'datos_almacen' 
               AND TABLE_NAME = 'stock' 
               AND COLUMN_NAME = 'id_ubicacion');

SET @query = IF(@exist > 0, 
               'ALTER TABLE stock DROP COLUMN id_ubicacion',
               'SELECT "La columna id_ubicacion no existe en la tabla stock"');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar y eliminar la columna id_lote si existe
SET @exist := (SELECT COUNT(*) 
               FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_SCHEMA = 'datos_almacen' 
               AND TABLE_NAME = 'stock' 
               AND COLUMN_NAME = 'id_lote');

SET @query = IF(@exist > 0, 
               'ALTER TABLE stock DROP COLUMN id_lote',
               'SELECT "La columna id_lote no existe en la tabla stock"');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. Crear una tabla de relación entre ubicaciones y productos
-- Esta tabla reemplazará la relación directa que existía antes

CREATE TABLE IF NOT EXISTS ubicacion_producto (
  id_ubicacion_producto INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  id_ubicacion INT NOT NULL,
  cantidad INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id_ubicacion_producto),
  KEY idx_ubicacion_producto_producto (id_producto),
  KEY idx_ubicacion_producto_ubicacion (id_ubicacion),
  CONSTRAINT fk_ubicacion_producto_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto) ON DELETE CASCADE,
  CONSTRAINT fk_ubicacion_producto_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES ubicacion_bodega (id_ubicacion) ON DELETE CASCADE
);

-- 3. Crear una tabla de relación entre lotes y stock
-- Esta tabla reemplazará la relación directa que existía antes

CREATE TABLE IF NOT EXISTS stock_lote (
  id_stock_lote INT NOT NULL AUTO_INCREMENT,
  id_stock INT NOT NULL,
  id_lote INT NOT NULL,
  cantidad INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id_stock_lote),
  KEY idx_stock_lote_stock (id_stock),
  KEY idx_stock_lote_lote (id_lote),
  CONSTRAINT fk_stock_lote_stock FOREIGN KEY (id_stock) REFERENCES stock (id_stock) ON DELETE CASCADE,
  CONSTRAINT fk_stock_lote_lote FOREIGN KEY (id_lote) REFERENCES lote (id_lote) ON DELETE CASCADE
);

-- 4. Actualizar las vistas o procedimientos almacenados que puedan estar usando las columnas eliminadas

-- Vista para obtener información de stock con ubicaciones
CREATE OR REPLACE VIEW vista_stock_ubicaciones AS
SELECT 
    s.id_stock,
    s.id_producto,
    p.nombre AS nombre_producto,
    s.id_bodega,
    b.nombre AS nombre_bodega,
    s.cantidad AS cantidad_total,
    up.id_ubicacion,
    ub.pasillo,
    ub.estante,
    ub.nivel,
    ub.posicion,
    up.cantidad AS cantidad_en_ubicacion
FROM 
    stock s
JOIN 
    producto p ON s.id_producto = p.id_producto
JOIN 
    bodega b ON s.id_bodega = b.id_bodega
LEFT JOIN 
    ubicacion_producto up ON s.id_producto = up.id_producto
LEFT JOIN 
    ubicacion_bodega ub ON up.id_ubicacion = ub.id_ubicacion;

-- Vista para obtener información de stock con lotes
CREATE OR REPLACE VIEW vista_stock_lotes AS
SELECT 
    s.id_stock,
    s.id_producto,
    p.nombre AS nombre_producto,
    s.id_bodega,
    b.nombre AS nombre_bodega,
    s.cantidad AS cantidad_total,
    sl.id_lote,
    l.numero_lote,
    l.fecha_fabricacion,
    l.fecha_caducidad,
    sl.cantidad AS cantidad_en_lote
FROM 
    stock s
JOIN 
    producto p ON s.id_producto = p.id_producto
JOIN 
    bodega b ON s.id_bodega = b.id_bodega
LEFT JOIN 
    stock_lote sl ON s.id_stock = sl.id_stock
LEFT JOIN 
    lote l ON sl.id_lote = l.id_lote;

-- 5. Crear un procedimiento almacenado para registrar entrada de inventario
DELIMITER //
DROP PROCEDURE IF EXISTS registrar_entrada_inventario //
CREATE PROCEDURE registrar_entrada_inventario(
    IN p_id_producto INT,
    IN p_id_bodega INT,
    IN p_id_ubicacion INT,
    IN p_cantidad INT,
    IN p_numero_lote VARCHAR(50),
    IN p_fecha_caducidad DATE
)
BEGIN
    DECLARE v_id_stock INT;
    DECLARE v_id_lote INT;
    DECLARE v_id_ubicacion_producto INT;
    
    -- Buscar o crear stock
    SELECT id_stock INTO v_id_stock FROM stock 
    WHERE id_producto = p_id_producto AND id_bodega = p_id_bodega;
    
    IF v_id_stock IS NULL THEN
        -- Crear nuevo registro de stock
        INSERT INTO stock (id_producto, id_bodega, cantidad, nombre_producto)
        SELECT id_producto, p_id_bodega, p_cantidad, nombre
        FROM producto WHERE id_producto = p_id_producto;
        
        SET v_id_stock = LAST_INSERT_ID();
    ELSE
        -- Actualizar stock existente
        UPDATE stock SET cantidad = cantidad + p_cantidad
        WHERE id_stock = v_id_stock;
    END IF;
    
    -- Si se especifica ubicación, registrar en ubicacion_producto
    IF p_id_ubicacion IS NOT NULL THEN
        SELECT id_ubicacion_producto INTO v_id_ubicacion_producto
        FROM ubicacion_producto
        WHERE id_producto = p_id_producto AND id_ubicacion = p_id_ubicacion;
        
        IF v_id_ubicacion_producto IS NULL THEN
            -- Crear nueva relación producto-ubicación
            INSERT INTO ubicacion_producto (id_producto, id_ubicacion, cantidad)
            VALUES (p_id_producto, p_id_ubicacion, p_cantidad);
        ELSE
            -- Actualizar relación existente
            UPDATE ubicacion_producto
            SET cantidad = cantidad + p_cantidad
            WHERE id_ubicacion_producto = v_id_ubicacion_producto;
        END IF;
    END IF;
    
    -- Si se especifica lote, registrar en lote y stock_lote
    IF p_numero_lote IS NOT NULL THEN
        -- Buscar lote existente o crear uno nuevo
        SELECT id_lote INTO v_id_lote FROM lote
        WHERE id_producto = p_id_producto AND numero_lote = p_numero_lote;
        
        IF v_id_lote IS NULL THEN
            -- Crear nuevo lote
            INSERT INTO lote (id_producto, numero_lote, fecha_fabricacion, fecha_caducidad, 
                             cantidad_inicial, cantidad_actual, id_proveedor)
            SELECT id_producto, p_numero_lote, CURDATE(), p_fecha_caducidad, 
                   p_cantidad, p_cantidad, id_proveedor
            FROM producto WHERE id_producto = p_id_producto;
            
            SET v_id_lote = LAST_INSERT_ID();
        ELSE
            -- Actualizar lote existente
            UPDATE lote
            SET cantidad_actual = cantidad_actual + p_cantidad
            WHERE id_lote = v_id_lote;
        END IF;
        
        -- Registrar relación stock-lote
        INSERT INTO stock_lote (id_stock, id_lote, cantidad)
        VALUES (v_id_stock, v_id_lote, p_cantidad);
    END IF;
END //
DELIMITER ;

-- 6. Crear un procedimiento almacenado para registrar salida de inventario
DELIMITER //
DROP PROCEDURE IF EXISTS registrar_salida_inventario //
CREATE PROCEDURE registrar_salida_inventario(
    IN p_id_producto INT,
    IN p_id_bodega INT,
    IN p_id_ubicacion INT,
    IN p_id_lote INT,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_id_stock INT;
    DECLARE v_id_ubicacion_producto INT;
    DECLARE v_id_stock_lote INT;
    DECLARE v_cantidad_disponible INT;
    
    -- Buscar stock
    SELECT id_stock INTO v_id_stock FROM stock 
    WHERE id_producto = p_id_producto AND id_bodega = p_id_bodega;
    
    IF v_id_stock IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay stock disponible para el producto en esta bodega';
    END IF;
    
    -- Verificar cantidad disponible
    SELECT cantidad INTO v_cantidad_disponible FROM stock
    WHERE id_stock = v_id_stock;
    
    IF v_cantidad_disponible < p_cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay suficiente stock disponible';
    END IF;
    
    -- Actualizar stock
    UPDATE stock SET cantidad = cantidad - p_cantidad
    WHERE id_stock = v_id_stock;
    
    -- Si se especifica ubicación, actualizar ubicacion_producto
    IF p_id_ubicacion IS NOT NULL THEN
        SELECT id_ubicacion_producto, cantidad INTO v_id_ubicacion_producto, v_cantidad_disponible
        FROM ubicacion_producto
        WHERE id_producto = p_id_producto AND id_ubicacion = p_id_ubicacion;
        
        IF v_id_ubicacion_producto IS NULL OR v_cantidad_disponible < p_cantidad THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No hay suficiente stock en la ubicación especificada';
        END IF;
        
        UPDATE ubicacion_producto
        SET cantidad = cantidad - p_cantidad
        WHERE id_ubicacion_producto = v_id_ubicacion_producto;
        
        -- Si la cantidad llega a cero, eliminar el registro
        DELETE FROM ubicacion_producto
        WHERE id_ubicacion_producto = v_id_ubicacion_producto AND cantidad <= 0;
    END IF;
    
    -- Si se especifica lote, actualizar stock_lote y lote
    IF p_id_lote IS NOT NULL THEN
        SELECT id_stock_lote, cantidad INTO v_id_stock_lote, v_cantidad_disponible
        FROM stock_lote
        WHERE id_stock = v_id_stock AND id_lote = p_id_lote;
        
        IF v_id_stock_lote IS NULL OR v_cantidad_disponible < p_cantidad THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No hay suficiente stock en el lote especificado';
        END IF;
        
        UPDATE stock_lote
        SET cantidad = cantidad - p_cantidad
        WHERE id_stock_lote = v_id_stock_lote;
        
        -- Actualizar cantidad en lote
        UPDATE lote
        SET cantidad_actual = cantidad_actual - p_cantidad
        WHERE id_lote = p_id_lote;
        
        -- Si la cantidad llega a cero, eliminar el registro
        DELETE FROM stock_lote
        WHERE id_stock_lote = v_id_stock_lote AND cantidad <= 0;
    END IF;
    
    -- Si el stock llega a cero, eliminar el registro
    DELETE FROM stock
    WHERE id_stock = v_id_stock AND cantidad <= 0;
END //
DELIMITER ;

-- 7. Crear un procedimiento almacenado para transferir inventario entre bodegas
DELIMITER //
DROP PROCEDURE IF EXISTS transferir_inventario //
CREATE PROCEDURE transferir_inventario(
    IN p_id_producto INT,
    IN p_id_bodega_origen INT,
    IN p_id_bodega_destino INT,
    IN p_id_ubicacion_origen INT,
    IN p_id_ubicacion_destino INT,
    IN p_id_lote INT,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_numero_lote VARCHAR(50);
    DECLARE v_fecha_caducidad DATE;
    
    -- Registrar salida del origen
    CALL registrar_salida_inventario(
        p_id_producto,
        p_id_bodega_origen,
        p_id_ubicacion_origen,
        p_id_lote,
        p_cantidad
    );
    
    IF p_id_lote IS NOT NULL THEN
        SELECT numero_lote, fecha_caducidad 
        INTO v_numero_lote, v_fecha_caducidad
        FROM lote
        WHERE id_lote = p_id_lote;
    END IF;
    
    -- Registrar entrada en el destino
    CALL registrar_entrada_inventario(
        p_id_producto,
        p_id_bodega_destino,
        p_id_ubicacion_destino,
        p_cantidad,
        v_numero_lote,
        v_fecha_caducidad
    );
    
    -- Registrar la transferencia en la tabla transferencias
    INSERT INTO transferencias (
        id_producto,
        id_bodega_origen,
        id_bodega_destino,
        cantidad,
        fecha
    ) VALUES (
        p_id_producto,
        p_id_bodega_origen,
        p_id_bodega_destino,
        p_cantidad,
        NOW()
    );
END //
DELIMITER ;

-- 8. Insertar un usuario administrador si no existe
INSERT INTO usuario (nombre, email, password, activo, id_rol)
SELECT 'David Rojas', 'rojasbotero@gmail.com', 'Admin123', 1, 1
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE email = 'rojasbotero@gmail.com'
);
