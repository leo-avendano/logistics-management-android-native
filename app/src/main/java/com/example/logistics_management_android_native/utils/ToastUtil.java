package com.example.logistics_management_android_native.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private final Context context;
    public ToastUtil(Context context) {
        this.context = context.getApplicationContext();
    }
    public void showToast(ToastMessage message) {
        Toast.makeText(context, message.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void showToastConcat(ToastMessage message, String additionalString) {
        Toast.makeText(context, message.getMessage() + additionalString, Toast.LENGTH_SHORT).show();
    }
}