package com.example.japp.ui.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentHomeBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private String type;
    Context context;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = binding.getRoot().getContext();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);

        if (Objects.equals(type, "JOB_SEEKER")) {
            binding.tvTitle.setText(getString(R.string.find_your_job));
            binding.llJobType.setVisibility(View.VISIBLE);
            binding.tvJob.setText(getString(R.string.job_list));
            viewModel.getJobs(context);
        } else {
            binding.tvTitle.setText(getString(R.string.find_your_employee));
            binding.llJobType.setVisibility(View.GONE);
            binding.tvJob.setText(getString(R.string.applicant_list));
            viewModel.getApplicants(context);
        }

        binding.btnFullTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getFullTimeJobs(binding.getRoot().getContext());
            }
        });

        binding.btnPartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getPartTimeJobs(binding.getRoot().getContext());
            }
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), new Observer<ArrayList<Job>>() {
            @Override
            public void onChanged(ArrayList<Job> jobs) {
                if (jobs != null) {
                    String json = new SharedHelper().getString(context, SharedHelper.user);
                    User user = new Gson().fromJson(json, User.class);
                    binding.rvJob.setAdapter(new JobsAdapter(jobs, user.getSkills(), viewModel, "HOME"));
                }
            }
        });

        viewModel.applicants.observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    String json = new SharedHelper().getString(context, SharedHelper.user);
                    User user = new Gson().fromJson(json, User.class);
                    if (user.getJobs() != null)
                        binding.rvJob.setAdapter(new ApplicantsAdapter(users, user.getJobs()));

                }
            }
        });
    }
}