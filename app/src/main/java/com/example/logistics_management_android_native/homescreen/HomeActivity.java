package com.example.logistics_management_android_native.homescreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistics_management_android_native.R;
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
//        Intent intent = new Intent(this, RuteActivity.class);
//        intent.putExtra("UUID", rute.getUuid());
//        startActivity(intent);
    }

    @Override
    public void onDetailsClick(Rute rute) {
//        Intent intent = new Intent(this, DetalleActivity.class);
//        intent.putExtra("UUID", rute.getUuid());
//        startActivity(intent);
    }
}