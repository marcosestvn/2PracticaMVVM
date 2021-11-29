package com.example.mvvmreal.domain.useCase;

import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.repository.FacturaRepository;
import com.example.mvvmreal.domain.executor.UseCaseCallBack;
import com.example.mvvmreal.domain.executor.UseCaseCallbackHandler;

public abstract class UseCase<R> implements Runnable {
    private UseCaseCallbackHandler callbackHandler;
    private UseCaseCallBack<RespuestaFactura> callback;
    public UseCase(UseCaseCallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void notifyResult(final RespuestaFactura result) {
        callbackHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        });
    }

    public void setCallback(UseCaseCallBack<RespuestaFactura> callback) {
        this.callback = callback;
    }

}