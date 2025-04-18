package com.example.logistics_management_android_native.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.home.RouteViewAdapter;
import com.example.logistics_management_android_native.model.Route;

import java.util.ArrayList;
import java.util.List;


public class AvailableRoutesFragment extends Fragment implements RouteViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RouteViewAdapter adapter;
    private List<Route> routeList = new ArrayList<>();

    public AvailableRoutesFragment() {
        super(R.layout.fragment_available_routes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewRutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        View navbar = view.findViewById(R.id.navbar_menu);
//        NavbarHelper.setupNavbar(navbar, requireActivity());

        loadSampleData();
        adapter = new RouteViewAdapter(routeList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadSampleData() {
        // Sample route data, or fetch from API/DB
    }

    @Override
    public void onItemClick(Route route) {
        // handle item click
    }

    @Override
    public void onDetailsClick(Route route) {
        // handle details click
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_routes, container, false);
    }
}