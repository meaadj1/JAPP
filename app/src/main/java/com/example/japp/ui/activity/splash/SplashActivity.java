package com.example.japp.ui.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.ActivitySplashBinding;
import com.example.japp.ui.activity.home.HomeActivity;
import com.example.japp.ui.activity.login.LoginActivity;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        new Handler().postDelayed(() -> {
            if (!Objects.equals(new SharedHelper().getString(binding.getRoot().getContext(), SharedHelper.user), "")) {
                startActivity(new Intent(binding.getRoot().getContext(), HomeActivity.class));
                finish();
            } else {
                binding.llSplash.setVisibility(View.GONE);
                binding.llIntro.setVisibility(View.VISIBLE);
                binding.getRoot().setBackgroundColor(getColor(R.color.white));
                binding.fabGo.setOnClickListener(v -> {
                    startActivity(new Intent(binding.getRoot().getContext(), LoginActivity.class));
                    finish();
                });
            }
        }, 2000);
    }
}