package com.example.japp.ui.fragment.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.databinding.FragmentSettingBinding;
import com.example.japp.databinding.SheetLogoutBinding;
import com.example.japp.ui.activity.login.LoginActivity;
import com.example.japp.ui.activity.splash.SplashActivity;
import com.example.japp.ui.activity.verify.VerifyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        binding.llPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
            assert email != null;
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                Toast.makeText(binding.getRoot().getContext(), getString(R.string.we_have_sent_the_verification_code_to_email_address), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(binding.getRoot().getContext(), VerifyActivity.class);
                intent.putExtra("type", "password");
                intent.putExtra("email", email);
                startActivity(intent);
            }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        });

        binding.llLogout.setOnClickListener(v -> showBottomSheet());
        binding.llLanguage.setOnClickListener(v -> ShowLanguageSheet());
    }

    void showBottomSheet() {
        SheetLogoutBinding sheetBinding = SheetLogoutBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(sheetBinding.getRoot().getContext());
        dialog.setContentView(sheetBinding.getRoot());
        sheetBinding.btnYes.setOnClickListener(v -> {
            binding.getRoot().getContext().deleteSharedPreferences("app_data");
            FirebaseAuth.getInstance().signOut();
            new SharedHelper().saveString(binding.getRoot().getContext(), SharedHelper.uid, "");
            new SharedHelper().saveString(binding.getRoot().getContext(), SharedHelper.type, "");
            startActivity(new Intent(sheetBinding.getRoot().getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        sheetBinding.btnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    void ShowLanguageSheet() {
        SheetLogoutBinding sheetBinding = SheetLogoutBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(sheetBinding.getRoot().getContext());
        dialog.setContentView(sheetBinding.getRoot());
        sheetBinding.tvTitle.setText(getString(R.string.language));
        sheetBinding.tvDetails.setText(getString(R.string.choose_language));
        sheetBinding.btnYes.setText(getString(R.string.ar));
        sheetBinding.btnCancel.setText(getString(R.string.en));
        sheetBinding.btnYes.setOnClickListener(v -> {
            setLocale(requireActivity(), "ar");
            LocaleHelper.setLocale(sheetBinding.getRoot().getContext(), "ar");
            startActivity(new Intent(sheetBinding.getRoot().getContext(), SplashActivity.class));
            requireActivity().recreate();
        });

        sheetBinding.btnCancel.setOnClickListener(v -> {
            setLocale(requireActivity(), "en");
            LocaleHelper.setLocale(sheetBinding.getRoot().getContext(), "en");
            startActivity(new Intent(sheetBinding.getRoot().getContext(), SplashActivity.class));
            requireActivity().recreate();
        });

        dialog.show();
    }

    void setLocale(Activity activity, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources res = activity.getResources();
        Configuration config = res.getConfiguration();
        res.getConfiguration().setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}