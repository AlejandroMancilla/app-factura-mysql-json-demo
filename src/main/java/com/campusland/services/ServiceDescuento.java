package com.campusland.services;

import java.util.List;

import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;

public interface ServiceDescuento {
    
    List<Descuento> listar();

    List<Descuento> getAplicables(Factura factura);
}
