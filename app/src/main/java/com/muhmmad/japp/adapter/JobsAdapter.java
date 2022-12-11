package com.muhmmad.japp.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.muhmmad.japp.R;
import com.muhmmad.japp.databinding.JobItemBinding;
import com.muhmmad.japp.model.Job;

import java.util.ArrayList;
import java.util.Objects;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private static final String TAG = "JobsAdapter";

    private ArrayList<Job> list;
    private ArrayList<String> skills;

    public JobsAdapter(ArrayList<Job> list, ArrayList<String> skills) {
        this.list = list;
        this.skills = skills;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(JobItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        float validate = 0;
        holder.binding.tvTitle.setText(list.get(position).getTitle());
        holder.binding.btnCategory.setText(list.get(position).getCategory());
        holder.binding.btnTime.setText(list.get(position).getType());
        for (int i = 0; i < list.get(position).getRequirements().size(); i++) {
            for (int j = 0; j < skills.size(); j++) {
                if (Objects.equals(list.get(position).getRequirements().get(i), skills.get(j)))
                    validate++;
            }
        }

        validate = (validate / list.get(position).getRequirements().size()) * 100;
        holder.binding.progressBar.setProgress((int) validate);
        holder.binding.tvPresent.setText((int) validate + "%");

        holder.binding.ivPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.cvDetails.getVisibility() == View.VISIBLE)
                    holder.binding.cvDetails.setVisibility(View.GONE);
                else
                    holder.binding.cvDetails.setVisibility(View.VISIBLE);
            }
        });

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, list.get(holder.getAdapterPosition()).toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", list.get(holder.getAdapterPosition()));
                Navigation.createNavigateOnClickListener(R.id.jobDetailsFragment, bundle).onClick(v);
            }
        });

        holder.binding.btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", list.get(holder.getAdapterPosition()));
                Navigation.createNavigateOnClickListener(R.id.jobDetailsFragment, bundle).onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        JobItemBinding binding;

        public ViewHolder(JobItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}