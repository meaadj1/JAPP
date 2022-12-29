package com.example.japp.ui.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.japp.R;
import com.example.japp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }
}