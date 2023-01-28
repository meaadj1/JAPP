package com.example.japp.ui.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.ActivityLoginBinding;
import com.example.japp.model.User;
import com.example.japp.ui.activity.home.HomeActivity;
import com.example.japp.ui.activity.register.RegisterActivity;
import com.example.japp.ui.activity.verify.VerifyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = binding.getRoot().getContext();

        LocaleHelper.setLocale(context, LocaleHelper.getLanguage(context));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.tvSignUp.setOnClickListener(view -> startActivity(new Intent(context, RegisterActivity.class)));

        binding.btnSignIn.setOnClickListener(v -> login());

        binding.tvForgetPassword.setOnClickListener(v -> resetPassword());
    }

    private void login() {
        if (!isValidForm())
            return;
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                new SharedHelper().saveString(context, SharedHelper.uid, user.getUid());
                                mDatabase.child("users").child(user.getUid()).get().addOnSuccessListener(dataSnapshot -> {
                                    User user1 = dataSnapshot.getValue(User.class);
                                    if (user1 != null) {
                                        Gson gson = new Gson();
                                        String json = gson.toJson(user1);
                                        new SharedHelper().saveString(context, SharedHelper.user, json);
                                        new SharedHelper().saveString(context, SharedHelper.firstName, user1.getFirstName());
                                        new SharedHelper().saveString(context, SharedHelper.lastName, user1.getLastName());
                                        new SharedHelper().saveString(context, SharedHelper.type, user1.getType());
                                        new SharedHelper().saveString(context, SharedHelper.email, user1.getEmail());
                                        new SharedHelper().saveString(context, SharedHelper.phone, user1.getPhone());
                                    }
                                    startActivity(new Intent(context, HomeActivity.class));
                                });
                            } else {
                                verify(user);
                            }
                        }
                    } else {
                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidForm() {
        boolean valid = true;
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString().trim();
        if (TextUtils.isEmpty(email)) {
            binding.inputEmail.setError(getString(R.string.email_alert_1));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getString(R.string.email_alert_2));
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.inputPassword.setError(getString(R.string.pass_alert));
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }
        return valid;
    }

    private void verify(FirebaseUser user) {
        user.sendEmailVerification().addOnSuccessListener(unused -> startActivity(new Intent(binding.getRoot().getContext(), VerifyActivity.class))).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void resetPassword() {
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.inputEmail.setError(getString(R.string.email_alert_1));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getString(R.string.email_alert_2));
        } else {
            binding.inputEmail.setError(null);
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                Toast.makeText(binding.getRoot().getContext(), getString(R.string.we_have_sent_the_verification_code_to_email_address), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(binding.getRoot().getContext(), VerifyActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("type", "password");
                startActivity(intent);
            }).addOnFailureListener(e -> {
                Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}