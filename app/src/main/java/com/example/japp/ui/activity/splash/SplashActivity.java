package com.example.japp.ui.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.ActivitySplashBinding;
import com.example.japp.ui.activity.home.HomeActivity;
import com.example.japp.ui.activity.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user), "")) {
                    startActivity(new Intent(binding.getRoot().getContext(), HomeActivity.class));
                    finish();
                } else {
                    binding.llSplash.setVisibility(View.GONE);
                    binding.llIntro.setVisibility(View.VISIBLE);
                    binding.getRoot().setBackgroundColor(getColor(R.color.white));
                    binding.fabGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(binding.getRoot().getContext(), LoginActivity.class));
                            finish();
                        }
                    });
                }
            }
        }, 2000);
    }
}