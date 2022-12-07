package com.muhmmad.japp.ui.fragment.jobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhmmad.japp.R;
import com.muhmmad.japp.databinding.FragmentJobsBinding;

public class JobsFragment extends Fragment {

    private FragmentJobsBinding binding;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}