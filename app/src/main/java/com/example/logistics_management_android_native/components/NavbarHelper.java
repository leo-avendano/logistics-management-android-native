package com.example.logistics_management_android_native.components;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.history.HistoryActivity;
import com.example.logistics_management_android_native.home.HomeActivity;
import com.example.logistics_management_android_native.R;

public class NavbarHelper {
    public static void setupNavbar(View navbar, Activity  activity) {
        Button btnHome = navbar.findViewById(R.id.nav_home);
        Button btnRute = navbar.findViewById(R.id.nav_rute);
        Button btnHistory = navbar.findViewById(R.id.nav_history);
        Button btnSettings = navbar.findViewById(R.id.nav_settings);
        ImageButton btnQr = navbar.findViewById(R.id.nav_qr);

        btnHome.setOnClickListener(v -> {
            if (!(activity instanceof HomeActivity)) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
            }
        });

        btnHistory.setOnClickListener(v -> {
            if (!(activity instanceof HistoryActivity)) {
                activity.startActivity(new Intent(activity, HistoryActivity.class));
            }
        });
    }
}

