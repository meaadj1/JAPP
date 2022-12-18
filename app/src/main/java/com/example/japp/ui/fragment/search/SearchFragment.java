package com.example.japp.ui.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.R;
import com.example.japp.adapter.CategoriesAdapter;
import com.example.japp.databinding.FragmentSearchBinding;
import com.example.japp.model.Category;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Category> categories = new ArrayList();
        categories.add(new Category(getString(R.string.education), R.drawable.ic_education));
        categories.add(new Category(getString(R.string.finance), R.drawable.ic_finance));
        categories.add(new Category(getString(R.string.restaurant), R.drawable.ic_resturant));
        categories.add(new Category(getString(R.string.programming), R.drawable.ic_programming));
        categories.add(new Category(getString(R.string.health), R.drawable.ic_health));

        binding.rvCategories.setAdapter(new CategoriesAdapter(categories));
    }
}