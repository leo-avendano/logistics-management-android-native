package com.example.logistics_management_android_native.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.data.model.Package;

import java.util.ArrayList;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    private List<Package> packageList;

    public PackageAdapter(List<Package> packageList) {
        this.packageList = packageList != null ? packageList : new ArrayList<>();
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        Package pkg = packageList.get(position);
        holder.packageNameTextView.setText(String.format("Nombre: %s", pkg.getNombre()));
        holder.packageDescriptionTextView.setText(String.format("Descripción: %s", pkg.getDescripcion()));
        
        // Ubicación
        Package.Ubicacion ubicacion = pkg.getUbicacion();
        holder.locationTextView.setText(String.format("Ubicación: Depósito %d, Sector %d, Estante %d", 
            ubicacion.getDeposito(), ubicacion.getSector(), ubicacion.getEstante()));
        
        // Dimensiones
        Package.Tamaño tamaño = pkg.getTamaño();
        holder.dimensionsTextView.setText(String.format("Dimensiones: %d x %d x %d cm", 
            tamaño.getAncho(), tamaño.getLargo(), tamaño.getAlto()));
        
        // Peso
        holder.weightTextView.setText(String.format("Peso: %.2f kg", pkg.getPeso()));
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public void updateList(List<Package> newList) {
        this.packageList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView packageNameTextView;
        TextView packageDescriptionTextView;
        TextView locationTextView;
        TextView dimensionsTextView;
        TextView weightTextView;

        PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.packageNameTextView);
            packageDescriptionTextView = itemView.findViewById(R.id.packageDescriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dimensionsTextView = itemView.findViewById(R.id.dimensionsTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
        }
    }
} 