package com.example.mvvmreal.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Factura {

    @PrimaryKey
    @ColumnInfo(name = "fecha")
    @NonNull
    public String fecha;

    @ColumnInfo(name="descEstado")
    public String descEstado;

    @ColumnInfo(name = "importeOrdenacion")
    public double importeOrdenacion;

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }



    public Factura(String descEstado, double importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }
}
