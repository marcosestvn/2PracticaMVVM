package com.example.mvvmreal.data.repository;

import androidx.lifecycle.MutableLiveData;

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
    List<Factura> rs ;

    public void FacturaRepository() {
       this.api = new RetroFitNetwork().getRetrofit();
    }



    @Override
    public RespuestaFactura getFacturas() throws IOException {
        return api.getFacturas().execute().body();
    }
}
