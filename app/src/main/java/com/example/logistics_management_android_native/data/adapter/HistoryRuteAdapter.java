package com.example.logistics_management_android_native.data.adapter;

import com.example.logistics_management_android_native.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.logistics_management_android_native.data.model.Route;

public class HistoryRuteAdapter extends RecyclerView.Adapter<HistoryRuteAdapter.ViewHolder> {
    private final List<Route> items;

    public HistoryRuteAdapter(List<Route> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Route item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textId;
        private final TextView textDate;
        private final TextView textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textDate = itemView.findViewById(R.id.textDate);
            textStatus = itemView.findViewById(R.id.textStatus);
        }

        public void bind(Route item) {
            textId.setText(item.getUuid());
            textDate.setText(item.getFechas().getInicioRepartir());
            textStatus.setText(item.getEstado());
        }
    }
}