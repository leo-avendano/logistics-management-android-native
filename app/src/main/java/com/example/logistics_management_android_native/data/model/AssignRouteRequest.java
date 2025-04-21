package com.example.logistics_management_android_native.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRouteRequest {
    private String routeUUID;
    private String userID;

    public AssignRouteRequest(String routeUUID, String userID) {
        this.routeUUID = routeUUID;
        this.userID = userID;
    }

    public void setRouteUUID(String routeUUID) {
        this.routeUUID = routeUUID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
} 