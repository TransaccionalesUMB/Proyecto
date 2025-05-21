-- Modificar la tabla Producto para cambiar id_categoria e id_proveedor a VARCHAR(25)
ALTER TABLE Producto MODIFY COLUMN id_categoria VARCHAR(25);
ALTER TABLE Producto MODIFY COLUMN id_proveedor VARCHAR(25);

-- Modificar la tabla Categoria para cambiar la llave foránea a VARCHAR(25)
-- Asumiendo que la llave foránea se llama id_categoria
ALTER TABLE Categoria MODIFY COLUMN id_categoria VARCHAR(25);

-- Modificar la tabla Proveedor para cambiar la llave foránea a VARCHAR(25)
-- Asumiendo que la llave foránea se llama id_proveedor
ALTER TABLE Proveedor MODIFY COLUMN id_proveedor VARCHAR(25);
