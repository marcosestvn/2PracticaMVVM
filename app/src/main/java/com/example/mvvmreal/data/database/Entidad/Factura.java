package com.example.mvvmreal.data.database.Entidad;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Factura")
public class Factura {

    @PrimaryKey(autoGenerate = true)
    int FacturaId;
    String fecha;
    String descEstado;
    double importe;

    public int getFacturaId() {
        return FacturaId;
    }

    public void setFacturaId(int facturaId) {
        FacturaId = facturaId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
