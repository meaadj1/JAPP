package com.example.japp.ui.fragment.pending_jobs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.PendingAdapter;
import com.example.japp.databinding.FragmentPendingJobsBinding;

import java.util.Objects;

public class PendingJobsFragment extends Fragment {

    private FragmentPendingJobsBinding binding;

    public PendingJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingJobsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PendingViewModel viewModel = new ViewModelProvider(this).get(PendingViewModel.class);

        if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER"))
            viewModel.getPendingJobs(getContext());
        else
            viewModel.getPosts(binding.getRoot().getContext());

        viewModel.pendingJobs.observe(getViewLifecycleOwner(), jobs -> {
            if (!jobs.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJobs.setVisibility(View.VISIBLE);
                binding.rvJobs.setAdapter(new PendingAdapter(jobs));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJobs.setVisibility(View.GONE);
            }
        });
    }
}