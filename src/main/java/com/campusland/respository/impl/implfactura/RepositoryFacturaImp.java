package com.campusland.respository.impl.implfactura;

import java.util.List;

import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.utils.conexionpersistencia.conexiondblist.ConexionBDList;

public class RepositoryFacturaImp implements RepositoryFactura {

    ConexionBDList conexion = ConexionBDList.getConexion();

    @Override
    public List<Factura> listar() {
        return conexion.getListFacturas();
        
    }

    @Override
    public void crear(Factura factura) {
       conexion.getListFacturas().add(factura);
        
    }

    @Override
    public void listarClientesPorCompras() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarClientesPorCompras'");
    }

    @Override
    public void listarProductosPorVentas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarProductosPorVentas'");
    }

    @Override
    public void informeVentas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'informeVentas'");
    }

    @Override
    public double obtenerImpuesto(int year) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getImpuesto'");
    }


}
