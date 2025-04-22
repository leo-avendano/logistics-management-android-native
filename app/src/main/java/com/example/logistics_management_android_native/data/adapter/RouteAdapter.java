package com.example.logistics_management_android_native.data.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.data.model.Package;
import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.data.repository.FirebaseRouteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private List<Route> routeList;
    private OnItemClickListener listener;
    private List<Boolean> expandedStates;
    private FirebaseRouteRepository routeRepository;

    public interface OnItemClickListener {
        void onItemClick(Route route);
        void onDetailsClick(Route route);
    }

    public RouteAdapter(List<Route> routeList) {
        this.routeList = new ArrayList<>();
        this.expandedStates = new ArrayList<>();
        this.routeRepository = new FirebaseRouteRepository();
        filterRoutesWithPackages(routeList);
    }

    private void filterRoutesWithPackages(List<Route> routes) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (Route route : routes) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);
            
            routeRepository.getPackagesForRoute(route.getUuid())
                    .addOnSuccessListener(packages -> {
                        if (packages != null && !packages.isEmpty()) {
                            synchronized (this) {
                                this.routeList.add(route);
                                this.expandedStates.add(false);
                            }
                        }
                        future.complete(null);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("RouteAdapter", "Error checking packages for route: " + e.getMessage());
                        future.complete(null);
                    });
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    notifyDataSetChanged();
                });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_layout, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.routeNameTextView.setText(route.getUuid());
        holder.routeStatusTextView.setText(route.getEstado());

        String estado = route.getEstado().toLowerCase();
        switch (estado) {
            case "disponible":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Disponible);
                holder.actionButton.setVisibility(View.VISIBLE);
                holder.actionButton.setText("Reservar");
                break;
            case "pendiente":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Pendiente);
                holder.actionButton.setVisibility(View.VISIBLE);
                holder.actionButton.setText("Quitar");
                break;
            case "en progreso":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_EnProgreso);
                holder.actionButton.setVisibility(View.GONE);
                break;
            case "completado":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Completado);
                holder.actionButton.setVisibility(View.GONE);
                break;
            case "fallida":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Fallida);
                holder.actionButton.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            boolean isExpanded = expandedStates.get(position);
            expandedStates.set(position, !isExpanded);
            holder.toggleExpandCollapse(!isExpanded);
            
            if (!isExpanded) {
                loadPackagesForRoute(route.getUuid(), holder.packagesRecyclerView);
            }
        });

        holder.packagesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.packagesRecyclerView.setAdapter(new PackageAdapter(new ArrayList<>()));

        holder.packageDetailsLayout.setVisibility(expandedStates.get(position) ? View.VISIBLE : View.GONE);
        holder.expandIcon.setRotation(expandedStates.get(position) ? 180 : 0);

        holder.actionButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailsClick(route);
            }
        });
    }

    private void loadPackagesForRoute(String routeId, RecyclerView recyclerView) {
        routeRepository.getPackagesForRoute(routeId)
                .addOnSuccessListener(packages -> {
                    PackageAdapter adapter = (PackageAdapter) recyclerView.getAdapter();
                    if (adapter != null) {
                        adapter.updateList(packages);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("RouteAdapter", "Error loading packages: " + e.getMessage());
                });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public void updateList(List<Route> newList) {
        routeList.clear();
        expandedStates.clear();
        filterRoutesWithPackages(newList);
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView routeNameTextView;
        TextView routeStatusTextView;
        Button actionButton;
        ImageView expandIcon;
        LinearLayout packageDetailsLayout;
        RecyclerView packagesRecyclerView;

        RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeNameTextView = itemView.findViewById(R.id.routeNameTextView);
            routeStatusTextView = itemView.findViewById(R.id.routeStatusTextView);
            actionButton = itemView.findViewById(R.id.actionButton);
            expandIcon = itemView.findViewById(R.id.expandIcon);
            packageDetailsLayout = itemView.findViewById(R.id.packageDetailsLayout);
            packagesRecyclerView = itemView.findViewById(R.id.packagesRecyclerView);
        }

        void toggleExpandCollapse(boolean expand) {
            if (expand) {
                packageDetailsLayout.setVisibility(View.VISIBLE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_180);
                expandIcon.startAnimation(rotateAnimation);
            } else {
                packageDetailsLayout.setVisibility(View.GONE);
                Animation rotateAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_0);
                expandIcon.startAnimation(rotateAnimation);
            }
        }
    }
}
