package com.example.logistics_management_android_native.data.adapter;

import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.R;
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

        holder.textViewUUID.setText(route.getUuid());
        holder.textViewEstado.setText(route.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onItemClick(route);
            }
        });

        holder.buttonDetalles.setOnClickListener(v -> {
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
        TextView textViewUUID;
        TextView textViewEstado;
        Button buttonDetalles;

        public RuteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUUID = itemView.findViewById(R.id.textViewUUID);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            buttonDetalles = itemView.findViewById(R.id.buttonDetalles);
        }
    }
    public void updateList(List<Route> newList) {
        routeList = newList;
        notifyDataSetChanged();
    }
}
