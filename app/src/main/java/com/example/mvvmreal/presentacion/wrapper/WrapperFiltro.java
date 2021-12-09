package com.example.mvvmreal.presentacion.wrapper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class WrapperFiltro {

    private Integer importeMaximo;
    private Integer importeFiltro;
    private final List<String> estadosFiltro;
    private String fecha_desde;
    private String fecha_hasta;

    //Constructor
    public WrapperFiltro() {
        this.importeMaximo=0;
        this.importeFiltro=0;
        estadosFiltro= new ArrayList<>();
        this.fecha_desde="";
        this.fecha_hasta="";
    }

    public Integer getImporteMaximo() {
        if(importeMaximo==null){
            return null;
        }
        return importeMaximo;
    }

    public void setImporteMaximo(int importeMaximo) {
        this.importeMaximo = importeMaximo;
    }

    public Integer getImporteFiltro() {
        return importeFiltro;
    }

    public void setImporteFiltro(int importeFiltro) {
        this.importeFiltro = importeFiltro;
    }

    public List<String> getEstadosFiltro() {
        return estadosFiltro;
    }

    @NonNull
    @Override
    public String toString() {
        return "WrapperFiltro{" +
                "importeMaximo=" + importeMaximo +
                ", importeFiltro=" + importeFiltro +
                ", estadosFiltro=" + estadosFiltro +
                ", fecha_desde=" + fecha_desde +
                ", fecha_hasta=" + fecha_hasta +
                '}';
    }



    public String getFecha_desde() {
        return fecha_desde;
    }

    public void setFecha_desde(String fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public String getFecha_hasta() {
        return fecha_hasta;
    }

    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

}
