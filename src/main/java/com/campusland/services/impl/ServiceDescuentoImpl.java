package com.campusland.services.impl;

import java.util.List;

import com.campusland.respository.RepositoryDescuento;
import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.services.ServiceDescuento;

public class ServiceDescuentoImpl implements ServiceDescuento{

    private final RepositoryDescuento crudRepositoryDescuento;

    public ServiceDescuentoImpl(RepositoryDescuento crudRepositoryDescuento){
        this.crudRepositoryDescuento=crudRepositoryDescuento;
    }

    @Override
    public List<Descuento> listar() {
        return this.crudRepositoryDescuento.listar();
    }

    @Override
    public List<Descuento> getAplicables(Factura factura) {
        List<Descuento> descuentos = this.crudRepositoryDescuento.getActivos();
        for(Descuento descuento : descuentos) {
            switch (descuento.getId()) {
                case 1:
                    if(factura.getTotalFactura() > 1000) descuento.setDesc(factura.getTotalFactura() * descuento.getValor() * 0.01);
                    break;
                case 2:
                    for(ItemFactura item : factura.getItems()) {
                        if(item.getProducto().getCodigo() == 5 && item.getCantidad() >= 5) descuento.setDesc(descuento.getValor());
                    }
                    break;
                case 3:
                    if(crudRepositoryDescuento.validarCliente(factura.getCliente().getId()) > 10) descuento.setDesc(factura.getTotalFactura() * descuento.getValor() * 0.01);
                    break;
                case 4:
                    if(factura.getFecha().getDayOfWeek().getValue() == 5) descuento.setDesc(descuento.getValor());
                    break;
                case 5:
                    if(factura.getFecha().getMonthValue() == 12) descuento.setDesc(factura.getTotalFactura() * descuento.getValor() * 0.01);
                    break;
                default:
                    break;
            }
        }factura.calcularDescuentoTotal(descuentos);
        return descuentos;
    }
}
