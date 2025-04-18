package com.example.logistics_management_android_native.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.components.NavbarHelper;
import com.example.logistics_management_android_native.components.NavbarHelperV2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View navbar = findViewById(R.id.navbar_menu);
        NavbarHelperV2.setupNavbar(navbar, this);

        if (savedInstanceState == null) {
            loadFragment(new AvailableRoutesFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}