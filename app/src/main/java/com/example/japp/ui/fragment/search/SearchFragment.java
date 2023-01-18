package com.example.japp.ui.fragment.search;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.CategoriesAdapter;
import com.example.japp.databinding.FragmentSearchBinding;
import com.example.japp.databinding.SearchDialogBinding;
import com.example.japp.model.Category;

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
        if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER")) {
            binding.tvTitle.setText(getString(R.string.find_your_job));
        } else {
            binding.tvTitle.setText(getString(R.string.find_your_employee));
        }

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.education), R.drawable.ic_education));
        categories.add(new Category(getString(R.string.eng), R.drawable.ic_engineering));
        categories.add(new Category(getString(R.string.finance), R.drawable.ic_finance));
        categories.add(new Category(getString(R.string.translation), R.drawable.ic_translation));
        categories.add(new Category(getString(R.string.marketing), R.drawable.ic_marketing));
        categories.add(new Category(getString(R.string.food), R.drawable.ic_resturant));
        categories.add(new Category(getString(R.string.law), R.drawable.ic_law));
        categories.add(new Category(getString(R.string.programming), R.drawable.ic_programming));
        categories.add(new Category(getString(R.string.health), R.drawable.ic_health));

        binding.rvCategories.setAdapter(new CategoriesAdapter(categories));

        binding.ivFilter.setOnClickListener(v -> {
            SearchDialogBinding dialogBinding = SearchDialogBinding.inflate(LayoutInflater.from(binding.getRoot().getContext()), null, false);
            Dialog dialog = new Dialog(binding.getRoot().getContext());
            dialog.setContentView(dialogBinding.getRoot());

            String type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);
            String text = Objects.requireNonNull(binding.edtSearch.getText()).toString().trim();

            dialogBinding.tvCity.setOnClickListener(v13 -> {
                if (!text.isEmpty()) {
                    if (Objects.equals(type, "JOB_SEEKER")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("city", text);
                        bundle.putString("category", "jobs");
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("city", text);
                        bundle.putString("category", "users");
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                    }
                } else {
                    binding.edtSearch.setError(getString(R.string.edt_alert));
                }
                dialog.dismiss();
            });

            dialogBinding.tvJobs.setOnClickListener(v12 -> {
                if (!text.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    bundle.putString("category", "jobs");
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                } else {
                    binding.edtSearch.setError(getString(R.string.edt_alert));
                }
                dialog.dismiss();
            });

            dialogBinding.tvOrg.setOnClickListener(v1 -> {
                if (!text.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    bundle.putString("category", "org");
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                } else {
                    binding.edtSearch.setError(getString(R.string.edt_alert));
                }
                dialog.dismiss();
            });

            dialog.show();
        });
    }
}