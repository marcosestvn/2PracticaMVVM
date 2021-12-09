package com.example.mvvmreal.data.model;

import java.util.List;

public class RespuestaFacturaVO {
    private List<FacturaVO> facturas;
    private String numFacturas;

    public List<FacturaVO> getFacturas() {
        return facturas;
    }

    public String getNumFacturas() {
        return numFacturas;
    }

    public String toString(){
    return facturas.toString();
    }
    public RespuestaFacturaVO(List<FacturaVO> facturas, String numFacturas) {
        this.facturas = facturas;
        this.numFacturas = numFacturas;
    }
}
