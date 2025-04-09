package com.example.logistics_management_android_native.model;

public class PackageHistory {
    private final String id;
    private final String date;
    private final String status;

    public PackageHistory(String id, String date, String status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
