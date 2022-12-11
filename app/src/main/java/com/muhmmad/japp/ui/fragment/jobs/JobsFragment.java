package com.muhmmad.japp.ui.fragment.jobs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.muhmmad.japp.R;
import com.muhmmad.japp.adapter.JobsPagerAdapter;
import com.muhmmad.japp.databinding.FragmentJobsBinding;

public class JobsFragment extends Fragment {


    private static final String TAG = "JobsFragment";

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.viewPager.setAdapter(new JobsPagerAdapter(getContext(), getParentFragmentManager()));

        binding.tabLayout.setupWithViewPager(binding.viewPager);


        for(int i=0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 7, 0);
            tab.requestLayout();
        }
    }
}