package com.example.mvvmreal.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.RespuestaFactura;

import java.util.List;

@Dao
public interface FacturaDao {

    @Query("select * from Factura")
    List<Factura> getAll();

    @Query("select count() from Factura WHERE fecha = :fecha")
    Integer getFactura(String fecha);

    @Insert
    void insert(Factura factura);

    @Delete
    void deleteFactura(Factura factura);
}
