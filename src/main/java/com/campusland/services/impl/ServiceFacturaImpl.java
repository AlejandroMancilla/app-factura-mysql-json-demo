package com.campusland.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.services.ServiceFactura;

public class ServiceFacturaImpl implements ServiceFactura {

  private final RepositoryFactura repositoryFacturaMysql;
  private final RepositoryFactura repositoryFacturaJson;

  public ServiceFacturaImpl(RepositoryFactura repositoryFacturaMysql, RepositoryFactura repositoryFacturaJson) {
    this.repositoryFacturaMysql = repositoryFacturaMysql;
    this.repositoryFacturaJson = repositoryFacturaJson;
  }

  @Override
  public List<Factura> listar() {
    return this.repositoryFacturaMysql.listar();

  }

  @Override
  public void crear(Factura factura) {
      try {
          guardarEnMysqlYJson(factura);
      } catch (FacturaExceptionInsertDataBase e) {
         e.printStackTrace();
      }
  }
  
  private void guardarEnMysqlYJson(Factura factura) throws FacturaExceptionInsertDataBase {
      guardarEnMysql(factura);
      guardarEnJson(factura);
  }
  
  private void guardarEnMysql(Factura factura) throws FacturaExceptionInsertDataBase {
      this.repositoryFacturaMysql.crear(factura);
  }
  
  private void guardarEnJson(Factura factura) throws FacturaExceptionInsertDataBase {
      try {
          this.repositoryFacturaJson.crear(factura);
      } catch (FacturaExceptionInsertDataBase eJson) {
          eJson.printStackTrace();
      }
  }

    @Override
    public void listarClientesPorCompras() {
        this.repositoryFacturaMysql.listarClientesPorCompras();
    }

    public void listarProductosPorVentas() {
        this.repositoryFacturaMysql.listarProductosPorVentas();
    }

    @Override
    public void informeVentas() {
        this.repositoryFacturaMysql.informeVentas();
    }

    @Override
    public double obtenerImpuesto(int year) {
        return this.repositoryFacturaMysql.obtenerImpuesto(year);
    }

    @Override
    public List<Factura> listarporAnino(int year) {
        List<Factura> facturas = new ArrayList<>();
        for (Factura factura : listar()){
            if(factura.getFecha().getYear() == year) facturas.add(factura);
        }
        return facturas;
    }
  

}
