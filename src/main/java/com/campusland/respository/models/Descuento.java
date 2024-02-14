package com.campusland.respository.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Descuento {
    private int id;
    private String tipo;
    private String condicion;
    private float valor;
    private int producto;
    private boolean estado;
    private double desc;

    public Descuento(int id, String tipo, String condicion, float valor, int producto, boolean estado) {
        this.id = id;
        this.tipo = tipo;
        this.condicion = condicion;
        this.valor = valor;
        this.producto = producto;
        this.estado = estado;
        this.desc = 0;
    }

}
