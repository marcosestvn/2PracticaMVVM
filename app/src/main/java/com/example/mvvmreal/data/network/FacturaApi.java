package com.example.mvvmreal.data.network;

import com.example.mvvmreal.data.model.RespuestaFactura;


import retrofit2.Call;
import retrofit2.http.GET;

public interface FacturaApi {

    @GET("/facturas")
    Call<RespuestaFactura> getFacturas();
}
