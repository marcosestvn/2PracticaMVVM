package com.example.mvvmreal.data.model;

import java.util.List;

public class RespuestaFactura {
    public List<Factura> facturas;
    public String numFacturas;

    public List<Factura> getFacturas() {
        return facturas;
    }

    public String getNumFacturas() {
        return numFacturas;
    }

    public RespuestaFactura(List<Factura> facturas, String numFacturas) {
        this.facturas = facturas;
        this.numFacturas = numFacturas;
    }
}
