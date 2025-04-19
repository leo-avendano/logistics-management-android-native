package com.example.logistics_management_android_native.data.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HelloResponse {
    private String message;
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
} 