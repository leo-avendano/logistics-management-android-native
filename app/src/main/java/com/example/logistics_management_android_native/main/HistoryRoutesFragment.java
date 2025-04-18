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
import com.example.logistics_management_android_native.data.model.PackageHistory;

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
        ArrayList<PackageHistory> packageList = new ArrayList<>();
        adapter = new HistoryRuteAdapter(packageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Sample data
        addItem(new PackageHistory("Paquete DH95K", "15/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete KFLD4Q", "12/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete UBK64D", "11/04/2025", "Completada"));
        addItem(new PackageHistory("Paquete M9KDJ", "11/04/2025", "Fallida"));

        return view;
    }

    private void addItem(PackageHistory packageHistory) {
        adapter.addItem(packageHistory); // Make sure adapter has this method
    }

}
