package com.example.japp.ui.fragment.jobs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.JobsPagerAdapter;
import com.example.japp.databinding.FragmentJobsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class JobsFragment extends Fragment {
    private FragmentJobsBinding binding;
    Context context;

    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = binding.getRoot().getContext();

        JobsPagerAdapter adapter = new JobsPagerAdapter(context, requireActivity(), new SharedHelper().getString(context, SharedHelper.type));
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, true, (tab, position) -> tab.setText(adapter.getPageTitle(position))).attach();


    }
}