package com.campusland.services;

import java.sql.SQLException;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Factura;

public interface ServiceFactura {

    List<Factura> listar();

    void crear(Factura factura)throws FacturaExceptionInsertDataBase;

    void listarClientesPorCompras();

    void listarProductosPorVentas();

    void informeVentas();

    double obtenerImpuesto(int year) throws SQLException;

    List<Factura> listarporAnino(int year);
    
}
