package com.example.logistics_management_android_native.home;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.components.NavbarHelper;
import com.example.logistics_management_android_native.model.Rute;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RuteViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RuteViewAdapter adapter;
    private List<Rute> ruteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerViewRutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View navbar = findViewById(R.id.navbar_menu);
        NavbarHelper.setupNavbar(navbar, this);

        loadSampleData();
        adapter = new RuteViewAdapter(ruteList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadSampleData() {
        ruteList.add(Rute.builder()
                .uuid("123ABC")
                .estado("Disponible")
                .build());

        ruteList.add(Rute.builder()
                .uuid("123ABD")
                .estado("Pendiente")
                .build());
    }

    @Override
    public void onItemClick(Rute rute) {

    }

    @Override
    public void onDetailsClick(Rute rute) {

    }
}