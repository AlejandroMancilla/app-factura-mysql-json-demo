package com.campusland.respository;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Factura;

public interface RepositoryFactura {

    List<Factura> listar();

    void crear(Factura factura)throws FacturaExceptionInsertDataBase;
    
    void listarClientesPorCompras();

    void listarProductosPorVentas();

    void informeVentas();

    double obtenerImpuesto(int year);

}
