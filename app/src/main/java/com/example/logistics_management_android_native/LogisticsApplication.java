package com.example.logistics_management_android_native;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class LogisticsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
