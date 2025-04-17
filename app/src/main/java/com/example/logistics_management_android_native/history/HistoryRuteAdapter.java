package com.example.logistics_management_android_native.history;

import com.example.logistics_management_android_native.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.logistics_management_android_native.model.PackageHistory;

public class HistoryRuteAdapter extends RecyclerView.Adapter<HistoryRuteAdapter.ViewHolder> {
    private final List<PackageHistory> items;

    public HistoryRuteAdapter(List<PackageHistory> items) {
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

    public void addItem(PackageHistory item) {
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

        public void bind(PackageHistory item) {
            textId.setText(item.getId());
            textDate.setText(item.getDate());
            textStatus.setText(item.getStatus());
        }
    }
}