package com.example.japp.ui.fragment.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.R;
import com.example.japp.Utils.MergeSort;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentHomeBinding;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

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
            if (!jobs.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                String json = new SharedHelper().getString(context, SharedHelper.user);
                String uid = new SharedHelper().getString(context, SharedHelper.uid);
                User user = new Gson().fromJson(json, User.class);
                binding.rvJob.setAdapter(new JobsAdapter(jobs, uid, user.getSkills(), viewModel, "HOME"));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });

        viewModel.applicants.observe(getViewLifecycleOwner(), users -> {
            if (!users.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                Log.i(TAG, "Not empty");
                Log.i(TAG, String.valueOf(users.size()));
                handleApplicants(users);
            } else {
                Log.i(TAG, "empty");
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });
    }

    private void handleApplicants(List<User> users) {
        MergeSort ob = new MergeSort();
        ob.mergeSort(users, 0, users.size() - 1);
        binding.rvJob.setAdapter(new ApplicantsAdapter(users));
    }
}