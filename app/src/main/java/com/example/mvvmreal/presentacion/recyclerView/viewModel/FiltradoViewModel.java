package com.example.mvvmreal.presentacion.recyclerView.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.mvvmreal.util.Constantes;
import com.example.mvvmreal.data.model.WrapperFiltro;
import com.google.gson.Gson;

public class FiltradoViewModel extends ViewModel {
    private WrapperFiltro wrapperFiltro ;
    final Gson converter = new Gson();

    public FiltradoViewModel(String wrapper){
        this.wrapperFiltro=converter.fromJson(wrapper,WrapperFiltro.class);
    }

    public WrapperFiltro getWrapper() {
        return this.wrapperFiltro;
    }

    public String getWrapperParseado() {
        return converter.toJson(wrapperFiltro);
    }

    public void anyadirEstadoFiltro(String estado) {
        this.wrapperFiltro.getEstadosFiltro().add(estado);
    }

    public void eliminarEstadoFiltro(String estado) {
        this.wrapperFiltro.getEstadosFiltro().remove(estado);
    }


    public String getFecha(String codigo) {
        if (codigo.equals(Constantes.Clave_1)) {
            return this.wrapperFiltro.getFecha_desde();
        }

        if (codigo.equals(Constantes.Clave_2)) {
            return this.wrapperFiltro.getFecha_hasta();

        }
        return null;
    }

    public void limpiarFiltros() {
        Integer importeTemp=this.wrapperFiltro.getImporteMaximo();
        this.wrapperFiltro= new WrapperFiltro();
        this.wrapperFiltro.setImporteMaximo(importeTemp);
    }
}
