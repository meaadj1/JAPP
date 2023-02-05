package com.example.japp.ui.fragment.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentNotificationBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private NotificationViewModel viewModel;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);
        User user = new Gson().fromJson(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user), User.class);

        if (Objects.equals(type, "JOB_SEEKER"))
            viewModel.getJobs(binding.getRoot().getContext());
        else
            viewModel.getApplicants(binding.getRoot().getContext());

        viewModel.jobs.observe(getViewLifecycleOwner(), jobs -> {
            if (!jobs.isEmpty()) {
                binding.tvNotFound.setVisibility(View.GONE);
                binding.ivNotFound.setVisibility(View.GONE);
                binding.rvNotification.setVisibility(View.VISIBLE);
                handleJobs(jobs, user);
            } else {
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.rvNotification.setVisibility(View.GONE);
            }
        });

        viewModel.applicants.observe(getViewLifecycleOwner(), users -> {
            if (!users.isEmpty()) {
                binding.tvNotFound.setVisibility(View.GONE);
                binding.ivNotFound.setVisibility(View.GONE);
                binding.rvNotification.setVisibility(View.VISIBLE);
                binding.rvNotification.setAdapter(new ApplicantsAdapter(users));
            } else {
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.rvNotification.setVisibility(View.GONE);
            }
        });
    }

    private void handleJobs(ArrayList<Job> jobs, User user) {
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
            binding.rvNotification.setVisibility(View.GONE);
        } else
            binding.rvNotification.setAdapter(new JobsAdapter(userJobs, "SAVED"));
    }
}