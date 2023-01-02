package com.example.japp.ui.fragment.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.japp.adapter.SkillsAdapter;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.FragmentProfileBinding;
import com.example.japp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private static final int RESULT_LOAD_IMG = 5;
    private FragmentProfileBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    Context context;
    private String companySize = firstCompanySize;
    static String firstCompanySize = "1-5";
    static String secondCompanySize = "6-24";
    static String thirdCompanySize = "25-49";
    static String fourthCompanySize = "50-149";
    static String fifthCompanySize = "+200";

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

        context = binding.edtDate.getContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.ivSetting.setOnClickListener(v -> Navigation.createNavigateOnClickListener(R.id.nav_settings).onClick(v));

        binding.btnEdit.setOnClickListener(v -> getImage());

        if (Objects.equals(new SharedHelper().getString(context, SharedHelper.type), "JOB_SEEKER")) {
            binding.llSeeker.setVisibility(View.VISIBLE);
            binding.llOrg.setVisibility(View.GONE);
        } else {
            binding.llSeeker.setVisibility(View.GONE);
            binding.llOrg.setVisibility(View.VISIBLE);
        }

        binding.edtDate.setOnClickListener(v -> new DatePickerDialog(context, (view1, year, month, dayOfMonth) -> binding.edtDate.setText(dayOfMonth + "-" + month + "-" + year), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show());

        String json = new SharedHelper().getString(getContext(), SharedHelper.user);

        User user = new Gson().fromJson(json, User.class);

        if (!(new SharedHelper().getString(context, SharedHelper.photo)).isEmpty())
            Glide.with(context).load(new SharedHelper().getString(context, SharedHelper.photo)).into(binding.ivProfile);

        binding.edtName.setText(user.getName());
        binding.edtOrgName.setText(user.getName());
        binding.edtOrgLocation.setText(user.getCountry());
        binding.edtOrgCity.setText(user.getCity());
        binding.edtOrgPhone.setText(user.getPhone());
        binding.edtDescription.setText(user.getDescription());
        binding.edtGender.setText(user.getGender());
        binding.edtDate.setText(user.getDateOfBirth());
        binding.edtCountry.setText(user.getCountry());
        binding.edtCity.setText(user.getCity());
        binding.edtNationality.setText(user.getNationality());

        binding.btn1.setOnClickListener(v -> companySize = firstCompanySize);

        binding.btn2.setOnClickListener(v -> companySize = secondCompanySize);

        binding.btn3.setOnClickListener(v -> companySize = thirdCompanySize);

        binding.btn4.setOnClickListener(v -> companySize = fourthCompanySize);

        binding.btn5.setOnClickListener(v -> companySize = fifthCompanySize);
        SkillsAdapter skillsAdapter = new SkillsAdapter(user.getSkills());
        binding.rvSkills.setAdapter(skillsAdapter);
        binding.btnAddSkill.setOnClickListener(v -> addingItem(skillsAdapter));

        SkillsAdapter eduAdapter;
        if (user.getEducation() == null)
            eduAdapter = new SkillsAdapter(new ArrayList<>());
        else
            eduAdapter = new SkillsAdapter(user.getEducation());

        binding.rvEdu.setAdapter(eduAdapter);
        binding.btnAddEdu.setOnClickListener(v -> addingItem(eduAdapter));
        SkillsAdapter langAdapter;
        if (user.getLanguages() == null)
            langAdapter = new SkillsAdapter(new ArrayList<>());
        else
            langAdapter = new SkillsAdapter(user.getLanguages());

        binding.rvLang.setAdapter(langAdapter);
        binding.btnAddLang.setOnClickListener(v -> addingItem(langAdapter));

        binding.btnSave.setOnClickListener(v -> {
            if (filePath != null) {
                StorageReference ref = storageReference.child("images/" + new SharedHelper().getString(requireContext(), SharedHelper.uid));
                ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("photo").setValue(uri.toString());
                    new SharedHelper().saveString(context, SharedHelper.photo, uri.toString());
                }));
            }

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
        });

        binding.btnOrgSave.setOnClickListener(v -> {
            if (filePath != null) {
                StorageReference ref = storageReference.child("images/" + new SharedHelper().getString(requireContext(), SharedHelper.uid));
                ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("photo").setValue(uri.toString());
                    new SharedHelper().saveString(context, SharedHelper.photo, uri.toString());
                }));
            }

            User data = new User(Objects.requireNonNull(binding.edtOrgName.getText()).toString(), user.getEmail(), user.getPhone(), user.getType(), Objects.requireNonNull(binding.edtOrgLocation.getText()).toString(), Objects.requireNonNull(binding.edtOrgCity.getText()).toString(), companySize, Objects.requireNonNull(binding.edtDescription.getText()).toString());

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
        });
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
                Toast.makeText(getContext(), "please Enter the text", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG) {
            try {
                if (data != null) {
                    filePath = data.getData();
                    binding.ivProfile.setImageURI(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(binding.getRoot().getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(binding.getRoot().getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();

        }
    }
}