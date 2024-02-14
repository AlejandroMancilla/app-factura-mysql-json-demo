DROP TABLE IF EXISTS descuentos;
CREATE TABLE descuentos (
    id_desc INT AUTO_INCREMENT NOT NULL,
    tipo ENUM ("Porcentaje", "Fijo"),
    condicion VARCHAR(50) NOT NULL,
    valor FLOAT NOT NULL,
    productos INT,
    activo BOOLEAN NOT NULL,
    PRIMARY KEY(id_desc)
);

CREATE TABLE impuestos (
    id_imp INT AUTO_INCREMENT NOT NULL,
    year INT NOT NULL,
    valor FLOAT NOT NULL,
    PRIMARY KEY(id_imp)
);

INSERT INTO descuentos (tipo, condicion, valor, productos, activo) 
VALUES 
    (1, "Monto minimo de compra: $1000", 10, NULL, TRUE),
    (2, "Compra de al menos 5 unidades del producto 5", 5, 5, TRUE),
    (1, "Cliente Gold con más de 10 compras previas", 15, NULL, TRUE),
    (2, "Válido solo los viernes", 3, NULL, TRUE),
    (1, "Compra durante la temporada navideña", 5, NULL, TRUE);


INSERT INTO impuestos (year, valor) 
VALUES 
    (2023, 12),
    (2024, 19);

ALTER TABLE factura ADD COLUMN descuento FLOAT NOT NULL;
ALTER TABLE factura ADD COLUMN impuesto FLOAT NOT NULL;