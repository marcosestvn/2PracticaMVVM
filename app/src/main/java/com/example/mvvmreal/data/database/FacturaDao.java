package com.example.mvvmreal.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mvvmreal.data.model.RespuestaFactura;

@Dao
public interface FacturaDao {

    @Query("select *,count(FacturaId) from Factura")
    RespuestaFactura getAll();

    @Insert
    void insert(com.example.mvvmreal.data.model.Factura factura);
}
