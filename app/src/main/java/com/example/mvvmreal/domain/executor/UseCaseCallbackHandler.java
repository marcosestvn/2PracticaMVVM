package com.example.mvvmreal.domain.executor;

public interface UseCaseCallbackHandler {
    void post(Runnable runnable);
}
