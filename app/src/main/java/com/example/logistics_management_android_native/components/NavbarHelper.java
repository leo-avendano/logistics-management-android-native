package com.example.logistics_management_android_native.components;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.auth.AuthActivity;
import com.example.logistics_management_android_native.main.AvailableRoutesFragment;
import com.example.logistics_management_android_native.main.HistoryRoutesFragment;
import com.example.logistics_management_android_native.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class NavbarHelper {
    public static void setupNavbar(View navbar, MainActivity activity) {
        Button btnHome = navbar.findViewById(R.id.nav_home);
//        Button btnRute = navbar.findViewById(R.id.nav_rute);
        Button btnHistory = navbar.findViewById(R.id.nav_history);
        Button btnLogout = navbar.findViewById(R.id.nav_logout);
//        ImageButton btnQr = navbar.findViewById(R.id.nav_qr);


        btnHome.setOnClickListener(v -> activity.loadFragment(new AvailableRoutesFragment()));
        btnHistory.setOnClickListener(v -> activity.loadFragment(new HistoryRoutesFragment()));



        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(activity, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        });
    }
}

