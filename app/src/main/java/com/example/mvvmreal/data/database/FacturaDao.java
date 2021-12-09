package com.example.mvvmreal.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mvvmreal.data.model.FacturaVO;

import java.util.List;

@Dao
public interface FacturaDao {

    @Query("select * from FacturaVO")
    List<FacturaVO> getAll();

    @Query("select count() from FacturaVO WHERE fecha = :fecha")
    Integer getFactura(String fecha);

    @Insert
    void insert(FacturaVO factura);

}
