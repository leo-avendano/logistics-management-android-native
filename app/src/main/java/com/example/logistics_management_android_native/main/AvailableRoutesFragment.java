package com.example.logistics_management_android_native.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.data.adapter.PackageAdapter;
import com.example.logistics_management_android_native.data.adapter.RouteAdapter;
import com.example.logistics_management_android_native.data.interfaces.LogisticsService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsServiceCallback;
import com.example.logistics_management_android_native.data.model.Package;
import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.data.repository.FirebaseRouteRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AvailableRoutesFragment extends Fragment implements RouteAdapter.OnItemClickListener {

    private final List<Route> routeList = new ArrayList<>();
    private RouteAdapter adapter;
    private FirebaseRouteRepository routeRepository;
    private FirebaseAuth mAuth;
    private RadioGroup radioGroupFiltro;
    private String currentFilter = "Todas";
    private FirebaseFirestore db;

    @Inject
    LogisticsService logisticsService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeRepository = new FirebaseRouteRepository();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_available_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RouteAdapter(routeList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        radioGroupFiltro = view.findViewById(R.id.radioGroupFiltro);
        setupRadioGroup();

        RadioButton radioButtonAll = view.findViewById(R.id.radioButtonAll);
        radioButtonAll.setChecked(true);

        loadRoutes();
    }

    private void setupRadioGroup() {
        radioGroupFiltro.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            updateRadioButtonStyles(checkedId);
            if (selectedRadioButton != null) {
                currentFilter = selectedRadioButton.getText().toString();
                loadRoutes();
            }
        });
    }

    private void loadRoutes() {
        String repartidorId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        if (currentFilter.equals("Todas")) {
            routeRepository.getRoutesByStatusAndRepartidor("Todas", repartidorId)
                    .addOnSuccessListener(this::updateRouteList)
                    .addOnFailureListener(this::handleError);
        } else {
            String estadoFirebase = convertToFirebaseEstado(currentFilter);
            routeRepository.getRoutesByStatusAndRepartidor(estadoFirebase, repartidorId)
                    .addOnSuccessListener(this::updateRouteList)
                    .addOnFailureListener(this::handleError);
        }
    }

    private String convertToFirebaseEstado(String estadoUI) {
        switch (estadoUI) {
            case "Disponible": return "disponible";
            case "Pendiente": return "pendiente";
            case "En Progreso": return "en progreso";
            case "Completado": return "completado";
            case "Fallida": return "fallida";
            default: return estadoUI.toLowerCase();
        }
    }

    private void updateRouteList(List<Route> routes) {
        routeList.clear();
        routeList.addAll(routes);
        adapter.updateList(routeList);
    }

    private void handleError(Exception e) {
        Toast.makeText(getContext(), "Error al cargar rutas: " + e.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Route route) {
        // This is now handled by the adapter's expand/collapse functionality
    }

    @Override
    public void onDetailsClick(Route route) {
        // Handle the action button click
        String estado = route.getEstado().toLowerCase();
        switch (estado) {
            case "disponible":
                logisticsService.assignRouteToRepartidor(route.getUuid(), mAuth.getCurrentUser().getUid(), new LogisticsServiceCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadRoutes();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "pendiente":
                logisticsService.unassignRouteFromRepartidor(route.getUuid(), new LogisticsServiceCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadRoutes();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private void updateRadioButtonStyles(int checkedId) {
        for (int i = 0; i < radioGroupFiltro.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupFiltro.getChildAt(i);
            if (radioButton.getId() == checkedId) {
                radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                radioButton.setBackgroundResource(R.drawable.radio_dark_background);
            } else {
                radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow));
                radioButton.setBackgroundResource(R.drawable.radio_yellow_background);
            }
        }
    }
}