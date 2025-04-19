package com.example.logistics_management_android_native.data.services;

import com.example.logistics_management_android_native.data.interfaces.LogisticsApiService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsServiceCallback;
import com.example.logistics_management_android_native.data.model.HelloResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LogisticsRetrofitService implements LogisticsService {
    private final LogisticsApiService logisticsApiService;

    @Inject
    public LogisticsRetrofitService(LogisticsApiService logisticsApiService) {
        this.logisticsApiService = logisticsApiService;
    }

    @Override
    public String getHelloWorld(LogisticsServiceCallback callback) {
        logisticsApiService.getHelloWorld().enqueue(new retrofit2.Callback<HelloResponse>() {
            @Override
            public void onResponse(retrofit2.Call<HelloResponse> call, retrofit2.Response<HelloResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError(new Exception("Failed to get response"));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<HelloResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
        return null;
    }
}
