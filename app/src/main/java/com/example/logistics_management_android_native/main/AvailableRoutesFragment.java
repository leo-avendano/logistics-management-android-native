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
import com.example.logistics_management_android_native.data.adapter.RouteAdapter;
import com.example.logistics_management_android_native.data.interfaces.LogisticsService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsServiceCallback;
import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.data.repository.FirebaseRouteRepository;
import com.google.firebase.auth.FirebaseAuth;

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

    @Inject
    LogisticsService logisticsService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeRepository = new FirebaseRouteRepository();
        mAuth = FirebaseAuth.getInstance();
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

        loadRoutes();
    }

    private void setupRadioGroup() {
        radioGroupFiltro.setOnCheckedChangeListener((group, checkedId) -> {
            updateRadioButtonStyles(checkedId);

            if (checkedId == R.id.radioTodas) {
                currentFilter = "Todas";
            } else if (checkedId == R.id.radioDisponible) {
                currentFilter = "Disponible";
            } else if (checkedId == R.id.radioPendiente) {
                currentFilter = "Pendiente";
            } else if (checkedId == R.id.radioEnProgreso) {
                currentFilter = "En Progreso";
            }

            loadRoutes();
        });
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
            case "En Curso": return "en_curso";
            default: return estadoUI.toLowerCase();
        }
    }

    private void updateRouteList(List<Route> routes) {
        routeList.clear();
        routeList.addAll(routes);
        adapter.notifyDataSetChanged();
    }

    private void handleError(Exception e) {
        Toast.makeText(getContext(), "Error al cargar rutas: " + e.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Route route) {
        logisticsService.getHelloWorld(new LogisticsServiceCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d("PEPE", "Hello World response: " + message);
            }

            @Override
            public void onError(Throwable error) {
                Log.e("PEPE", "Error calling hello world: " + error.getMessage());
            }
        });
    }

    @Override
    public void onDetailsClick(Route route) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        if (userId.isEmpty()) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("pendiente".equalsIgnoreCase(route.getEstado())) {
            logisticsService.unassignRouteFromRepartidor(route.getUuid(), new LogisticsServiceCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("PEPE", "Route unassignment success: " + message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    loadRoutes(); // Refresh the routes list
                }

                @Override
                public void onError(Throwable error) {
                    Log.e("PEPE", "Error unassigning route: " + error.getMessage());
                    Toast.makeText(getContext(), "Error unassigning route: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            logisticsService.assignRouteToRepartidor(route.getUuid(), userId, new LogisticsServiceCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("PEPE", "Route assignment success: " + message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    loadRoutes(); // Refresh the routes list
                }

                @Override
                public void onError(Throwable error) {
                    Log.e("PEPE", "Error assigning route: " + error.getMessage());
                    Toast.makeText(getContext(), "Error assigning route: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}