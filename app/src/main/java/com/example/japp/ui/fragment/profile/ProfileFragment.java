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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.adapter.SkillsAdapter;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.FragmentProfileBinding;
import com.example.japp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 5;
    private FragmentProfileBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    Context context;

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

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.createNavigateOnClickListener(R.id.nav_settings).onClick(v);
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        binding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.edtDate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String json = new SharedHelper().getString(getContext(), SharedHelper.user);
        User user = new Gson().fromJson(json, User.class);

        Picasso.get().load(new SharedHelper().getString(context, SharedHelper.photo)).into(binding.ivProfile);

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
                if (filePath != null) {
                    StorageReference ref = storageReference.child("images/" + new SharedHelper().getString(requireContext(), SharedHelper.uid));
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mDatabase.child("users").child(new SharedHelper().getString(getContext(), SharedHelper.uid)).child("photo").setValue(uri.toString());
                                    new SharedHelper().saveString(context, SharedHelper.photo, uri.toString());
                                }
                            });
                        }
                    });
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