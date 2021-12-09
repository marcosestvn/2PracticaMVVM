package com.example.mvvmreal.data.repository;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.model.FacturaVO;
import com.example.mvvmreal.data.model.RespuestaFacturaVO;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;
import java.util.List;

public class FacturaRepositoryNoInternet implements FacturaRepositoryInterface {

    private final FacturaDao dao ;
    public FacturaRepositoryNoInternet(AppDatabase db){
        this.dao= db.facturaDao();
    }
    @Override
    public RespuestaFacturaVO getFacturas() throws IOException {
        List<FacturaVO> facturas = dao.getAll();
        return new RespuestaFacturaVO(facturas,String.valueOf(facturas.size()));
    }


}
