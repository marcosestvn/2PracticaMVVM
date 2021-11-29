package com.example.mvvmreal.domain.executor;

public interface UseCaseCallBack<R>{
    void onResult(R result);
}
