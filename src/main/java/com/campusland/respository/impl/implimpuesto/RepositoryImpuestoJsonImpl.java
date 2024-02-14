package com.campusland.respository.impl.implimpuesto;

import java.util.List;

import com.campusland.respository.RepositoryImpuesto;
import com.campusland.respository.models.Impuesto;
import com.campusland.utils.conexionpersistencia.conexionbdjson.ConexionBDJsonImpuesto;

public class RepositoryImpuestoJsonImpl implements RepositoryImpuesto{

    ConexionBDJsonImpuesto conexion = ConexionBDJsonImpuesto.getConexion();

    @Override
    public void crear(Impuesto impuesto) {
        List<Impuesto> listImpuesto = conexion.getData(Impuesto.class);
        listImpuesto.add(impuesto);
        conexion.saveData(listImpuesto);
    }
    
}
