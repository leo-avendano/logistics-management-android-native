package com.example.logistics_management_android_native.data.adapter;

import com.example.logistics_management_android_native.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.logistics_management_android_native.data.model.Route;
import com.example.logistics_management_android_native.main.HistoryRoutesFragment;

public class HistoryRuteAdapter extends RecyclerView.Adapter<HistoryRuteAdapter.ViewHolder> {
    private final List<Route> routeList;
    private final Context context;
    public HistoryRuteAdapter(List<Route> items, Context context) {
        this.routeList = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.bind(routeList.get(position));
        switch (route.getEstado()) {
            case "completado":
                holder.textStatus.setTextAppearance(R.style.TextRowStatus_Completado);
                break;
            case "pendiente":
                holder.textStatus.setTextAppearance(R.style.TextRowStatus_Pendiente);
                break;
            case "en progreso":
                holder.textStatus.setTextAppearance(R.style.TextRowStatus_EnProgreso);
                break;
            case "fallido":
                holder.textStatus.setTextAppearance(R.style.TextRowStatus_Fallida);
                break;
            default:
                holder.textStatus.setTextAppearance(R.style.TextRowStatus);
        }

        holder.btnDetails.setOnClickListener(v -> showRouteDetailsDialog(route));
    }

    private void showRouteDetailsDialog(Route route) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Ruta");
        String message = "ID: " + route.getUuid() + "\n\n" +
                "Estado: " + route.getEstado() + "\n\n" +
                "Cliente: " + route.getCliente() + "\n\n" +
                "Ubicación Destino:\n" +
                " - Latitud: " + route.getDestino().getLat() + "\n" +
                " - Longitud: " + route.getDestino().getLon() + "\n\n" +
                "Fechas:\n" +
                " - Inicio: " + route.getFechas().getInicioRepartir() + "\n" +
                " - Fin: " + route.getFechas().getFinRepartir();
        builder.setMessage(message);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public void addItem(Route item) {
        routeList.add(item);
        notifyItemInserted(routeList.size() - 1);
    }

    public void clearItems() {
        routeList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textId;
        private final TextView textStatus;

        private final Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textStatus = itemView.findViewById(R.id.textStatus);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }

        public void bind(Route item) {
            textId.setText(item.getUuid());
            textStatus.setText(item.getEstado());
        }
    }
}