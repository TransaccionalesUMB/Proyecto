-- Script para actualizar la base de datos datos_almacen con datos iniciales
-- Mayo 2025

USE datos_almacen;

-- =============================================
-- DATOS INICIALES PARA TABLAS PRINCIPALES
-- =============================================

-- Datos para tabla rol (si no existen)
INSERT INTO rol (id_rol, nombre) 
SELECT 1, 'Admin' 
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE id_rol = 1);

INSERT INTO rol (id_rol, nombre) 
SELECT 2, 'Operador' 
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE id_rol = 2);

INSERT INTO rol (id_rol, nombre) 
SELECT 3, 'Auditor' 
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE id_rol = 3);

INSERT INTO rol (id_rol, nombre) 
SELECT 4, 'Cliente' 
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE id_rol = 4);

-- Datos para tabla permiso (si no existen)
INSERT INTO permiso (id_permiso, nombre, descripcion) 
SELECT 1, 'GESTIONAR_USUARIOS', 'Permite crear, modificar y eliminar usuarios' 
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE id_permiso = 1);

INSERT INTO permiso (id_permiso, nombre, descripcion) 
SELECT 2, 'GESTIONAR_PRODUCTOS', 'Permite crear, modificar y eliminar productos' 
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE id_permiso = 2);

INSERT INTO permiso (id_permiso, nombre, descripcion) 
SELECT 3, 'GESTIONAR_INVENTARIO', 'Permite gestionar el inventario' 
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE id_permiso = 3);

INSERT INTO permiso (id_permiso, nombre, descripcion) 
SELECT 4, 'VER_REPORTES', 'Permite ver reportes' 
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE id_permiso = 4);

INSERT INTO permiso (id_permiso, nombre, descripcion) 
SELECT 5, 'REALIZAR_PEDIDOS', 'Permite realizar pedidos' 
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE id_permiso = 5);

-- Relaciones entre roles y permisos (si no existen)
-- Admin tiene todos los permisos
INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 1, 1 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 1 AND id_permiso = 1);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 1, 2 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 1 AND id_permiso = 2);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 1, 3 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 1 AND id_permiso = 3);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 1, 4 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 1 AND id_permiso = 4);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 1, 5 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 1 AND id_permiso = 5);

-- Operador puede gestionar productos, inventario y ver reportes
INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 2, 2 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 2 AND id_permiso = 2);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 2, 3 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 2 AND id_permiso = 3);

INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 2, 4 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 2 AND id_permiso = 4);

-- Auditor solo puede ver reportes
INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 3, 4 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 3 AND id_permiso = 4);

-- Cliente puede realizar pedidos
INSERT INTO rol_permiso (id_rol, id_permiso) 
SELECT 4, 5 
WHERE NOT EXISTS (SELECT 1 FROM rol_permiso WHERE id_rol = 4 AND id_permiso = 5);

-- Usuario administrador por defecto (si no existe)
INSERT INTO usuario (nombre, email, password, id_rol) 
SELECT 'Administrador', 'admin@stockmaster.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 1 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@stockmaster.com');
-- Nota: La contraseña es 'admin' encriptada con BCrypt

-- Datos para tabla categoria (si no existen)
INSERT INTO categoria (id_categoria, nombre) 
SELECT 'CAT001', 'Electrónicos' 
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 'CAT001');

INSERT INTO categoria (id_categoria, nombre) 
SELECT 'CAT002', 'Alimentos' 
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 'CAT002');

INSERT INTO categoria (id_categoria, nombre) 
SELECT 'CAT003', 'Ropa' 
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 'CAT003');

INSERT INTO categoria (id_categoria, nombre) 
SELECT 'CAT004', 'Hogar' 
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 'CAT004');

-- Datos para tabla proveedor (si no existen)
INSERT INTO proveedor (id_proveedor, nombre, contacto) 
SELECT 'PROV001', 'Electrónica Global', 'contacto@electronicaglobal.com' 
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 'PROV001');

INSERT INTO proveedor (id_proveedor, nombre, contacto) 
SELECT 'PROV002', 'Alimentos Frescos', 'ventas@alimentosfrescos.com' 
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 'PROV002');

