package com.example.logistics_management_android_native.data.interfaces;

public interface LogisticsServiceCallback {
    void onSuccess(String message);
    void onError(Throwable error);
}