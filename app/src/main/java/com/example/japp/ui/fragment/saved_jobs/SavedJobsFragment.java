package com.example.japp.ui.fragment.saved_jobs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.AddingRequirementsAdapter;
import com.example.japp.adapter.JobsAdapter;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.FragmentSavedJobsBinding;
import com.example.japp.model.Job;
import com.example.japp.model.Requirement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class SavedJobsFragment extends Fragment {
    private FragmentSavedJobsBinding binding;
    private DatabaseReference mDatabase;
    private String type = "Full time";
    private String category = "Education";
    private int counter = 0;

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


        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        Job jobData = null;
        if (getArguments() != null) {
            jobData = (Job) getArguments().get("data");
        }

        AddingRequirementsAdapter requirementsAdapter = new AddingRequirementsAdapter(new ArrayList<>());

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

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    category = "Education";
                } else if (position == 1) {
                    category = "Engineering";
                } else if (position == 2) {
                    category = "Finance";
                } else if (position == 3) {
                    category = "Translation";
                } else if (position == 4) {
                    category = "Marketing";
                } else if (position == 5) {
                    category = "Food";
                } else if (position == 6) {
                    category = "Law";
                } else if (position == 7) {
                    category = "Technology";
                } else {
                    category = "Health";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (jobData != null) {
            binding.edtTitle.setEnabled(false);
            binding.edtDescription.setEnabled(false);
            binding.edtSpecialization.setEnabled(false);
            binding.edtLocation.setEnabled(false);
            binding.edtExperience.setEnabled(false);
            binding.spinnerType.setEnabled(false);
            binding.spinnerCategory.setEnabled(false);
            binding.btnAdd.setEnabled(false);
            binding.edtTitle.setText(jobData.getTitle());
            binding.edtDescription.setText(jobData.getDescription());
            binding.edtSpecialization.setText(jobData.getSpecialization());
            binding.edtLocation.setText(jobData.getLocation());
            binding.edtExperience.setText(jobData.getExperience());
            requirementsAdapter.setList((ArrayList<Requirement>) jobData.getRequirements());
            binding.btnSave.setText(getString(R.string.edit));
            if (Objects.equals(jobData.getType(), "Full part"))
                binding.spinnerType.setSelection(0);
            else
                binding.spinnerType.setSelection(1);

            category = jobData.getCategory();
            if (Objects.equals(jobData.getCategory(), "Education"))
                binding.spinnerCategory.setSelection(0);
            else if (Objects.equals(jobData.getCategory(), "Engineering"))
                binding.spinnerCategory.setSelection(1);
            else if (Objects.equals(jobData.getCategory(), "Finance"))
                binding.spinnerCategory.setSelection(2);
            else if (Objects.equals(jobData.getCategory(), "Translation"))
                binding.spinnerCategory.setSelection(3);
            else if (Objects.equals(jobData.getCategory(), "Marketing"))
                binding.spinnerCategory.setSelection(4);
            else if (Objects.equals(jobData.getCategory(), "Food"))
                binding.spinnerCategory.setSelection(5);
            else if (Objects.equals(jobData.getCategory(), "Law"))
                binding.spinnerCategory.setSelection(6);
            else if (Objects.equals(jobData.getCategory(), "Technology"))
                binding.spinnerCategory.setSelection(7);
            else
                binding.spinnerCategory.setSelection(8);
        }

        Job finalJobData = jobData;
        binding.btnSave.setOnClickListener(v -> {
            String id = "";

            if (finalJobData != null) {
                if (counter >= 1) {
                    if (!isValidForm(requirementsAdapter)) {
                        counter++;
                        return;
                    }
                    counter = 0;
                    id = String.valueOf(finalJobData.getId());
                } else {
                    counter++;
                    binding.edtTitle.setEnabled(true);
                    binding.edtDescription.setEnabled(true);
                    binding.edtSpecialization.setEnabled(true);
                    binding.edtLocation.setEnabled(true);
                    binding.edtExperience.setEnabled(true);
                    binding.spinnerType.setEnabled(true);
                    binding.spinnerCategory.setEnabled(true);
                    binding.btnAdd.setEnabled(true);
                    requirementsAdapter.setEdit(false);
                    binding.btnSave.setText(getString(R.string.save));
                    return;
                }
            }

            if (!isValidForm(requirementsAdapter))
                return;

            ProgressDialog loading = new ProgressDialog(binding.getRoot().getContext());
            loading.setTitle("loading");
            loading.setMessage("Wait while loading...");
            loading.setCancelable(false);
            loading.show();

            String title = Objects.requireNonNull(binding.edtTitle.getText()).toString();
            String description = Objects.requireNonNull(binding.edtDescription.getText()).toString();
            String location = Objects.requireNonNull(binding.edtLocation.getText()).toString();
            String experience = Objects.requireNonNull(binding.edtExperience.getText()).toString();
            String specialization = Objects.requireNonNull(binding.edtSpecialization.getText()).toString();

            String finalId = id;
            mDatabase.child("jobs").get().addOnSuccessListener(dataSnapshot -> {
                if (!finalId.equals("")) {
                    mDatabase.child("jobs").child(finalId).setValue(new Job(Integer.parseInt(finalId), title, description, requirementsAdapter.getList(), new SharedHelper().getString(getContext(), SharedHelper.firstName), new SharedHelper().getString(getContext(), SharedHelper.photo), category, type, location, experience, "reject", new SharedHelper().getString(getContext(), SharedHelper.uid), specialization));
                } else {
                    mDatabase.child("jobs").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(new Job((int) dataSnapshot.getChildrenCount(), title, description, requirementsAdapter.getList(), new SharedHelper().getString(getContext(), SharedHelper.firstName), new SharedHelper().getString(getContext(), SharedHelper.photo), category, type, location, experience, "reject", new SharedHelper().getString(getContext(), SharedHelper.uid), specialization));
                }

                loading.dismiss();
                binding.edtTitle.setText("");
                binding.edtDescription.setText("");
                binding.edtLocation.setText("");
                binding.edtExperience.setText("");
                binding.edtSpecialization.setText("");
                requirementsAdapter.setList(new ArrayList<>());

                Toast.makeText(binding.getRoot().getContext(), getString(R.string.save_data), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.pendingJobsFragment);
            });
        });

        viewModel.jobs.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                binding.rvJobs.setAdapter(new JobsAdapter(list, "SAVED"));
            }
        });
    }

    private boolean isValidForm(AddingRequirementsAdapter adapter) {
        boolean valid = true;

        String title = Objects.requireNonNull(binding.edtTitle.getText()).toString();
        String description = Objects.requireNonNull(binding.edtDescription.getText()).toString();
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

        if (adapter.getListCount() > 100) {
            valid = false;
            Toast.makeText(binding.getRoot().getContext(), "requirements value more than 100%", Toast.LENGTH_SHORT).show();
        } else if (adapter.getListCount() == 0) {
            valid = false;
            Toast.makeText(binding.getRoot().getContext(), "you should add requirements", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    void addingItem(AddingRequirementsAdapter adapter) {
        AddingItemLayoutBinding dialogBinding = AddingItemLayoutBinding.inflate(LayoutInflater.from(getContext()), null, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(getString(R.string.adding_requirement));
        dialogBinding.llValue.setVisibility(View.VISIBLE);
        dialogBinding.tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.tvSave.setOnClickListener(v -> {
            if (dialogBinding.edtItem.getText() != null) {
                adapter.addingItem(new Requirement(dialogBinding.edtItem.getText().toString(), Integer.parseInt(Objects.requireNonNull(dialogBinding.edtValue.getText()).toString())));
                dialog.dismiss();
            } else
                Toast.makeText(getContext(), getString(R.string.edt_alert), Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }
}