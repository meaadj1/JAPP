package com.muhmmad.japp.ui.fragment.profile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.muhmmad.japp.Utils.SharedHelper;
import com.muhmmad.japp.adapter.SkillsAdapter;
import com.muhmmad.japp.databinding.AddingItemLayoutBinding;
import com.muhmmad.japp.databinding.FragmentProfileBinding;
import com.muhmmad.japp.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseReference mDatabase;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String json = new SharedHelper().getString(getContext(), SharedHelper.user);
        User user = new Gson().fromJson(json, User.class);

        binding.edtName.setText(user.getName());
        binding.edtGender.setText(user.getGender());
        binding.edtDate.setText(user.getDateOfBirth());
        binding.edtCountry.setText(user.getCountry());
        binding.edtCity.setText(user.getCity());
        binding.edtNationality.setText(user.getNationality());
        SkillsAdapter skillsAdapter = new SkillsAdapter(user.getSkills());
        binding.rvSkills.setAdapter(skillsAdapter);
        binding.btnAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingItem(skillsAdapter);
            }
        });

        SkillsAdapter eduAdapter;
        if (user.getEducation() == null)
            eduAdapter = new SkillsAdapter(new ArrayList<String>());
        else
            eduAdapter = new SkillsAdapter(user.getEducation());

        binding.rvEdu.setAdapter(eduAdapter);
        binding.btnAddEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingItem(eduAdapter);
            }
        });
        SkillsAdapter langAdapter;
        if (user.getLanguages() == null)
            langAdapter = new SkillsAdapter(new ArrayList<String>());
        else
            langAdapter = new SkillsAdapter(user.getLanguages());

        binding.rvLang.setAdapter(langAdapter);
        binding.btnAddLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingItem(langAdapter);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User data = new User(Objects.requireNonNull(binding.edtName.getText()).toString(), user.getEmail(), user.getPhone(), Objects.requireNonNull(binding.edtGender.getText()).toString(), user.getType(), Objects.requireNonNull(binding.edtDate.getText()).toString(), Objects.requireNonNull(binding.edtCountry.getText()).toString(), Objects.requireNonNull(binding.edtNationality.getText()).toString(), Objects.requireNonNull(binding.edtCity.getText()).toString(), skillsAdapter.getList(), eduAdapter.getList(), langAdapter.getList());
                mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).setValue(data);
                Toast.makeText(getContext(), "Done , succeed you saved the data", Toast.LENGTH_SHORT).show();

                mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).get().addOnSuccessListener(dataSnapshot -> {
                    User user1 = dataSnapshot.getValue(User.class);
                    if (user1 != null) {
                        Gson gson = new Gson();
                        String json1 = gson.toJson(user1);
                        new SharedHelper().saveString(getContext(), SharedHelper.user, json1);
                        new SharedHelper().saveString(getContext(), SharedHelper.name, user1.getName());
                        new SharedHelper().saveString(getContext(), SharedHelper.type, user1.getType());
                        new SharedHelper().saveString(getContext(), SharedHelper.email, user1.getEmail());
                        new SharedHelper().saveString(getContext(), SharedHelper.phone, user1.getPhone());
                    }
                });
            }
        });

    }

    void addingItem(SkillsAdapter adapter) {
        AddingItemLayoutBinding dialogBinding = AddingItemLayoutBinding.inflate(LayoutInflater.from(getContext()), null, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogBinding.edtItem.getText() != null) {
                    adapter.addingItem(dialogBinding.edtItem.getText().toString());
                    dialog.dismiss();
                } else
                    Toast.makeText(getContext(), "please Enter the text", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}