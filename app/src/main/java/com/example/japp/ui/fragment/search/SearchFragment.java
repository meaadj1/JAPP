package com.example.japp.ui.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.CategoriesAdapter;
import com.example.japp.databinding.FragmentSearchBinding;
import com.example.japp.model.Category;
import com.example.japp.ui.fragment.result.ResultViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ResultViewModel viewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER")) {
            binding.tvTitle.setText(getString(R.string.find_your_job));
        } else {
            binding.tvTitle.setText(getString(R.string.find_your_employee));
        }

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.education), R.drawable.ic_education));
        categories.add(new Category(getString(R.string.finance), R.drawable.ic_finance));
        categories.add(new Category(getString(R.string.restaurant), R.drawable.ic_resturant));
        categories.add(new Category(getString(R.string.programming), R.drawable.ic_programming));
        categories.add(new Category(getString(R.string.health), R.drawable.ic_health));

        binding.rvCategories.setAdapter(new CategoriesAdapter(categories));

        binding.ivLocation.setOnClickListener(v -> {
            if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER")) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "jobs");
                Navigation.createNavigateOnClickListener(R.id.resultFragment, bundle).onClick(v);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("category", "users");
                Navigation.createNavigateOnClickListener(R.id.resultFragment, bundle).onClick(v);
            }
        });
    }
}