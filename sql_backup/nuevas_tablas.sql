-- Script para crear las nuevas tablas propuestas para StockMaster
-- Mayo 2025

-- Usar la base de datos existente
USE datos_almacen;

-- =============================================
-- NUEVAS TABLAS PROPUESTAS
-- =============================================

-- Tabla Unidad_Medida
CREATE TABLE IF NOT EXISTS unidad_medida (
    id_unidad INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    es_unidad_base BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB;

-- Modificación a la tabla Producto para incluir unidades de medida
ALTER TABLE producto 
ADD COLUMN id_unidad_compra INT,
ADD COLUMN id_unidad_venta INT,
ADD COLUMN factor_conversion DECIMAL(10,4) DEFAULT 1.0,
ADD CONSTRAINT fk_producto_unidad_compra FOREIGN KEY (id_unidad_compra) REFERENCES unidad_medida(id_unidad),
ADD CONSTRAINT fk_producto_unidad_venta FOREIGN KEY (id_unidad_venta) REFERENCES unidad_medida(id_unidad);

-- Tabla Lote
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
) ENGINE=InnoDB;

-- Tabla Ubicacion_Bodega
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
) ENGINE=InnoDB;

-- Modificación a la tabla Stock para incluir ubicación y lote
ALTER TABLE stock
ADD COLUMN id_ubicacion INT,
ADD COLUMN id_lote INT,
ADD CONSTRAINT fk_stock_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES ubicacion_bodega(id_ubicacion),
ADD CONSTRAINT fk_stock_lote FOREIGN KEY (id_lote) REFERENCES lote(id_lote);

-- Tabla Inventario_Ciclico
CREATE TABLE IF NOT EXISTS inventario_ciclico (
    id_inventario INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    id_usuario_responsable INT,
    estado ENUM('Planificado', 'En Proceso', 'Completado') DEFAULT 'Planificado',
    notas TEXT,
    FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- Tabla Conteo_Inventario
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
) ENGINE=InnoDB;

-- Tabla Precio_Especial
CREATE TABLE IF NOT EXISTS precio_especial (
    id_precio_especial INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo_cliente ENUM('Minorista', 'Mayorista', 'Distribuidor') NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;

-- Tabla Devolucion
CREATE TABLE IF NOT EXISTS devolucion (
    id_devolucion INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    fecha DATE NOT NULL,
    motivo VARCHAR(200),
    estado ENUM('Pendiente', 'Procesada', 'Rechazada') DEFAULT 'Pendiente',
    id_usuario_procesador INT,
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
    FOREIGN KEY (id_usuario_procesador) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- Tabla Item_Devolucion
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
) ENGINE=InnoDB;

-- Tabla Alerta_Inventario
CREATE TABLE IF NOT EXISTS alerta_inventario (
    id_alerta INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo_alerta ENUM('Stock_Minimo', 'Caducidad_Proxima', 'Sin_Movimiento') NOT NULL,
    umbral INT NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    notificar_a VARCHAR(100),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;

-- Índices para mejorar el rendimiento
CREATE INDEX idx_lote_producto ON lote(id_producto);
CREATE INDEX idx_lote_caducidad ON lote(fecha_caducidad);
CREATE INDEX idx_ubicacion_bodega ON ubicacion_bodega(id_bodega);
CREATE INDEX idx_stock_ubicacion ON stock(id_ubicacion);
CREATE INDEX idx_stock_lote ON stock(id_lote);
CREATE INDEX idx_conteo_inventario ON conteo_inventario(id_inventario);
CREATE INDEX idx_conteo_producto ON conteo_inventario(id_producto);
CREATE INDEX idx_precio_especial_producto ON precio_especial(id_producto);
CREATE INDEX idx_devolucion_factura ON devolucion(id_factura);
CREATE INDEX idx_item_devolucion_producto ON item_devolucion(id_producto);
CREATE INDEX idx_alerta_producto ON alerta_inventario(id_producto);

-- Datos iniciales para unidades de medida comunes
INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base) VALUES 
('Unidad', 'UN', TRUE),
('Caja', 'CJ', FALSE),
('Kilogramo', 'KG', TRUE),
('Gramo', 'G', FALSE),
('Litro', 'L', TRUE),
('Mililitro', 'ML', FALSE),
('Metro', 'M', TRUE),
('Centímetro', 'CM', FALSE),
('Docena', 'DOC', FALSE),
('Paquete', 'PQ', FALSE);

-- Mensaje de confirmación
SELECT 'Nuevas tablas creadas correctamente' AS mensaje;
