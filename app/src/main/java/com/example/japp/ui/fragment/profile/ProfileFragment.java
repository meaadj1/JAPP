package com.example.japp.ui.fragment.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
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
    private static final int RESULT_LOAD_IMG = 5;
    private static final int RESULT_LOAD_PDF = 1;
    private FragmentProfileBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri imagePath, pdfPath;
    Context context;
    private String companySize = firstCompanySize, uid, photo, pdf;
    static String firstCompanySize = "1-5";
    static String secondCompanySize = "6-24";
    static String thirdCompanySize = "25-49";
    static String fourthCompanySize = "50-149";
    static String fifthCompanySize = "+200";
    private final ArrayList<String> categories = new ArrayList<>();

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

        context = binding.getRoot().getContext();

        LocaleHelper.setLocale(context, LocaleHelper.getLanguage(binding.getRoot().getContext()));


        uid = new SharedHelper().getString(context, SharedHelper.uid);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        photo = new SharedHelper().getString(context, SharedHelper.photo);
        pdf = new SharedHelper().getString(context, SharedHelper.pdf);
        String education = "Education";
        String engineering = "Engineering";
        String finance = "Finance";
        String translation = "Translation";
        String marketing = "Marketing";
        String food = "Food";
        String law = "law";
        String technology = "Technology";
        String health = "Health";

        binding.ivSetting.setOnClickListener(v -> Navigation.createNavigateOnClickListener(R.id.nav_settings).onClick(v));

        binding.btnEdit.setOnClickListener(v -> getImage());

        String json = new SharedHelper().getString(getContext(), SharedHelper.user);
        User user = new Gson().fromJson(json, User.class);

        if (user.getCategories() != null)
            categories.addAll(user.getCategories());

        if (Objects.equals(new SharedHelper().getString(context, SharedHelper.type), "JOB_SEEKER")) {
            binding.llSeeker.setVisibility(View.VISIBLE);
            binding.llOrg.setVisibility(View.GONE);

            if (user.getCv() != null && !Objects.equals(user.getCv(), "")) {
                binding.tvCv.setText(getString(R.string.done));
            }

            if (categories.contains(education))
                binding.tvEdu.setChecked(true);

            if (categories.contains(engineering))
                binding.tvEng.setChecked(true);

            if (categories.contains(finance))
                binding.tvFinance.setChecked(true);

            if (categories.contains(translation))
                binding.tvTranslation.setChecked(true);

            if (categories.contains(marketing))
                binding.tvMarketing.setChecked(true);

            if (categories.contains(food))
                binding.tvFood.setChecked(true);

            if (categories.contains(law))
                binding.tvLaw.setChecked(true);

            if (categories.contains(technology))
                binding.tvTech.setChecked(true);

            if (categories.contains(health))
                binding.tvHealth.setChecked(true);

            binding.tvEdu.setOnClickListener(v -> {
                if (binding.tvEdu.isChecked()) {
                    binding.tvEdu.setChecked(false);
                    categories.remove(education);
                } else {
                    binding.tvEdu.setChecked(true);
                    categories.add(education);
                }
            });

            binding.tvEng.setOnClickListener(v -> {
                if (binding.tvEng.isChecked()) {
                    binding.tvEng.setChecked(false);
                    categories.remove(engineering);
                } else {
                    binding.tvEng.setChecked(true);
                    categories.add(engineering);
                }
            });

            binding.tvFinance.setOnClickListener(v -> {
                if (binding.tvFinance.isChecked()) {
                    binding.tvFinance.setChecked(false);
                    categories.remove(finance);
                } else {
                    binding.tvFinance.setChecked(true);
                    categories.add(finance);
                }
            });

            binding.tvTranslation.setOnClickListener(v -> {
                if (binding.tvTranslation.isChecked()) {
                    binding.tvTranslation.setChecked(false);
                    categories.remove(translation);
                } else {
                    binding.tvTranslation.setChecked(true);
                    categories.add(translation);
                }
            });

            binding.tvMarketing.setOnClickListener(v -> {
                if (binding.tvMarketing.isChecked()) {
                    binding.tvMarketing.setChecked(false);
                    categories.remove(marketing);
                } else {
                    binding.tvMarketing.setChecked(true);
                    categories.add(marketing);
                }
            });

            binding.tvFood.setOnClickListener(v -> {
                if (binding.tvFood.isChecked()) {
                    binding.tvFood.setChecked(false);
                    categories.remove(food);
                } else {
                    binding.tvFood.setChecked(true);
                    categories.add(food);
                }
            });

            binding.tvLaw.setOnClickListener(v -> {
                if (binding.tvLaw.isChecked()) {
                    binding.tvLaw.setChecked(false);
                    categories.remove(law);
                } else {
                    binding.tvLaw.setChecked(true);
                    categories.add(law);
                }
            });

            binding.tvTech.setOnClickListener(v -> {
                if (binding.tvTech.isChecked()) {
                    binding.tvTech.setChecked(false);
                    categories.remove(technology);
                } else {
                    binding.tvTech.setChecked(true);
                    categories.add(technology);
                }
            });

            binding.tvHealth.setOnClickListener(v -> {
                if (binding.tvHealth.isChecked()) {
                    binding.tvHealth.setChecked(false);
                    categories.remove(health);
                } else {
                    binding.tvHealth.setChecked(true);
                    categories.add(health);
                }
            });
        } else {
            binding.llSeeker.setVisibility(View.GONE);
            binding.llOrg.setVisibility(View.VISIBLE);
        }

        binding.edtDate.setOnClickListener(v -> new DatePickerDialog(context, (view1, year, month, dayOfMonth) -> binding.edtDate.setText(dayOfMonth + "-" + month + 1 + "-" + year), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show());

        if (!photo.isEmpty())
            Glide.with(context).load(photo).into(binding.ivProfile);

        binding.edtFirstName.setText(user.getFirstName());
        binding.edtLastName.setText(user.getLastName());
        binding.edtOrgName.setText(user.getFirstName());
        binding.edtOrgLocation.setText(user.getCountry());
        binding.edtOrgCity.setText(user.getCity());
        binding.edtOrgPhone.setText(user.getPhone());
        binding.edtDescription.setText(user.getDescription());
        binding.edtGender.setText(user.getGender());
        binding.edtDate.setText(user.getDateOfBirth());
        binding.edtCountry.setText(user.getCountry());
        binding.edtCity.setText(user.getCity());
        binding.edtNationality.setText(user.getNationality());

        binding.rgSize.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_1)
                companySize = firstCompanySize;
            else if (checkedId == R.id.rb_2)
                companySize = secondCompanySize;
            else if (checkedId == R.id.rb_3)
                companySize = thirdCompanySize;
            else if (checkedId == R.id.rb_4)
                companySize = fourthCompanySize;
            else if (checkedId == R.id.rb_5)
                companySize = fifthCompanySize;
        });

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

        binding.btnUploadCv.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "SELECT PDF FILE"), RESULT_LOAD_PDF);
        });

        binding.btnSave.setOnClickListener(v -> {
            saveImage();

            if (pdfPath != null) {
                storageReference.child("cv/" + uid).putFile(pdfPath).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    mDatabase.child("users").child(uid).child("cv").setValue(uri.toString());
                    new SharedHelper().saveString(context, SharedHelper.pdf, uri.toString());
                }).addOnFailureListener(e -> Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()));
            }

            User data = new User(Objects.requireNonNull(binding.edtFirstName.getText()).toString(), Objects.requireNonNull(binding.edtLastName.getText()).toString(), user.getEmail(), user.getPhone(), Objects.requireNonNull(binding.edtGender.getText()).toString(), user.getType(), Objects.requireNonNull(binding.edtDate.getText()).toString(), Objects.requireNonNull(binding.edtCountry.getText()).toString(), Objects.requireNonNull(binding.edtNationality.getText()).toString(), Objects.requireNonNull(binding.edtCity.getText()).toString(), skillsAdapter.getList(), eduAdapter.getList(), langAdapter.getList(), categories);
            data.setPhoto(photo);
            data.setCv(pdf);
            mDatabase.child("users").child(uid).setValue(data);
            Toast.makeText(context, getString(R.string.save_data), Toast.LENGTH_SHORT).show();

            mDatabase.child("users").child(uid).get().addOnSuccessListener(dataSnapshot -> {
                User user1 = dataSnapshot.getValue(User.class);
                if (user1 != null) {
                    Gson gson = new Gson();
                    String json1 = gson.toJson(user1);
                    new SharedHelper().saveString(getContext(), SharedHelper.user, json1);
                    new SharedHelper().saveString(getContext(), SharedHelper.firstName, user1.getFirstName());
                    new SharedHelper().saveString(getContext(), SharedHelper.lastName, user1.getLastName());
                    new SharedHelper().saveString(getContext(), SharedHelper.type, user1.getType());
                    new SharedHelper().saveString(getContext(), SharedHelper.email, user1.getEmail());
                    new SharedHelper().saveString(getContext(), SharedHelper.phone, user1.getPhone());
                }
            });
        });

        binding.btnOrgSave.setOnClickListener(v -> {
            if (!isValidation())
                return;
            saveImage();

            User data = new User(Objects.requireNonNull(binding.edtOrgName.getText()).toString(), "", user.getEmail(), user.getPhone(), user.getType(), Objects.requireNonNull(binding.edtOrgLocation.getText()).toString(), Objects.requireNonNull(binding.edtOrgCity.getText()).toString(), companySize, Objects.requireNonNull(binding.edtDescription.getText()).toString());

            mDatabase.child("users").child(uid).setValue(data);
            Toast.makeText(getContext(), getString(R.string.save_data), Toast.LENGTH_SHORT).show();

            mDatabase.child("users").child(uid).get().addOnSuccessListener(dataSnapshot -> {
                User user1 = dataSnapshot.getValue(User.class);
                if (user1 != null) {
                    Gson gson = new Gson();
                    String json1 = gson.toJson(user1);
                    new SharedHelper().saveString(getContext(), SharedHelper.user, json1);
                    new SharedHelper().saveString(getContext(), SharedHelper.firstName, user1.getFirstName());
                    new SharedHelper().saveString(getContext(), SharedHelper.lastName, user1.getLastName());
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
                Toast.makeText(getContext(), getString(R.string.edt_alert), Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    private void saveImage() {
        if (imagePath != null) {
            StorageReference ref = storageReference.child("images/" + uid);
            ref.putFile(imagePath).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                mDatabase.child("users").child(uid).child("photo").setValue(uri.toString()).addOnSuccessListener(unused -> new SharedHelper().saveString(context, SharedHelper.photo, uri.toString())).addOnFailureListener(e -> Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show());
            }));
        }
    }

    private boolean isValidation() {
        boolean validation = true;
        String phone = Objects.requireNonNull(binding.edtOrgPhone.getText()).toString().trim();
        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.edtOrgPhone.setError(getString(R.string.phone_alert2));
            validation = false;
        } else if (phone.length() > 10) {
            binding.edtOrgPhone.setError(getString(R.string.phone_alert2));
            validation = false;
        } else if (!phone.startsWith("05")) {
            binding.edtOrgPhone.setError(getString(R.string.phone_alert2));
            validation = false;
        }
        return validation;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG) {
            try {
                if (data != null) {
                    imagePath = data.getData();
                    binding.ivProfile.setImageURI(imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(binding.getRoot().getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULT_LOAD_PDF) {
            try {
                if (data != null) {
                    pdfPath = data.getData();
                    DocumentFile file = DocumentFile.fromSingleUri(binding.getRoot().getContext(), data.getData());
                    assert file != null;
                    binding.tvCv.setText(file.getName());
                }
            } catch (Exception e) {
                Toast.makeText(binding.getRoot().getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(binding.getRoot().getContext(), getString(R.string.picked_file), Toast.LENGTH_LONG).show();
    }
}