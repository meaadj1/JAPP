package com.muhmmad.japp.ui.fragment.job_details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhmmad.japp.R;
import com.muhmmad.japp.Utils.SharedHelper;
import com.muhmmad.japp.databinding.FragmentJobDetailsBinding;
import com.muhmmad.japp.model.Job;
import com.squareup.picasso.Picasso;

public class JobDetailsFragment extends Fragment {

    private static final String TAG = "JobDetailsFragment";

    private FragmentJobDetailsBinding binding;
    private DatabaseReference mDatabase;

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Job data = (Job) requireArguments().get("data");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {
            Picasso.get().load(data.getCompanyImage()).placeholder(R.drawable.place_holder).into(binding.ivCompany);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.tvTitle.setText(data.getTitle());
        binding.tvCompany.setText(data.getCompanyName());
        binding.tvDescription.setText(data.getDescription());
        binding.tvRequirements.setText(data.getRequirements().toString());
        binding.tvPosition.setText(data.getCategory());
        binding.tvQualification.setText(data.getQualification());
        binding.tvExperience.setText(data.getExperience());
        binding.tvType.setText(data.getType());
        binding.tvSpecialization.setText(data.getSpecialization());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("jobs").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        data.setStatus("pending");
                        mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("jobs").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(data);
                        Toast.makeText(getContext(), "Officially You applied on this job", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}