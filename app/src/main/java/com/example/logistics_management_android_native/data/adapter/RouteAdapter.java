package com.example.logistics_management_android_native.data.adapter;

import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RuteViewHolder> {

    private List<Route> routeList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Route route);
        void onDetailsClick(Route route);
    }

    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_layout, parent, false);
        return new RuteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuteViewHolder holder, int position) {
        Route route = routeList.get(position);

        holder.routeNameTextView.setText(route.getUuid());
        holder.routeStatusTextView.setText(route.getEstado());

        String estado = route.getEstado().toLowerCase();
        Log.println(Log.INFO, "route", estado);
        switch (estado) {
            case "disponible":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Disponible);
                holder.actionButton.setVisibility(View.VISIBLE);
                holder.actionButton.setText("Reservar");
                break;
            case "pendiente":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_Pendiente);
                holder.actionButton.setVisibility(View.VISIBLE);
                holder.actionButton.setText("Borrar");
                break;
            case "en progreso":
                holder.routeStatusTextView.setTextAppearance(R.style.TextRowStatus_EnProgreso);
                holder.actionButton.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onItemClick(route);
            }
        });

        holder.actionButton.setOnClickListener(v -> {
            if(listener != null) {
                listener.onDetailsClick(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class RuteViewHolder extends RecyclerView.ViewHolder {
        TextView routeNameTextView;
        TextView routeStatusTextView;
        Button actionButton;

        public RuteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeNameTextView = itemView.findViewById(R.id.routeNameTextView);
            routeStatusTextView = itemView.findViewById(R.id.routeStatusTextView);
            actionButton = itemView.findViewById(R.id.actionButton);
        }
    }

    public void updateList(List<Route> newList) {
        routeList = newList;
        notifyDataSetChanged();
    }
}
