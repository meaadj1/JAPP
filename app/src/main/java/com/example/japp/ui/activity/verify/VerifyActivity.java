package com.example.japp.ui.activity.verify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.databinding.ActivityVerifyBinding;
import com.example.japp.ui.activity.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class VerifyActivity extends AppCompatActivity {

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyBinding binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        type = getIntent().getStringExtra("type");
        String email = getIntent().getStringExtra("email");
        binding.tvEmail.setText(getString(R.string.we_have_sent_the_verification_code_to_email_address) + email);

        binding.btnEmail.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(binding.getRoot().getContext(), getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSignIn.setOnClickListener(v -> startActivity(new Intent(binding.getRoot().getContext(), LoginActivity.class)));

        binding.tvResend.setOnClickListener(v -> {
            if (Objects.equals(type, "password")) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(unused -> Toast.makeText(binding.getRoot().getContext(), getString(R.string.we_have_sent_the_verification_code_to_email_address), Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                user.sendEmailVerification().addOnSuccessListener(unused -> Toast.makeText(binding.getRoot().getContext(), getString(R.string.we_have_sent_the_verification_code_to_email_address), Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}