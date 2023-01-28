package com.example.japp.ui.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.databinding.ActivityRegisterBinding;
import com.example.japp.model.User;
import com.example.japp.ui.activity.login.LoginActivity;
import com.example.japp.ui.activity.verify.VerifyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String USER_TYPE;
    private final String SEEKER = "JOB_SEEKER";
    private final String ORGANIZATION = "ORGANIZATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.btnSignUp.setOnClickListener(v -> register());

        binding.tvSignIn.setOnClickListener(v -> startActivity(new Intent(binding.getRoot().getContext(), LoginActivity.class)));

        binding.cbSeeker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                USER_TYPE = SEEKER;
                binding.cbOrganization.setChecked(false);
            }
        });

        binding.cbOrganization.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                USER_TYPE = ORGANIZATION;
                binding.cbSeeker.setChecked(false);
            }
        });
    }

    private void register() {
        if (!isValidForm()) return;
        String firstName = Objects.requireNonNull(binding.edtFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(binding.edtLastName.getText()).toString().trim();
        String phone = Objects.requireNonNull(binding.edtPhone.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                User userData = new User(firstName, lastName, email, phone, USER_TYPE);
                assert user != null;
                {
                    mDatabase.child("users").child(user.getUid()).setValue(userData);
                    user.sendEmailVerification().addOnSuccessListener(unused -> {
                        Intent intent = new Intent(binding.getRoot().getContext(), VerifyActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(binding.getRoot().getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidForm() {
        boolean valid = true;
        String firstName = Objects.requireNonNull(binding.edtFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(binding.edtLastName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String phone = Objects.requireNonNull(binding.edtPhone.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            binding.inputFirstName.setError(getString(R.string.name_alert));
            valid = false;
        } else {
            binding.inputFirstName.setError(null);
        }

        if (TextUtils.isEmpty(lastName)) {
            binding.inputLastName.setError(getString(R.string.name_alert));
            valid = false;
        } else {
            binding.inputLastName.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            binding.inputEmail.setError(getString(R.string.email_alert_1));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getString(R.string.email_alert_2));
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            binding.inputPhone.setError(getString(R.string.phone_alert));
            valid = false;
        } else if (!checkPhone(phone)) {
            binding.inputPhone.setError(getString(R.string.phone_alert2));
            valid = false;
        } else {
            binding.inputPhone.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.inputPassword.setError(getString(R.string.pass_alert));
            valid = false;
        } else if (!isContainCapital(password)) {
            binding.inputPassword.setError(getString(R.string.pass_alert2));
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        if (!binding.cbSeeker.isChecked() && !binding.cbOrganization.isChecked()) {
            Toast.makeText(binding.getRoot().getContext(), getString(R.string.checkbox_alert), Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private boolean isContainCapital(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPhone(String phone) {
        if (phone.length() >= 10)
            return false;
        else if (!Patterns.PHONE.matcher(phone).matches())
            return false;

        return true;
    }
}