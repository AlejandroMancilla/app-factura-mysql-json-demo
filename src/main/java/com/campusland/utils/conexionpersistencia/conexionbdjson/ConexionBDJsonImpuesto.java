package com.campusland.utils.conexionpersistencia.conexionbdjson;

import com.campusland.respository.models.Impuesto;

public class ConexionBDJsonImpuesto extends ConexionBDJsonBase<Impuesto>{

    private static ConexionBDJsonImpuesto conexionImpuesto;

    private ConexionBDJsonImpuesto() {
        super("impuesto.json");
    }

    public static ConexionBDJsonImpuesto getConexion() {
        if (conexionImpuesto != null) {
            return conexionImpuesto;
        } else {
            conexionImpuesto = new ConexionBDJsonImpuesto();
            return conexionImpuesto;
        }
    }
    
}
