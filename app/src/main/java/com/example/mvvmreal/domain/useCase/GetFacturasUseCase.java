package com.example.mvvmreal.domain.useCase;

import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.domain.executor.UseCaseCallBack;
import com.example.mvvmreal.domain.executor.UseCaseCallbackHandler;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;

import java.io.IOException;
public class GetFacturasUseCase extends UseCase<RespuestaFactura> {
    private final FacturaRepositoryInterface repository;


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
