package com.example.japp.ui.fragment.job_details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.FragmentJobDetailsBinding;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.function.Consumer;

public class JobDetailsFragment extends Fragment {
    private FragmentJobDetailsBinding binding;
    private DatabaseReference mDatabase;

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Job data = (Job) requireArguments().get("data");
        User userData = (User) requireArguments().get("user");
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            binding.tvRequirements.setText(data.getRequirements().toString());
            binding.tvPosition.setText(data.getCategory());
            binding.tvExperience.setText(data.getExperience());
            binding.tvType.setText(data.getType());
            binding.tvSpecialization.setText(data.getSpecialization());

            binding.btnApply.setOnClickListener(v -> mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("jobs").get().addOnSuccessListener(dataSnapshot -> {
                data.setStatus("pending");
                mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("jobs").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(data);
                mDatabase.child("users").child(data.getCompanyUid()).child("applicants").get().addOnSuccessListener(dataSnapshot1 -> {
                    User user = new Gson().fromJson(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user), User.class);
                    user.setJobId(data.getId());
                    float validate = 0;
                    for (int i = 0; i < data.getRequirements().size(); i++) {
                        if (user.getSkills() != null) {
                            for (int j = 0; j < user.getSkills().size(); j++) {
                                if (Objects.equals(data.getRequirements().get(i), user.getSkills().get(j)))
                                    validate++;
                            }
                        }
                    }
                    validate = (validate / data.getRequirements().size()) * 100;
                    user.setMatching(validate);
                    mDatabase.child("users").child(data.getCompanyUid()).child("applicants").child(String.valueOf(dataSnapshot1.getChildrenCount())).setValue(user);
                });
                Toast.makeText(getContext(), "Officially You applied on this job", Toast.LENGTH_SHORT).show();
            }));

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

            binding.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    assert user != null;
                                    if (Objects.equals(user.getEmail(), userData.getEmail())) {
                                        dataSnapshot.child("jobs").child(String.valueOf(userData.getJobId())).child("status").getRef().setValue("accept");
                                        Toast.makeText(binding.getRoot().getContext(), "Done your accept this applicant", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(binding.getRoot()).navigateUp();
                                    }
                                }
                            });
                        }
                    });
                }
            });

            binding.tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    assert user != null;
                                    if (Objects.equals(user.getEmail(), userData.getEmail())) {
                                        dataSnapshot.child("jobs").child(String.valueOf(userData.getJobId())).child("status").getRef().setValue("reject");
                                        Toast.makeText(binding.getRoot().getContext(), "Done your accept this applicant", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(binding.getRoot()).navigateUp();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
    }
}