package com.example.japp.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.japp.R;
import com.example.japp.databinding.ApplicantItemBinding;
import com.example.japp.model.User;

import java.util.ArrayList;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ViewHolder> {

    private static final String TAG = "ApplicantsAdapter";
    ArrayList<User> list;

    public ApplicantsAdapter(ArrayList<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ApplicantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvApplicant.setText(list.get(position).getName());
        holder.binding.tvPresent.setText(list.get(position).getMatching() + "%");
        holder.binding.progressBar.setProgress((int) list.get(position).getMatching());
        Glide.with(holder.binding.getRoot()).load(list.get(position).getPhoto()).placeholder(R.drawable.place_holder).into(holder.binding.ivApplicant);
        if (list.get(position).getCity() != null)
            holder.binding.tvDetails.setText(list.get(position).getCity());

        holder.binding.ivPresent.setOnClickListener(v -> {
            if (holder.binding.cvDetails.getVisibility() == View.VISIBLE) {
                holder.binding.cvDetails.setVisibility(View.GONE);
            } else {
                holder.binding.cvDetails.setVisibility(View.VISIBLE);
            }
        });

        holder.binding.getRoot().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", list.get(holder.getAdapterPosition()));
            Navigation.createNavigateOnClickListener(R.id.nav_job_details, bundle).onClick(v);
        });
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
