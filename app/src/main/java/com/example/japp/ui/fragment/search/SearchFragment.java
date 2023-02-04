package com.example.japp.ui.fragment.search;

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
import com.example.japp.databinding.SearchSheetBinding;
import com.example.japp.model.Category;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private String filter = "city";

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

        binding.ivFilter.setOnClickListener(v -> showFilterBottomSheet());

        binding.ivEnter.setOnClickListener(v -> {
            String type = new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type);
            String text = Objects.requireNonNull(binding.edtSearch.getText()).toString().trim();

            if (Objects.equals(filter, "city")) {
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
            } else if (Objects.equals(filter, "jobs")) {
                if (!text.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    bundle.putString("category", "jobs");
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                } else {
                    binding.edtSearch.setError(getString(R.string.edt_alert));
                }
            } else {
                if (!text.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    bundle.putString("category", "org");
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.resultFragment, bundle);
                } else {
                    binding.edtSearch.setError(getString(R.string.edt_alert));
                }
            }
        });
    }

    private void showFilterBottomSheet() {
        SearchSheetBinding sheetBinding = SearchSheetBinding.inflate(LayoutInflater.from(binding.getRoot().getContext()));
        BottomSheetDialog dialog = new BottomSheetDialog(sheetBinding.getRoot().getContext());
        dialog.setContentView(sheetBinding.getRoot());


        sheetBinding.tvCity.setOnClickListener(v -> {
            filter = "city";
            dialog.dismiss();
        });

        sheetBinding.tvJobs.setOnClickListener(v -> {
            filter = "jobs";
            dialog.dismiss();
        });

        sheetBinding.tvOrg.setOnClickListener(v -> {
            filter = "org";
            dialog.dismiss();
        });

        dialog.show();
    }
}