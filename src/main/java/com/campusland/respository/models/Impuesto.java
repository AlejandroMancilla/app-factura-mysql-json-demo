package com.campusland.respository.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Impuesto {
    private Cliente cliente;
    private int numFactura;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    
    private LocalDateTime fechaFactura;
    private double totalFactura;
    private double impuestoTotal;


    public Impuesto(Factura factura) {
        this.cliente = factura.getCliente();
        this.numFactura = factura.getNumeroFactura();
        this.fechaFactura = factura.getFecha();
        this.totalFactura = factura.totalPagar();
        this.impuestoTotal = factura.getImpuesto();
    }
    

    
}
