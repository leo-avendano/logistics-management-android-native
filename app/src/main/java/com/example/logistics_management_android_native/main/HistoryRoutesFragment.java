package com.example.logistics_management_android_native.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.data.adapter.HistoryRuteAdapter;
import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.data.repository.FirebaseRouteRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HistoryRoutesFragment extends Fragment {

    private HistoryRuteAdapter adapter;
    private FirebaseRouteRepository routeRepository;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeRepository = new FirebaseRouteRepository();
        mAuth = FirebaseAuth.getInstance();
    }

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRepartidorRoutes();
    }

    private void loadRepartidorRoutes() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "No autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String repartidorId = currentUser.getUid();

        routeRepository.getRoutesByRepartidor(repartidorId, 20) // Limita a 20 rutas recientes
                .addOnSuccessListener(routes -> {
                    adapter.clearItems();
                    for (Route route : routes) {
                        adapter.addItem(route);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar historial: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}