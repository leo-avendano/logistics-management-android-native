package com.example.logistics_management_android_native.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.data.adapter.HistoryRuteAdapter;
import com.example.logistics_management_android_native.data.model.Route;

import java.util.ArrayList;

public class HistoryRoutesFragment extends Fragment {

    private HistoryRuteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_history_routes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ArrayList<Route> routeList = new ArrayList<>();
        adapter = new HistoryRuteAdapter(routeList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Sample data
        addItem(Route.builder()
                .uuid("DH95K")
                .fechas(Route.Fechas.builder()
                        .inicioRepartir("15/04/2025")
                        .build())
                .estado("Completada")
                .build());

        addItem(Route.builder()
                .uuid("KFLD4Q")
                .fechas(Route.Fechas.builder()
                        .inicioRepartir("12/04/2025")
                        .build())
                .estado("Completada")
                .build());

        addItem(Route.builder()
                .uuid("UBK64D")
                .fechas(Route.Fechas.builder()
                        .inicioRepartir("11/04/2025")
                        .build())
                .estado("Completada")
                .build());

        addItem(Route.builder()
                .uuid("M9KDJ")
                .fechas(Route.Fechas.builder()
                        .inicioRepartir("11/04/2025")
                        .build())
                .estado("Fallida")
                .build());

        return view;
    }

    private void addItem(Route aRoute ) {
        adapter.addItem(aRoute); // Make sure adapter has this method
    }

}
