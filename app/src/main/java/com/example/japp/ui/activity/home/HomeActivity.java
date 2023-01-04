package com.example.japp.ui.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                navController.navigate(R.id.nav_search);
                return true;
            } else if (item.getItemId() == R.id.nav_jobs) {
                navController.navigate(R.id.nav_jobs);
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                navController.navigate(R.id.nav_profile);
                return true;
            }
            return false;
        });
    }
}