package com.example.mvvmreal.data.repository;

import android.content.Context;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;
import java.util.List;

public class FacturaRepositoryNoInternet implements FacturaRepositoryInterface {

    private final FacturaDao dao ;
    public FacturaRepositoryNoInternet(AppDatabase db){
        this.dao= db.facturaDao();
    }
    @Override
    public RespuestaFactura getFacturas() throws IOException {
        List<Factura> facturas = dao.getAll();
        return new RespuestaFactura(facturas,String.valueOf(facturas.size()));
    }


}
