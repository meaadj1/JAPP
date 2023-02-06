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
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentHomeBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
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

        LocaleHelper.setLocale(context, LocaleHelper.getLanguage(binding.getRoot().getContext()));


        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        String type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);

        if (Objects.equals(type, "JOB_SEEKER")) {
            binding.tvTitle.setText(getString(R.string.find_your_job));
            binding.rgJobType.setVisibility(View.VISIBLE);
            binding.tvJob.setText(getString(R.string.job_list));
            viewModel.getJobs(context);
        } else {
            binding.tvTitle.setText(getString(R.string.find_your_employee));
            binding.rgJobType.setVisibility(View.GONE);
            binding.tvJob.setText(getString(R.string.applicant_list));
            viewModel.getApplicants(context);
        }

        binding.rgJobType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_full_time) {
                viewModel.getFullTimeJobs(binding.getRoot().getContext());
            } else if (checkedId == R.id.rb_part_time) {
                viewModel.getPartTimeJobs(binding.getRoot().getContext());
            } else {
                viewModel.getJobs(binding.getRoot().getContext());
            }
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), jobs -> {
            if (!jobs.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                handleJobs(jobs, new SharedHelper().getString(context, SharedHelper.uid), new Gson().fromJson(new SharedHelper().getString(context, SharedHelper.user), User.class));
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
                handleApplicants(users);
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });
    }

    private void handleApplicants(List<User> users) {
//        MergeSort ob = new MergeSort();
//        ob.mergeSort(users, 0, users.size() - 1);
        binding.rvJob.setAdapter(new ApplicantsAdapter(users));
    }

    private void handleJobs(ArrayList<Job> jobs, String uid, User user) {
        ArrayList<Job> userJobs = new ArrayList<>();
        jobs.forEach(job -> {
            final Boolean[] isFound = {false};
            if (job.getApplicants() != null) {
                job.getApplicants().forEach(user1 -> {
                    if (Objects.equals(user1.getEmail(), user.getEmail())) {
                        isFound[0] = true;
                    }
                });
            }
            if (!isFound[0])
                userJobs.add(job);
        });

        if (userJobs.isEmpty()) {
            binding.ivNotFound.setVisibility(View.VISIBLE);
            binding.tvNotFound.setVisibility(View.VISIBLE);
            binding.rvJob.setVisibility(View.GONE);
        } else
            binding.rvJob.setAdapter(new JobsAdapter(userJobs, uid, user.getSkills(), viewModel, "HOME"));
    }
}