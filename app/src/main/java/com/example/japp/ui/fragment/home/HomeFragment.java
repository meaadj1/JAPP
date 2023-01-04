package com.example.japp.ui.fragment.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentHomeBinding;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
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
        String type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);

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

        binding.btnFullTime.setOnClickListener(v -> viewModel.getFullTimeJobs(binding.getRoot().getContext()));

        binding.btnPartTime.setOnClickListener(v -> viewModel.getPartTimeJobs(binding.getRoot().getContext()));

        binding.btnAll.setOnClickListener(v -> viewModel.getJobs(binding.getRoot().getContext()));

        viewModel.jobs.observe(getViewLifecycleOwner(), jobs -> {
            if (jobs != null) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                String json = new SharedHelper().getString(context, SharedHelper.user);
                User user = new Gson().fromJson(json, User.class);
                binding.rvJob.setAdapter(new JobsAdapter(jobs, user.getSkills(), viewModel, "HOME"));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });

        viewModel.applicants.observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                binding.rvJob.setAdapter(new ApplicantsAdapter(users));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });
    }
}