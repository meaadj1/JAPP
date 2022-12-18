package com.example.japp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.databinding.PendingItemBinding;
import com.example.japp.model.Job;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {

    ArrayList<Job> list;

    public PendingAdapter(ArrayList<Job> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PendingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(list.get(position).getTitle());
        holder.binding.tvCompany.setText(list.get(position).getCompanyName());
        holder.binding.tvPosition.setText(list.get(position).getSpecialization());
        holder.binding.tvType.setText(list.get(position).getType());
        holder.binding.tvExperience.setText(list.get(position).getExperience());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PendingItemBinding binding;

        public ViewHolder(PendingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
