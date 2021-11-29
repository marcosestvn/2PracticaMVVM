package com.example.mvvmreal.domain.useCase;

import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.domain.executor.UseCaseCallBack;
import com.example.mvvmreal.domain.executor.UseCaseCallbackHandler;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;
import java.util.List;
public class GetFacturasUseCase extends UseCase<RespuestaFactura> {
    private FacturaRepositoryInterface repository;
    private List<Factura> facturas;


    public GetFacturasUseCase(UseCaseCallbackHandler callBackHandler,FacturaRepositoryInterface repository) {
        super(callBackHandler);
        this.repository=repository;
    }

    public void customize(UseCaseCallBack<RespuestaFactura> callBack){

        setCallback(callBack);
    }

    @Override
    public void run() {
        try {
            notifyResult(repository.getFacturas());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