INSERT INTO proveedor (id_proveedor, nombre, contacto) 
SELECT 'PROV003', 'Textiles Unidos', 'info@textilesunidos.com' 
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 'PROV003');

INSERT INTO proveedor (id_proveedor, nombre, contacto) 
SELECT 'PROV004', 'Hogar y Decoración', 'ventas@hogardeco.com' 
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 'PROV004');

-- Datos para tabla unidad_medida (si no existen)
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

INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base) 
SELECT 'Metro', 'M', TRUE 
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Metro');

INSERT INTO unidad_medida (nombre, abreviatura, es_unidad_base) 
SELECT 'Docena', 'DOC', FALSE 
WHERE NOT EXISTS (SELECT 1 FROM unidad_medida WHERE nombre = 'Docena');

-- Datos para tabla almacen (si no existe)
INSERT INTO almacen (id_almacen, nombre, ubicacion) 
SELECT 1, 'Almacén Principal', 'Calle Principal 123' 
WHERE NOT EXISTS (SELECT 1 FROM almacen WHERE id_almacen = 1);

INSERT INTO almacen (id_almacen, nombre, ubicacion) 
SELECT 2, 'Almacén Secundario', 'Avenida Central 456' 
WHERE NOT EXISTS (SELECT 1 FROM almacen WHERE id_almacen = 2);

-- Datos para tabla sucursal (si no existe)
INSERT INTO sucursal (id_sucursal, nombre, direccion, id_almacen) 
SELECT 1, 'Sucursal Norte', 'Calle Norte 789', 1 
WHERE NOT EXISTS (SELECT 1 FROM sucursal WHERE id_sucursal = 1);

INSERT INTO sucursal (id_sucursal, nombre, direccion, id_almacen) 
SELECT 2, 'Sucursal Sur', 'Avenida Sur 321', 1 
WHERE NOT EXISTS (SELECT 1 FROM sucursal WHERE id_sucursal = 2);

INSERT INTO sucursal (id_sucursal, nombre, direccion, id_almacen) 
SELECT 3, 'Sucursal Este', 'Boulevard Este 654', 2 
WHERE NOT EXISTS (SELECT 1 FROM sucursal WHERE id_sucursal = 3);

-- Datos para tabla bodega (si no existe)
INSERT INTO bodega (id_bodega, nombre, id_sucursal) 
SELECT 1, 'Bodega A', 1 
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 1);

INSERT INTO bodega (id_bodega, nombre, id_sucursal) 
SELECT 2, 'Bodega B', 1 
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 2);

INSERT INTO bodega (id_bodega, nombre, id_sucursal) 
SELECT 3, 'Bodega C', 2 
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 3);

INSERT INTO bodega (id_bodega, nombre, id_sucursal) 
SELECT 4, 'Bodega D', 3 
WHERE NOT EXISTS (SELECT 1 FROM bodega WHERE id_bodega = 4);

-- Datos para tabla ubicacion_bodega (si no existe)
INSERT INTO ubicacion_bodega (id_bodega, codigo_ubicacion, capacidad_maxima_kg, capacidad_maxima_volumen, tipo) 
SELECT 1, 'A-01', 1000.00, 500.00, 'Almacenamiento' 
WHERE NOT EXISTS (SELECT 1 FROM ubicacion_bodega WHERE id_bodega = 1 AND codigo_ubicacion = 'A-01');

INSERT INTO ubicacion_bodega (id_bodega, codigo_ubicacion, capacidad_maxima_kg, capacidad_maxima_volumen, tipo) 
SELECT 1, 'A-02', 1000.00, 500.00, 'Almacenamiento' 
WHERE NOT EXISTS (SELECT 1 FROM ubicacion_bodega WHERE id_bodega = 1 AND codigo_ubicacion = 'A-02');

INSERT INTO ubicacion_bodega (id_bodega, codigo_ubicacion, capacidad_maxima_kg, capacidad_maxima_volumen, tipo) 
SELECT 1, 'R-01', 500.00, 250.00, 'Recepcion' 
WHERE NOT EXISTS (SELECT 1 FROM ubicacion_bodega WHERE id_bodega = 1 AND codigo_ubicacion = 'R-01');

