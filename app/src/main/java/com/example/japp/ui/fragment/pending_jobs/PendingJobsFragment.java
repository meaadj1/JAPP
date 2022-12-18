package com.example.japp.ui.fragment.pending_jobs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.adapter.PendingAdapter;
import com.example.japp.databinding.FragmentPendingJobsBinding;
import com.example.japp.model.Job;
import java.util.ArrayList;

public class PendingJobsFragment extends Fragment {

    private FragmentPendingJobsBinding binding;
    private PendingViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(PendingViewModel.class);
        viewModel.getPendingJobs(getContext());

        viewModel.pendingJobs.observe(getViewLifecycleOwner(), new Observer<ArrayList<Job>>() {
            @Override
            public void onChanged(ArrayList<Job> jobs) {
                if (jobs != null) {
                    binding.rvJobs.setAdapter(new PendingAdapter(jobs));
                }
            }
        });
    }
}