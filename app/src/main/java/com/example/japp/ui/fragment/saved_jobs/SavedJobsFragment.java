package com.example.japp.ui.fragment.saved_jobs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.adapter.SkillsAdapter;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.FragmentSavedJobsBinding;
import com.example.japp.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class SavedJobsFragment extends Fragment {

    private FragmentSavedJobsBinding binding;
    private DatabaseReference mDatabase;
    private String type = "Full time";

    public SavedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SavedViewModel viewModel = new ViewModelProvider(this).get(SavedViewModel.class);

        Job jobData = null;
        if (getArguments() != null) {
            jobData = (Job) getArguments().get("data");
        }

        SkillsAdapter requirementsAdapter = new SkillsAdapter(new ArrayList<>());

        if (Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.type), "JOB_SEEKER")) {
            binding.rvJobs.setVisibility(View.VISIBLE);
            binding.llOrg.setVisibility(View.GONE);
            viewModel.getSavedJobs(getContext());
        } else {
            binding.rvJobs.setVisibility(View.GONE);
            binding.llOrg.setVisibility(View.VISIBLE);

            binding.rvRequirements.setAdapter(requirementsAdapter);

            binding.btnAdd.setOnClickListener(v -> addingItem(requirementsAdapter));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    type = "Full time";
                } else {
                    type = "Part time";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (jobData != null) {
            binding.edtTitle.setText(jobData.getTitle());
            binding.edtDescription.setText(jobData.getDescription());
            binding.edtCategory.setText(jobData.getCategory());
            binding.edtSpecialization.setText(jobData.getSpecialization());
            binding.edtLocation.setText(jobData.getLocation());
            binding.edtExperience.setText(jobData.getExperience());
            requirementsAdapter.setList((ArrayList<String>) jobData.getRequirements());
            binding.btnSave.setText("Edit");
            if (Objects.equals(jobData.getType(), "Full part")) {
                binding.spinnerType.setSelection(0);
            } else {
                binding.spinnerType.setSelection(1);
            }
        }


        Job finalJobData = jobData;
        binding.btnSave.setOnClickListener(v -> {
            if (!isValidForm())
                return;

            ProgressDialog loading = new ProgressDialog(binding.getRoot().getContext());
            loading.setTitle("loading");
            loading.setMessage("Wait while loading...");
            loading.setCancelable(false);
            loading.show();

            String id = null;

            if (finalJobData != null) {
                id = String.valueOf(finalJobData.getId());
            }


            String title = Objects.requireNonNull(binding.edtTitle.getText()).toString();
            String description = Objects.requireNonNull(binding.edtDescription.getText()).toString();
            String category = Objects.requireNonNull(binding.edtCategory.getText()).toString();
            String location = Objects.requireNonNull(binding.edtLocation.getText()).toString();
            String experience = Objects.requireNonNull(binding.edtExperience.getText()).toString();
            String specialization = Objects.requireNonNull(binding.edtSpecialization.getText()).toString();

            String finalId = id;
            mDatabase.child("jobs").get().addOnSuccessListener(dataSnapshot -> {
                Job data = new Job((int) dataSnapshot.getChildrenCount(), title, description, requirementsAdapter.getList(), new SharedHelper().getString(getContext(), SharedHelper.name), new SharedHelper().getString(getContext(), SharedHelper.photo), category, type, location, experience, "reject", new SharedHelper().getString(getContext(), SharedHelper.uid), specialization);
                if (finalId != null)
                    mDatabase.child("jobs").child(finalId).setValue(data);
                else
                    mDatabase.child("jobs").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(data);

                loading.dismiss();
                binding.edtTitle.setText("");
                binding.edtDescription.setText("");
                binding.edtCategory.setText("");
                binding.edtLocation.setText("");
                binding.edtExperience.setText("");
                binding.edtSpecialization.setText("");
                requirementsAdapter.setList(new ArrayList<>());

                Toast.makeText(binding.getRoot().getContext(), getString(R.string.save_data), Toast.LENGTH_SHORT).show();
            });
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                binding.rvJobs.setAdapter(new JobsAdapter(list, "SAVED"));
            }
        });
    }

    private boolean isValidForm() {
        boolean valid = true;

        String title = Objects.requireNonNull(binding.edtTitle.getText()).toString();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString();
        String category = Objects.requireNonNull(binding.edtCategory.getText()).toString();
        String location = Objects.requireNonNull(binding.edtLocation.getText()).toString();
        String experience = Objects.requireNonNull(binding.edtExperience.getText()).toString();
        String specialization = Objects.requireNonNull(binding.edtSpecialization.getText()).toString();

        if (TextUtils.isEmpty(title)) {
            binding.edtTitle.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtTitle.setError(null);
        }

        if (TextUtils.isEmpty(description)) {
            binding.edtDescription.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtDescription.setError(null);
        }

        if (TextUtils.isEmpty(category)) {
            binding.edtCategory.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtCategory.setError(null);
        }

        if (TextUtils.isEmpty(location)) {
            binding.edtLocation.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtLocation.setError(null);
        }

        if (TextUtils.isEmpty(experience)) {
            binding.edtExperience.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtExperience.setError(null);
        }

        if (TextUtils.isEmpty(specialization)) {
            binding.edtSpecialization.setError(getString(R.string.edt_alert));
            valid = false;
        } else {
            binding.edtSpecialization.setError(null);
        }

        return valid;
    }

    void addingItem(SkillsAdapter adapter) {
        AddingItemLayoutBinding dialogBinding = AddingItemLayoutBinding.inflate(LayoutInflater.from(getContext()), null, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.tvSave.setOnClickListener(v -> {
            if (dialogBinding.edtItem.getText() != null) {
                adapter.addingItem(dialogBinding.edtItem.getText().toString());
                dialog.dismiss();
            } else
                Toast.makeText(getContext(), getString(R.string.edt_alert), Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }
}