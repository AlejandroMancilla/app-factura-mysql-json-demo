package com.campusland.respository;

import java.util.List;

import com.campusland.respository.models.Descuento;

public interface RepositoryDescuento {

    List<Descuento> listar();

    Descuento porId(int descuentoId);

    void crear(Descuento descuento);

    void editar(Descuento descuento);

    void eliminar(Descuento descuento);

    List<Descuento> getActivos();

    int validarCliente(int clienteId);

}
