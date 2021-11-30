package com.example.mvvmreal.data.repository;

import android.content.Context;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.network.FacturaApi;
import com.example.mvvmreal.data.network.RetroFitNetwork;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FacturaRepository implements FacturaRepositoryInterface {


    FacturaApi api;
    private final FacturaDao dao;

    public  FacturaRepository(AppDatabase db) {
        this.api = new RetroFitNetwork().getRetrofit();
        this.dao = db.facturaDao();
    }


    @Override
    public RespuestaFactura getFacturas() throws IOException {

        RespuestaFactura res = api.getFacturas().execute().body();
        List<Factura> facturas = Objects.requireNonNull(res).getFacturas();

        for (Factura facturaEvaluada:
             facturas) {
            if(dao.getFactura(facturaEvaluada.fecha) == 0){

                dao.insert(facturaEvaluada);
            }
        }



        return api.getFacturas().execute().body();
    }
}
