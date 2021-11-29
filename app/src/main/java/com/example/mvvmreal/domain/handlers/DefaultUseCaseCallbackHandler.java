package com.example.mvvmreal.domain.handlers;

import android.os.Handler;
import android.os.Looper;

import com.example.mvvmreal.domain.executor.UseCaseCallbackHandler;

public class DefaultUseCaseCallbackHandler implements UseCaseCallbackHandler {

    private final Handler handler;

    //Creas un hilo
    public DefaultUseCaseCallbackHandler() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Causes the Runnable command to be added to the message queue.
     * The runnable will be run on the main thread.
     *
     * @param runnable {@link Runnable} to be executed.
     */
    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}