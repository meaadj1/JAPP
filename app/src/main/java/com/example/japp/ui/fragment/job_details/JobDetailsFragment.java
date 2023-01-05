package com.example.japp.ui.fragment.job_details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.FragmentJobDetailsBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.gson.Gson;

import java.util.Objects;

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
            if (data.getRequirements() != null)
                binding.tvRequirements.setText(data.getRequirements().toString());
            binding.tvPosition.setText(data.getCategory());
            binding.tvExperience.setText(data.getExperience());
            binding.tvType.setText(data.getType());
            binding.tvSpecialization.setText(data.getSpecialization());

            if (Objects.equals(uid, data.getCompanyUid()))
                binding.btnApply.setVisibility(View.GONE);

            binding.btnApply.setOnClickListener(v -> viewModel.applyJob(context, uid, data, user));
        } else {
            binding.llJob.setVisibility(View.GONE);
            binding.llUser.setVisibility(View.VISIBLE);

            try {
                Glide.with(binding.getRoot()).load(userData.getPhoto()).placeholder(R.drawable.place_holder).into(binding.ivCompany);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            binding.tvTitle.setText(userData.getName());
            binding.tvCompany.setText(userData.getCountry());
            binding.tvCity.setText(userData.getCity());
            binding.tvPhone.setText(userData.getPhone());
            if (userData.getSkills() != null)
                binding.tvSkills.setText(userData.getSkills().toString());
            if (userData.getEducation() != null)
                binding.tvEdu.setText(userData.getEducation().toString());
            if (userData.getLanguages() != null)
                binding.tvLang.setText(userData.getLanguages().toString());

            binding.btnShowCv.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(userData.getCv()))));

            binding.tvAccept.setOnClickListener(v -> viewModel.acceptUser(context, userData.getEmail(), userData.getJobId()));

            binding.tvReject.setOnClickListener(v -> viewModel.rejectUser(context, userData.getEmail(), userData.getJobId()));
        }

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        viewModel.isDone.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        });
    }
}