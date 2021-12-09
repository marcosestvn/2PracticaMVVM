package com.example.mvvmreal.data.repository;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.data.model.FacturaVO;
import com.example.mvvmreal.data.model.RespuestaFacturaVO;
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
    public RespuestaFacturaVO getFacturas() throws IOException {

        RespuestaFacturaVO res = api.getFacturas().execute().body();
        List<FacturaVO> facturas = Objects.requireNonNull(res).getFacturas();

        System.out.println(res.toString());
        System.out.println(facturas);
        for (FacturaVO facturaEvaluada :
                facturas) {
            if(dao.getFactura(facturaEvaluada.fecha) == 0){

                dao.insert(facturaEvaluada);
            }
        }



        return api.getFacturas().execute().body();
    }
}
