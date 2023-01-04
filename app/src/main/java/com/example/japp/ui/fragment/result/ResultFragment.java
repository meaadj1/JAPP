package com.example.japp.ui.fragment.result;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.ApplicantsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.FragmentResultBinding;

import java.util.Objects;

public class ResultFragment extends Fragment {
    private FragmentResultBinding binding;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ResultViewModel viewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        if (Objects.equals(requireArguments().getString("category"), "jobs")) {
            viewModel.getJobsByCity(requireArguments().getString("city"));
        } else if (Objects.equals(requireArguments().getString("category"), "users")) {
            viewModel.getUsersByCity(requireArguments().getString("city"));
        } else {
            if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER"))
                viewModel.getJobsByCategory(requireArguments().getString("category"));
        }

        viewModel.users.observe(getViewLifecycleOwner(), list -> {
            if (!list.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                binding.rvJob.setAdapter(new ApplicantsAdapter(list));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), jobs -> {
            if (!jobs.isEmpty()) {
                binding.ivNotFound.setVisibility(View.GONE);
                binding.tvNotFound.setVisibility(View.GONE);
                binding.rvJob.setVisibility(View.VISIBLE);
                binding.rvJob.setAdapter(new JobsAdapter(jobs, "SAVED"));
            } else {
                binding.ivNotFound.setVisibility(View.VISIBLE);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.rvJob.setVisibility(View.GONE);
            }
        });

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
    }
}