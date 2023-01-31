package com.example.japp.ui.fragment.job_details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.RequirementsAdapter;
import com.example.japp.databinding.FragmentJobDetailsBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.function.Consumer;

public class JobDetailsFragment extends Fragment {
    private FragmentJobDetailsBinding binding;
    JobDetailsViewModel viewModel;
    Context context;
    String uid;
    User user;

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(JobDetailsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = binding.getRoot().getContext();
        uid = new SharedHelper().getString(context, SharedHelper.uid);
        Job data = (Job) requireArguments().get("data");
        User userData = (User) requireArguments().get("user");
        user = new Gson().fromJson(new SharedHelper().getString(context, SharedHelper.user), User.class);
        RequirementsAdapter requirementsAdapter = null;

        if (data != null) {
            binding.llJob.setVisibility(View.VISIBLE);
            binding.llUser.setVisibility(View.GONE);

            try {
                Glide.with(binding.getRoot()).load(data.getCompanyImage()).placeholder(R.drawable.place_holder).into(binding.ivCompany);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            binding.tvTitle.setText(data.getTitle());
            binding.tvCompany.setText(data.getCompanyName());
            binding.tvDescription.setText(data.getDescription());
            if (data.getRequirements() != null) {
                requirementsAdapter = new RequirementsAdapter(data.getRequirements());
                binding.rvRequirements.setAdapter(requirementsAdapter);
            }
            binding.tvPosition.setText(data.getCategory());
            binding.tvExperience.setText(data.getExperience());
            binding.tvType.setText(data.getType());
            binding.tvSpecialization.setText(data.getSpecialization());

            if (Objects.equals(uid, data.getCompanyUid()))
                binding.btnApply.setVisibility(View.GONE);

            RequirementsAdapter finalRequirementsAdapter = requirementsAdapter;
            binding.btnApply.setOnClickListener(v -> {
                if (finalRequirementsAdapter != null)
                    viewModel.applyJob(context, uid, data, user, finalRequirementsAdapter.getCheckedList());
                else
                    viewModel.applyJob(context, uid, data, user, null);
            });

            binding.tvCompany.setOnClickListener(v -> viewModel.getUserData(context, data.getCompanyUid()));
        } else {
            binding.llJob.setVisibility(View.GONE);
            binding.llUser.setVisibility(View.VISIBLE);

            try {
                Glide.with(binding.getRoot()).load(userData.getPhoto()).placeholder(R.drawable.place_holder).into(binding.ivCompany);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            binding.tvTitle.setText(userData.getFirstName());
            binding.tvCompany.setText(userData.getCountry());
            binding.tvCity.setText(userData.getCity());
            binding.tvPhone.setText(userData.getPhone());
            if (userData.getSkills() != null)
                binding.tvSkills.setText(userData.getSkills().toString());
            if (userData.getEducation() != null)
                binding.tvEdu.setText(userData.getEducation().toString());
            if (userData.getLanguages() != null)
                binding.tvLang.setText(userData.getLanguages().toString());

            if (userData.getCv() == null || Objects.equals(userData.getCv(), "")) {
                binding.tvCv.setVisibility(View.GONE);
                binding.btnShowCv.setVisibility(View.GONE);
            } else {
                binding.tvCv.setVisibility(View.VISIBLE);
                binding.btnShowCv.setVisibility(View.VISIBLE);
            }

            if (userData.getJobId() == -1) {
                binding.btnShowCv.setVisibility(View.GONE);
                binding.tvAccept.setVisibility(View.GONE);
                binding.tvReject.setVisibility(View.GONE);
            }

            binding.btnShowCv.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(userData.getCv()))));

            binding.tvAccept.setOnClickListener(v -> viewModel.acceptUser(context, userData.getEmail(), userData.getJobId(), uid));

            binding.tvReject.setOnClickListener(v -> viewModel.rejectUser(context, userData.getEmail(), userData.getJobId(), uid));
        }

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        viewModel.isDone.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        });

        viewModel.userData.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                user.setJobId(-1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_job_details, bundle);
            }
        });

//        viewModel.isApplied.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(!aBoolean){
//
//                }
//            }
//        });
    }
}