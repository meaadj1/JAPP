package com.example.japp.ui.fragment.update_password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.japp.R;
import com.example.japp.Utils.LocaleHelper;
import com.example.japp.databinding.FragmentUpdatePasswordBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UpdatePasswordFragment extends Fragment {

    private FragmentUpdatePasswordBinding binding;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocaleHelper.setLocale(binding.getRoot().getContext(), LocaleHelper.getLanguage(binding.getRoot().getContext()));


        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        binding.btnUpdate.setOnClickListener(v -> {
            if (!isValidForm())
                return;
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updatePassword(password).addOnSuccessListener(unused -> Toast.makeText(binding.getRoot().getContext(), "The password was updated successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private boolean isValidForm() {
        boolean valid = true;
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString().trim();
        String confPassword = Objects.requireNonNull(binding.edtConfirm.getText()).toString().trim();
        if (TextUtils.isEmpty(password)) {
            binding.inputPassword.setError(getString(R.string.pass_alert));
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        if (TextUtils.isEmpty(confPassword)) {
            binding.inputConfirm.setError(getString(R.string.pass_alert));
            valid = false;
        } else {
            binding.inputConfirm.setError(null);
        }

        if (!password.equals(confPassword)) {
            valid = false;
            Toast.makeText(binding.getRoot().getContext(), getString(R.string.same_password), Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}