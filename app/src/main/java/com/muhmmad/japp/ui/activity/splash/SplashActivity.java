package com.muhmmad.japp.ui.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.muhmmad.japp.R;
import com.muhmmad.japp.databinding.ActivitySplashBinding;
import com.muhmmad.japp.ui.activity.home.HomeActivity;
import com.muhmmad.japp.ui.activity.login.LoginActivity;

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
                if (mAuth.getCurrentUser() != null) {
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