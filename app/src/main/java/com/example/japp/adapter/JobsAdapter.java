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
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.JobItemBinding;
import com.example.japp.model.Job;
import com.example.japp.ui.fragment.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private static final String TAG = "JobsAdapter";

    private ArrayList<Job> list;
    private ArrayList<String> skills;
    private HomeViewModel viewModel;
    private String type;

    public JobsAdapter(ArrayList<Job> list, ArrayList<String> skills, HomeViewModel viewModel, String type) {
        this.list = list;
        this.skills = skills;
        this.viewModel = viewModel;
        this.type = type;
    }

    public JobsAdapter(ArrayList<Job> list, String type) {
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(JobItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Objects.equals(type, "SAVED")) {
            holder.binding.tvTitle.setText(list.get(position).getTitle());
            holder.binding.btnCategory.setText(list.get(position).getCategory());
            holder.binding.btnTime.setText(list.get(position).getType());

            holder.binding.ivPercentage.setVisibility(View.GONE);
            holder.binding.ivSave.setVisibility(View.GONE);
            holder.binding.btnReadMore.setVisibility(View.GONE);

            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list.get(holder.getAdapterPosition()));
                    Navigation.createNavigateOnClickListener(R.id.nav_job_details, bundle).onClick(v);
                }
            });
        } else if (Objects.equals(type, "HOME")) {
            float validate = 0;
            holder.binding.tvTitle.setText(list.get(position).getTitle());
            holder.binding.btnCategory.setText(list.get(position).getCategory());
            holder.binding.btnTime.setText(list.get(position).getType());
            if (!list.get(position).getCompanyImage().isEmpty())
                Glide.with(holder.binding.getRoot()).load(list.get(position).getCompanyImage()).into(holder.binding.ivCompany);
            for (int i = 0; i < list.get(position).getRequirements().size(); i++) {

                Log.i(TAG, list.get(position).getRequirements().get(i));

                if (skills != null) {
                    for (int j = 0; j < skills.size(); j++) {
                        Log.i(TAG, skills.get(j));
                        if (Objects.equals(list.get(position).getRequirements().get(i), skills.get(j)))
                            validate++;
                    }
                }
            }

            validate = (validate / list.get(position).getRequirements().size()) * 100;
            holder.binding.progressBar.setProgress((int) validate);
            holder.binding.tvPresent.setText((int) validate + "%");

            try {
                for (int i = 0; i < list.get(position).getSaved().size(); i++) {
                    if (Objects.equals(list.get(position).getSaved().get(i), new SharedHelper().getString(holder.binding.getRoot().getContext(), SharedHelper.uid)))
                        holder.binding.ivSave.setImageResource(R.drawable.ic_saved);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list.get(holder.getAdapterPosition()));
                    Navigation.createNavigateOnClickListener(R.id.nav_job_details, bundle).onClick(v);
                }
            });

            holder.binding.btnReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list.get(holder.getAdapterPosition()));
                    Navigation.createNavigateOnClickListener(R.id.nav_job_details, bundle).onClick(v);
                }
            });

            holder.binding.ivSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.savingJob(holder.binding.getRoot().getContext(), holder.getAdapterPosition());
                    holder.binding.ivSave.setImageResource(R.drawable.ic_saved);
                }
            });
        }
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