package com.example.mvvmreal.data.network;

import com.example.mvvmreal.util.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitNetwork {

    public FacturaApi getRetrofit() {

        return new Retrofit.Builder().baseUrl(Constantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build().create(FacturaApi.class);

    }

}
