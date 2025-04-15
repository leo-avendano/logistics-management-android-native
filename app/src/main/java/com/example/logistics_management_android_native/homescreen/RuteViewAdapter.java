package com.example.logistics_management_android_native.homescreen;

import com.example.logistics_management_android_native.model.Rute;
import com.example.logistics_management_android_native.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RuteViewAdapter extends RecyclerView.Adapter<RuteViewAdapter.RuteViewHolder> {

    private List<Rute> ruteList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Rute rute);
        void onDetailsClick(Rute rute);
    }

    public RuteViewAdapter(List<Rute> ruteList) {
        this.ruteList = ruteList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rute_layout, parent, false);
        return new RuteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuteViewHolder holder, int position) {
        Rute rute = ruteList.get(position);

        holder.textViewUUID.setText(rute.getUuid());
        holder.textViewEstado.setText(rute.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onItemClick(rute);
            }
        });

        holder.buttonDetalles.setOnClickListener(v -> {
            if(listener != null) {
                listener.onDetailsClick(rute);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ruteList.size();
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
    public void updateList(List<Rute> newList) {
        ruteList = newList;
        notifyDataSetChanged();
    }
}
