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
import com.example.japp.model.User;
import com.google.gson.Gson;

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
            String data = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user);
            User user = new Gson().fromJson(data, User.class);
            viewModel.getJobsByCity(user.getCity());
        } else if (Objects.equals(requireArguments().getString("category"), "users")) {
            String data = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user);
            User user = new Gson().fromJson(data, User.class);
            viewModel.getUsersByCity(user.getCity());
        } else {
            if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER"))
                viewModel.getJobsByCategory(requireArguments().getString("category"));
        }

        viewModel.users.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                String json = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user);
                binding.rvJob.setAdapter(new ApplicantsAdapter(list));
            }
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), jobs -> {
            if (jobs != null) {
                String json = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user);
                User user = new Gson().fromJson(json, User.class);
                binding.rvJob.setAdapter(new JobsAdapter(jobs, "SAVED"));
            }
        });

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

    }
}