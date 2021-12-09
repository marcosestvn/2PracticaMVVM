package com.example.mvvmreal.presentacion.filtro;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmreal.util.Constantes;
import com.example.mvvmreal.presentacion.wrapper.WrapperFiltro;
import com.google.gson.Gson;

public class FiltradoViewModel extends ViewModel {
     public MutableLiveData<WrapperFiltro> wrapper;

    public FiltradoViewModel() {
        this.wrapper = new MutableLiveData<>();
    }

    public void setWrapperFiltro(String wrapper) {
        WrapperFiltro wrapperTemp = new Gson().fromJson(wrapper, WrapperFiltro.class);
        this.wrapper.setValue(wrapperTemp);
    }


    public String getWrapperParseado() {
        return new Gson().toJson(wrapper.getValue());
    }

    public void anyadirEstadoFiltro(String estado) {
        this.wrapper.getValue().getEstadosFiltro().add(estado);
    }

    public void eliminarEstadoFiltro(String estado) {
        this.wrapper.getValue().getEstadosFiltro().remove(estado);
    }


    public String getFecha(String codigo) {
        if (codigo.equals(Constantes.Clave_1)) {
            return this.wrapper.getValue().getFecha_desde();
        }

        if (codigo.equals(Constantes.Clave_2)) {
            return this.wrapper.getValue().getFecha_hasta();
        }
        return null;
    }

    public void limpiarFiltros() {
        Integer importeTemp = this.wrapper.getValue().getImporteMaximo();
        this.wrapper.setValue(new WrapperFiltro());
        this.wrapper.getValue().setImporteMaximo(importeTemp);


    }
}