INSERT INTO ubicacion_bodega (id_bodega, codigo_ubicacion, capacidad_maxima_kg, capacidad_maxima_volumen, tipo) 
SELECT 1, 'D-01', 500.00, 250.00, 'Despacho' 
WHERE NOT EXISTS (SELECT 1 FROM ubicacion_bodega WHERE id_bodega = 1 AND codigo_ubicacion = 'D-01');

-- Datos para tabla departamento (si no existe)
INSERT INTO departamento (id_departamento, nombre) 
SELECT 1, 'Ventas' 
WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE id_departamento = 1);

INSERT INTO departamento (id_departamento, nombre) 
SELECT 2, 'Compras' 
WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE id_departamento = 2);

INSERT INTO departamento (id_departamento, nombre) 
SELECT 3, 'Logística' 
WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE id_departamento = 3);

INSERT INTO departamento (id_departamento, nombre) 
SELECT 4, 'Administración' 
WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE id_departamento = 4);

-- Datos para tabla metodo_pago (si no existe)
INSERT INTO metodo_pago (id_metodo_pago, nombre) 
SELECT 1, 'Efectivo' 
WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE id_metodo_pago = 1);

INSERT INTO metodo_pago (id_metodo_pago, nombre) 
SELECT 2, 'Tarjeta de Crédito' 
WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE id_metodo_pago = 2);

INSERT INTO metodo_pago (id_metodo_pago, nombre) 
SELECT 3, 'Transferencia Bancaria' 
WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE id_metodo_pago = 3);

-- Datos para tabla cliente (si no existe)
INSERT INTO cliente (id_cliente, nombre, direccion, telefono, email) 
SELECT 1, 'Cliente Minorista', 'Calle Cliente 123', '123456789', 'cliente@ejemplo.com' 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 1);

INSERT INTO cliente (id_cliente, nombre, direccion, telefono, email) 
SELECT 2, 'Cliente Mayorista', 'Avenida Mayorista 456', '987654321', 'mayorista@ejemplo.com' 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 2);

-- Datos para tabla producto (ejemplos básicos)
-- Primero obtenemos los IDs de las unidades de medida
SET @id_unidad = (SELECT id_unidad FROM unidad_medida WHERE nombre = 'Unidad' LIMIT 1);
SET @id_caja = (SELECT id_unidad FROM unidad_medida WHERE nombre = 'Caja' LIMIT 1);
SET @id_kg = (SELECT id_unidad FROM unidad_medida WHERE nombre = 'Kilogramo' LIMIT 1);
SET @id_litro = (SELECT id_unidad FROM unidad_medida WHERE nombre = 'Litro' LIMIT 1);

-- Insertamos productos si no existen
INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Smartphone X', 'Smartphone de última generación', 'CAT001', 'PROV001', 599.99, @id_caja, @id_unidad, 10 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Smartphone X');

INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Laptop Pro', 'Laptop para profesionales', 'CAT001', 'PROV001', 1299.99, @id_caja, @id_unidad, 5 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Laptop Pro');

INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Arroz Premium', 'Arroz de alta calidad', 'CAT002', 'PROV002', 2.99, @id_kg, @id_kg, 1 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Arroz Premium');

INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Aceite de Oliva', 'Aceite de oliva extra virgen', 'CAT002', 'PROV002', 8.99, @id_litro, @id_litro, 1 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Aceite de Oliva');

INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Camiseta Algodón', 'Camiseta 100% algodón', 'CAT003', 'PROV003', 19.99, @id_caja, @id_unidad, 12 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Camiseta Algodón');

INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio, id_unidad_compra, id_unidad_venta, factor_conversion) 
SELECT 'Sartén Antiadherente', 'Sartén con recubrimiento antiadherente', 'CAT004', 'PROV004', 29.99, @id_caja, @id_unidad, 6 
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Sartén Antiadherente');

-- Mensaje de confirmación
SELECT 'Base de datos actualizada con datos iniciales' AS mensaje;
