package com.example.logistics_management_android_native.data.services;

import androidx.annotation.NonNull;

import com.example.logistics_management_android_native.data.interfaces.LogisticsApiService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsServiceCallback;
import com.example.logistics_management_android_native.data.model.AssignRouteRequest;
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
        logisticsApiService.getHelloWorld().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<HelloResponse> call, @NonNull retrofit2.Response<HelloResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError(new Exception("Failed to get response"));
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<HelloResponse> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
        return null;
    }

    @Override
    public void assignRouteToRepartidor(String routeUUID, String userID, LogisticsServiceCallback callback) {
        AssignRouteRequest request = new AssignRouteRequest(routeUUID, userID);
        logisticsApiService.assignRouteToRepartidor(request).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Route assigned successfully");
                } else {
                    callback.onError(new Exception("Failed to assign route"));
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void unassignRouteFromRepartidor(String routeUUID, LogisticsServiceCallback callback) {
        AssignRouteRequest request = new AssignRouteRequest(routeUUID, "");
        logisticsApiService.unassignRouteFromRepartidor(request).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Route unassigned successfully");
                } else {
                    callback.onError(new Exception("Failed to unassign route"));
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }
}
