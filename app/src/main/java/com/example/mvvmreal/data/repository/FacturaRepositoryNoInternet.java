package com.example.mvvmreal.data.repository;

import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;

public class FacturaRepositoryNoInternet implements FacturaRepositoryInterface {

    FacturaDao dao;

    public FacturaRepositoryNoInternet(FacturaDao dao){
        this.dao=dao;
    }
    @Override
    public RespuestaFactura getFacturas() throws IOException {
        return dao.getAll();
    }


}
