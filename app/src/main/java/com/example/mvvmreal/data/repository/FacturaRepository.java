package com.example.mvvmreal.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;
import com.example.mvvmreal.data.network.FacturaApi;
import com.example.mvvmreal.data.network.RetroFitNetwork;


import java.io.IOException;
import java.util.List;

public class FacturaRepository implements FacturaRepositoryInterface {


    FacturaApi api = new RetroFitNetwork().getRetrofit();
    MutableLiveData<RespuestaFactura> res;
    List<Factura> rs;
    private AppDatabase db;
    private FacturaDao dao;

    public  FacturaRepository(Context context) {
        this.api = new RetroFitNetwork().getRetrofit();
        db = AppDatabase.getINSTANCE(context);
        this.dao = db.facturaDao();
    }


    @Override
    public RespuestaFactura getFacturas() throws IOException {

        RespuestaFactura res = api.getFacturas().execute().body();
        List<Factura> facturas = res.facturas;

        for (Factura facturaEvaluada:
             facturas) {
            if(dao.getFactura(facturaEvaluada.fecha) == 0){

                dao.insert(facturaEvaluada);
            }
        }



        return api.getFacturas().execute().body();
    }
}
