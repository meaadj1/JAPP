package com.example.japp.ui.fragment.saved_jobs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentSavedJobsBinding;
import com.example.japp.model.Job;

import java.util.ArrayList;

public class SavedJobsFragment extends Fragment {

    private FragmentSavedJobsBinding binding;
    private SavedViewModel viewModel;

    public SavedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SavedViewModel.class);

        viewModel.getSavedJobs(getContext());

        viewModel.jobs.observe(getViewLifecycleOwner(), new Observer<ArrayList<Job>>() {
            @Override
            public void onChanged(ArrayList<Job> list) {
                if (list != null) {
                    binding.rvJobs.setAdapter(new JobsAdapter(list,"SAVED"));
                }
            }
        });
    }
}