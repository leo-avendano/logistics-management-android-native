package com.example.logistics_management_android_native.data.interfaces;

import com.example.logistics_management_android_native.data.model.HelloResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LogisticsApiService {
    @GET("hello")
    Call<HelloResponse> getHelloWorld();
}

