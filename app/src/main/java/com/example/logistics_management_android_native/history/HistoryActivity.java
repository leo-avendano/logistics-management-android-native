package com.example.logistics_management_android_native.history;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.components.NavbarHelper;
import com.example.logistics_management_android_native.model.PackageHistory; // Adjust the import based on your package structure
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private HistoryRuteAdapter adapter;
    private ArrayList<PackageHistory> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        packageList = new ArrayList<>();
        adapter = new HistoryRuteAdapter(packageList);

        View navbar = findViewById(R.id.navbar_menu);
        NavbarHelper.setupNavbar(navbar, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Adding items using PackageHistory
        addItem(new PackageHistory("Paquete DH95K", "15/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete KFLD4Q", "12/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete UBK64D", "11/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete M9KDJ", "11/04/2025", "Fallida"));
    }

    private void addItem(PackageHistory packageHistory) {
        adapter.addItem(packageHistory); // Ensure adapter has a method to handle PackageHistory
    }
}