package com.campusland.services.impl;

import com.campusland.respository.RepositoryImpuesto;

import com.campusland.respository.models.Impuesto;
import com.campusland.services.ServiceImpuesto;

public class ServiceImpuestoImp implements ServiceImpuesto {

    private final RepositoryImpuesto repositoryImpuestoJson;

    public ServiceImpuestoImp(RepositoryImpuesto repositoryImpuesto) {
        this.repositoryImpuestoJson = repositoryImpuesto;
    }

    @Override
    public void crear(Impuesto impuesto) {
        this.repositoryImpuestoJson.crear(impuesto);
    }

}
