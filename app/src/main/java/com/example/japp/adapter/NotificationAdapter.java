package com.example.japp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.databinding.ApplicantItemBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<String> list;

    public NotificationAdapter(List<String> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.ViewHolder(ApplicantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.ivApplicant.setVisibility(View.GONE);
        holder.binding.ivPresent.setVisibility(View.GONE);
        holder.binding.tvApplicant.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ApplicantItemBinding binding;

        public ViewHolder(ApplicantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
