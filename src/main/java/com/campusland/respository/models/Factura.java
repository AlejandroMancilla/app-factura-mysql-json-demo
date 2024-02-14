package com.campusland.respository.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.utils.Formato;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Factura {

    private int numeroFactura;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    
    private LocalDateTime fecha;
    private Cliente cliente;
    private List<ItemFactura> items;
    private static int nextNumeroFactura;
    private double descuento;
    private double impuesto;

    public Factura(){

    }

    public Factura(int numeroFactura, LocalDateTime fecha, Cliente cliente, double descuento, double impuesto) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.descuento = descuento;
        this.impuesto = impuesto;
    }

    public Factura(LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = ++nextNumeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    public double getTotalFactura() {
        double totalFactura = 0;

        for (ItemFactura item : items) {
            totalFactura += item.getImporte();
        }
        return totalFactura;
    }

    public double restarDescuento() {
        return getTotalFactura() - this.descuento; 
    }

    public void calcularImpuesto(double impuesto) {
        this.impuesto = restarDescuento() * impuesto * 0.01;
    }

    public double totalPagar() {
        return restarDescuento() + this.impuesto;
    }

    public void calcularDescuentoTotal(List<Descuento> list) {
        double totalDescuento = 0;
        for (Descuento des : list) {
            totalDescuento += des.getDesc();
        }
        this.descuento = totalDescuento;
    }

    public void agregarItem(ItemFactura item){
        this.items.add(item);
    }

    public void display() {
        System.out.println();
        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println("-----------Detalle Factura----------------------");
        for (ItemFactura item : this.items) {
            System.out.println(item.getProducto().getCodigoNombre() + " " + item.getCantidad() + " "
                    + Formato.formatoMonedaPesos(item.getImporte()));

        }
        System.out.println();
        System.out.println("Total                        " + Formato.formatoMonedaPesos(this.getTotalFactura()));
        System.out.println();
    }

    public void displayFinal(List<Descuento> list) {
        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println();
        System.out.println("----------------------- DETALLE FACTURA ---------------------------");
        for (ItemFactura item : this.items) {
            System.out.printf("|%20s|%13s|%12s|%18s|%n", item.getProducto().getNombre(), Formato.formatoMonedaPesos(item.getProducto().getPrecioVenta()), item.getCantidad(), Formato.formatoMonedaPesos(item.getImporte()));
        }
        System.out.println("+" + "-".repeat(66) + "+");
        System.out.printf("|%50s|%15s|%n", "Total  ", Formato.formatoMonedaPesos(this.getTotalFactura()));
        double descTotal = 0;
        for (Descuento dsto : list) {
            if(dsto.getDesc() > 0) System.out.printf("|%50s|%15s|%n", dsto.getCondicion(), Formato.formatoMonedaPesos(dsto.getDesc()));
            descTotal += dsto.getDesc();
        }
        System.out.println("+" + "-".repeat(66) + "+");
        System.out.printf("|%47s|%18s|%n", "DescuentoTotal  ", descTotal);
        System.out.printf("|%47s|%18s|%n", "Impuesto  " , Formato.formatoMonedaPesos(this.getImpuesto()));
        System.out.printf("|%47s|%18s|%n", "Total con Descuento  " , Formato.formatoMonedaPesos(totalPagar()));

        System.out.println();

    }
}
