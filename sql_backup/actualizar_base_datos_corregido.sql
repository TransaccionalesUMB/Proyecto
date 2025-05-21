-- Script para actualizar la base de datos según el modelo completo
-- Mayo 2025

USE datos_almacen;

-- =============================================
-- ACTUALIZACIÓN DE TABLAS EXISTENTES
-- =============================================

-- Asegurar que todas las tablas principales existen
CREATE TABLE IF NOT EXISTS rol (
    id_rol INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE IF NOT EXISTS permiso (
    id_permiso INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS rol_permiso (
    id_rol_permiso INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso)
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria VARCHAR(25) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS proveedor (
    id_proveedor VARCHAR(25) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    id_categoria VARCHAR(25),
    id_proveedor VARCHAR(25),
    precio DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS almacen (
    id_almacen INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS sucursal (
    id_sucursal INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion TEXT,
    id_almacen INT,
    FOREIGN KEY (id_almacen) REFERENCES almacen(id_almacen)
);

CREATE TABLE IF NOT EXISTS bodega (
    id_bodega INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_sucursal INT,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS departamento (
    id_departamento INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS empleado (
    id_empleado INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_usuario INT,
    id_departamento INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_departamento) REFERENCES departamento(id_departamento)
);

CREATE TABLE IF NOT EXISTS pedido (
    id_pedido INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    fecha DATE NOT NULL,
    estado ENUM('Pendiente', 'Completado', 'Cancelado') DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS factura (
    id_factura INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id_detalle_pedido INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_producto INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE IF NOT EXISTS detalle_factura (
    id_detalle_factura INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    id_producto INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE IF NOT EXISTS metodo_pago (
    id_metodo_pago INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS pagos (
    id_pago INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    id_metodo_pago INT,
    monto DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
    FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago)
);

CREATE TABLE IF NOT EXISTS stock (
    id_stock INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    nombre_producto VARCHAR(100),
    id_bodega INT,
    cantidad INT NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_bodega) REFERENCES bodega(id_bodega)
);

CREATE TABLE IF NOT EXISTS movimiento (
    id_movimiento INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_bodega INT,
    id_empleado INT,
    tipo ENUM('Entrada', 'Salida') NOT NULL,
    cantidad INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_bodega) REFERENCES bodega(id_bodega),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE IF NOT EXISTS transferencias (
    id_transferencia INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_bodega_origen INT,
    id_bodega_destino INT,
    cantidad INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_bodega_origen) REFERENCES bodega(id_bodega),
    FOREIGN KEY (id_bodega_destino) REFERENCES bodega(id_bodega)
);

CREATE TABLE IF NOT EXISTS log_auditoria (
    id_log INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    accion VARCHAR(200) NOT NULL,
    tabla_afectada VARCHAR(50),
    registro_afectado INT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- =============================================
-- ASEGURAR QUE LAS NUEVAS TABLAS EXISTEN
-- =============================================

-- Tabla Unidad_Medida (si no existe)
CREATE TABLE IF NOT EXISTS unidad_medida (
    id_unidad INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    es_unidad_base BOOLEAN DEFAULT FALSE
);

-- Tabla Lote (si no existe)
CREATE TABLE IF NOT EXISTS lote (
    id_lote INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    numero_lote VARCHAR(50) NOT NULL,
    fecha_fabricacion DATE,
    fecha_caducidad DATE,
    cantidad_inicial INT NOT NULL,
    cantidad_actual INT NOT NULL,
    id_proveedor VARCHAR(25),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
);

-- Tabla Ubicacion_Bodega (si no existe)
CREATE TABLE IF NOT EXISTS ubicacion_bodega (
    id_ubicacion INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_bodega INT NOT NULL,
    codigo_ubicacion VARCHAR(20) NOT NULL,
    capacidad_maxima_kg DECIMAL(10,2),
    capacidad_maxima_volumen DECIMAL(10,2),
    tipo ENUM('Recepcion', 'Almacenamiento', 'Despacho') DEFAULT 'Almacenamiento',
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_bodega) REFERENCES bodega(id_bodega),
    UNIQUE (id_bodega, codigo_ubicacion)
);

-- Tabla Inventario_Ciclico (si no existe)
CREATE TABLE IF NOT EXISTS inventario_ciclico (
    id_inventario INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    id_usuario_responsable INT,
    estado ENUM('Planificado', 'En Proceso', 'Completado') DEFAULT 'Planificado',
    notas TEXT,
    FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario)
);

-- Tabla Conteo_Inventario (si no existe)
CREATE TABLE IF NOT EXISTS conteo_inventario (
    id_conteo INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_inventario INT NOT NULL,
    id_producto INT NOT NULL,
    id_ubicacion INT,
    cantidad_sistema INT NOT NULL,
    cantidad_contada INT,
    ajustado BOOLEAN DEFAULT FALSE,
    fecha_conteo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario_contador INT,
    FOREIGN KEY (id_inventario) REFERENCES inventario_ciclico(id_inventario),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_ubicacion) REFERENCES ubicacion_bodega(id_ubicacion),
    FOREIGN KEY (id_usuario_contador) REFERENCES usuario(id_usuario)
);

-- Tabla Precio_Especial (si no existe)
CREATE TABLE IF NOT EXISTS precio_especial (
    id_precio_especial INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo_cliente ENUM('Minorista', 'Mayorista', 'Distribuidor') NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- Tabla Devolucion (si no existe)
CREATE TABLE IF NOT EXISTS devolucion (
    id_devolucion INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    fecha DATE NOT NULL,
    motivo VARCHAR(200),
    estado ENUM('Pendiente', 'Procesada', 'Rechazada') DEFAULT 'Pendiente',
    id_usuario_procesador INT,
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
    FOREIGN KEY (id_usuario_procesador) REFERENCES usuario(id_usuario)
);

-- Tabla Item_Devolucion (si no existe)
CREATE TABLE IF NOT EXISTS item_devolucion (
    id_item_devolucion INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_devolucion INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    estado_producto ENUM('Bueno', 'Dañado') DEFAULT 'Bueno',
    accion_tomada ENUM('Reintegrado', 'Desechado') DEFAULT 'Reintegrado',
    id_movimiento INT,
    FOREIGN KEY (id_devolucion) REFERENCES devolucion(id_devolucion),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_movimiento) REFERENCES movimiento(id_movimiento)
);

-- Tabla Alerta_Inventario (si no existe)
CREATE TABLE IF NOT EXISTS alerta_inventario (
    id_alerta INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo_alerta ENUM('Stock_Minimo', 'Caducidad_Proxima', 'Sin_Movimiento') NOT NULL,
    umbral INT NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    notificar_a VARCHAR(100),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- =============================================
-- ACTUALIZACIÓN DE COLUMNAS EN TABLAS EXISTENTES
-- =============================================

-- Verificar si la columna id_unidad_compra existe en la tabla producto
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'producto' AND column_name = 'id_unidad_compra';

-- Si no existe, añadirla
SET @sql = IF(@column_exists = 0, 'ALTER TABLE producto ADD COLUMN id_unidad_compra INT', 'SELECT "Columna id_unidad_compra ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si la columna id_unidad_venta existe en la tabla producto
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'producto' AND column_name = 'id_unidad_venta';

-- Si no existe, añadirla
SET @sql = IF(@column_exists = 0, 'ALTER TABLE producto ADD COLUMN id_unidad_venta INT', 'SELECT "Columna id_unidad_venta ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si la columna factor_conversion existe en la tabla producto
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'producto' AND column_name = 'factor_conversion';

-- Si no existe, añadirla
SET @sql = IF(@column_exists = 0, 'ALTER TABLE producto ADD COLUMN factor_conversion DECIMAL(10,4) DEFAULT 1.0', 'SELECT "Columna factor_conversion ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Intentar agregar las restricciones de clave foránea si no existen
-- Para id_unidad_compra
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = 'datos_almacen'
    AND TABLE_NAME = 'producto'
    AND COLUMN_NAME = 'id_unidad_compra'
    AND REFERENCED_TABLE_NAME = 'unidad_medida'
);

SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE producto ADD CONSTRAINT fk_producto_unidad_compra FOREIGN KEY (id_unidad_compra) REFERENCES unidad_medida(id_unidad)',
    'SELECT "Restricción fk_producto_unidad_compra ya existe"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Para id_unidad_venta
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = 'datos_almacen'
    AND TABLE_NAME = 'producto'
    AND COLUMN_NAME = 'id_unidad_venta'
    AND REFERENCED_TABLE_NAME = 'unidad_medida'
);

SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE producto ADD CONSTRAINT fk_producto_unidad_venta FOREIGN KEY (id_unidad_venta) REFERENCES unidad_medida(id_unidad)',
    'SELECT "Restricción fk_producto_unidad_venta ya existe"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si la columna id_ubicacion existe en la tabla stock
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'stock' AND column_name = 'id_ubicacion';

-- Si no existe, añadirla
SET @sql = IF(@column_exists = 0, 'ALTER TABLE stock ADD COLUMN id_ubicacion INT', 'SELECT "Columna id_ubicacion ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si la columna id_lote existe en la tabla stock
SELECT COUNT(*) INTO @column_exists FROM information_schema.columns 
WHERE table_schema = 'datos_almacen' AND table_name = 'stock' AND column_name = 'id_lote';

-- Si no existe, añadirla
SET @sql = IF(@column_exists = 0, 'ALTER TABLE stock ADD COLUMN id_lote INT', 'SELECT "Columna id_lote ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Intentar agregar las restricciones de clave foránea si no existen
-- Para id_ubicacion
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = 'datos_almacen'
    AND TABLE_NAME = 'stock'
    AND COLUMN_NAME = 'id_ubicacion'
    AND REFERENCED_TABLE_NAME = 'ubicacion_bodega'
);

SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE stock ADD CONSTRAINT fk_stock_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES ubicacion_bodega(id_ubicacion)',
    'SELECT "Restricción fk_stock_ubicacion ya existe"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Para id_lote
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = 'datos_almacen'
    AND TABLE_NAME = 'stock'
    AND COLUMN_NAME = 'id_lote'
    AND REFERENCED_TABLE_NAME = 'lote'
);

SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE stock ADD CONSTRAINT fk_stock_lote FOREIGN KEY (id_lote) REFERENCES lote(id_lote)',
    'SELECT "Restricción fk_stock_lote ya existe"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =============================================
-- CREACIÓN DE ÍNDICES PARA OPTIMIZAR CONSULTAS
-- =============================================

-- Verificar si el índice idx_producto_categoria existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'producto' AND index_name = 'idx_producto_categoria';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_producto_categoria ON producto(id_categoria)', 'SELECT "Índice idx_producto_categoria ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_producto_proveedor existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'producto' AND index_name = 'idx_producto_proveedor';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_producto_proveedor ON producto(id_proveedor)', 'SELECT "Índice idx_producto_proveedor ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_stock_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'stock' AND index_name = 'idx_stock_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_stock_producto ON stock(id_producto)', 'SELECT "Índice idx_stock_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_stock_bodega existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'stock' AND index_name = 'idx_stock_bodega';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_stock_bodega ON stock(id_bodega)', 'SELECT "Índice idx_stock_bodega ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_movimiento_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'movimiento' AND index_name = 'idx_movimiento_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_movimiento_producto ON movimiento(id_producto)', 'SELECT "Índice idx_movimiento_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_movimiento_bodega existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'movimiento' AND index_name = 'idx_movimiento_bodega';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_movimiento_bodega ON movimiento(id_bodega)', 'SELECT "Índice idx_movimiento_bodega ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_transferencia_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'transferencias' AND index_name = 'idx_transferencia_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_transferencia_producto ON transferencias(id_producto)', 'SELECT "Índice idx_transferencia_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_detalle_pedido_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'detalle_pedido' AND index_name = 'idx_detalle_pedido_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_detalle_pedido_producto ON detalle_pedido(id_producto)', 'SELECT "Índice idx_detalle_pedido_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_detalle_factura_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'detalle_factura' AND index_name = 'idx_detalle_factura_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_detalle_factura_producto ON detalle_factura(id_producto)', 'SELECT "Índice idx_detalle_factura_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_lote_producto existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'lote' AND index_name = 'idx_lote_producto';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_lote_producto ON lote(id_producto)', 'SELECT "Índice idx_lote_producto ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar si el índice idx_lote_caducidad existe
SELECT COUNT(*) INTO @index_exists FROM information_schema.statistics
WHERE table_schema = 'datos_almacen' AND table_name = 'lote' AND index_name = 'idx_lote_caducidad';

-- Si no existe, crearlo
SET @sql = IF(@index_exists = 0, 'CREATE INDEX idx_lote_caducidad ON lote(fecha_caducidad)', 'SELECT "Índice idx_lote_caducidad ya existe"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =============================================
-- DATOS INICIALES PARA NUEVAS TABLAS
-- =============================================

-- Datos iniciales para unidades de medida (si la tabla está vacía)
INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base)
SELECT 'Unidad', 'UN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Unidad');

INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base)
SELECT 'Caja', 'CJ', FALSE
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Caja');

INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base)
SELECT 'Kilogramo', 'KG', TRUE
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Kilogramo');

INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base)
SELECT 'Litro', 'L', TRUE
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Litro');

-- Asegurar que existe al menos un almacén
INSERT INTO almacen (id_almacen, nombre, ubicacion)
SELECT 1, 'Almacén Principal', 'Ubicación Central'
WHERE NOT EXISTS (SELECT 1 FROM almacen WHERE id_almacen = 1);

-- Asegurar que existe al menos una sucursal
INSERT INTO sucursal (id_sucursal, nombre, id_almacen)
SELECT 1, 'Sucursal Principal', 1
WHERE NOT EXISTS (SELECT 1 FROM sucursal WHERE id_sucursal = 1);

-- Asegurar que existe al menos una bodega
INSERT INTO bodega (id_bodega, nombre, id_sucursal)
SELECT 1, 'Bodega Principal', 1
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 1);

-- Mensaje de confirmación
SELECT 'Base de datos actualizada correctamente según el modelo completo' AS mensaje;
