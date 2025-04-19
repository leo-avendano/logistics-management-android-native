package com.example.logistics_management_android_native.data.interfaces;

import com.example.logistics_management_android_native.data.model.AssignRouteRequest;
import com.example.logistics_management_android_native.data.model.HelloResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LogisticsApiService {
    @GET("hello")
    Call<HelloResponse> getHelloWorld();

    @POST("route/assign")
    Call<Void> assignRouteToRepartidor(@Body AssignRouteRequest request);

    @POST("route/unassign")
    Call<Void> unassignRouteFromRepartidor(@Body AssignRouteRequest request);
}

