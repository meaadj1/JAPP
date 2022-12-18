package com.example.japp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.japp.databinding.ActiveJobItemBinding;
import com.example.japp.model.Job;
import java.util.ArrayList;
import java.util.Objects;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder> {

    ArrayList<Job> list;

    public ActiveAdapter(ArrayList<Job> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ActiveJobItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(list.get(position).getTitle());
        holder.binding.tvDetails.setText(list.get(position).getDescription());
        if (Objects.equals(list.get(position).getStatus(), "accept")) {
            holder.binding.tvAccept.setVisibility(View.VISIBLE);
            holder.binding.tvReject.setVisibility(View.GONE);
        } else {
            holder.binding.tvAccept.setVisibility(View.GONE);
            holder.binding.tvReject.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActiveJobItemBinding binding;

        ViewHolder(ActiveJobItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
