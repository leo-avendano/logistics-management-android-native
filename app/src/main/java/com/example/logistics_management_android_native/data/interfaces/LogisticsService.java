package com.example.logistics_management_android_native.data.interfaces;

public interface LogisticsService {
    String getHelloWorld(LogisticsServiceCallback callback);

    // String getHelloWorld(LogisticsServiceCallback callback);
    void assignRouteToRepartidor(String routeUUID, String userID, LogisticsServiceCallback callback);
    void unassignRouteFromRepartidor(String routeUUID, LogisticsServiceCallback callback);
}
