package com.example.japp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.japp.databinding.ApplicantItemBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;

import java.util.ArrayList;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ViewHolder> {

    private static final String TAG = "ApplicantsAdapter";

    ArrayList<User> list;
    ArrayList<Job> jobs;

    public ApplicantsAdapter(ArrayList<User> list, ArrayList<Job> jobs) {
        this.list = list;
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ApplicantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvApplicant.setText(list.get(position).getName());
        Glide.with(holder.binding.getRoot()).load(list.get(position).getPhoto()).into(holder.binding.ivApplicant);
        holder.binding.tvDetails.setText(list.get(position).getCity().toString());

        holder.binding.ivPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.cvDetails.getVisibility() == View.VISIBLE)
                    holder.binding.cvDetails.setVisibility(View.GONE);
                else
                    holder.binding.cvDetails.setVisibility(View.VISIBLE);
            }
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
