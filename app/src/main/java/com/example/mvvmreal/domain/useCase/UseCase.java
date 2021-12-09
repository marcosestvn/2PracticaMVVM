package com.example.mvvmreal.domain.useCase;

import com.example.mvvmreal.data.model.RespuestaFacturaVO;
import com.example.mvvmreal.domain.executor.UseCaseCallBack;
import com.example.mvvmreal.domain.executor.UseCaseCallbackHandler;

public abstract class UseCase<R> implements Runnable {
    private UseCaseCallbackHandler callbackHandler;
    private UseCaseCallBack<RespuestaFacturaVO> callback;
    public UseCase(UseCaseCallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void notifyResult(final RespuestaFacturaVO result) {
        callbackHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        });
    }

    public void setCallback(UseCaseCallBack<RespuestaFacturaVO> callback) {
        this.callback = callback;
    }

}