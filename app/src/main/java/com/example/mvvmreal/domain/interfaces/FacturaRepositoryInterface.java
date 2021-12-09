package com.example.mvvmreal.domain.interfaces;

import com.example.mvvmreal.data.model.RespuestaFacturaVO;

import java.io.IOException;

public interface FacturaRepositoryInterface {

    RespuestaFacturaVO getFacturas() throws IOException;
}
