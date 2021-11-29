package com.example.mvvmreal.domain.interfaces;

import com.example.mvvmreal.data.model.RespuestaFactura;

import java.io.IOException;

public interface FacturaRepositoryInterface {

    RespuestaFactura getFacturas() throws IOException;
}
