package com.muhmmad.japp.ui.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhmmad.japp.R;
import com.muhmmad.japp.Utils.SharedHelper;
import com.muhmmad.japp.databinding.ActivityLoginBinding;
import com.muhmmad.japp.model.Job;
import com.muhmmad.japp.model.User;
import com.muhmmad.japp.ui.activity.home.HomeActivity;
import com.muhmmad.japp.ui.activity.register.RegisterActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("jobs").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onSuccess");
                Job job = new Job("Android developer", "this is the description", "Google", "", "Developer", "Full time", "Cairo", null, "1 year", "reject", "Developer", new SharedHelper().getString(context, SharedHelper.uid));
                dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).getRef().setValue(job);
            }
        });

        binding.tvSignUp.setOnClickListener(view -> startActivity(new Intent(context, RegisterActivity.class)));

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void login() {
        if (!isValidForm())
            return;
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                new SharedHelper().saveString(context, SharedHelper.uid, user.getUid());
                                mDatabase.child("users").child(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user != null) {
                                            new SharedHelper().saveString(context, SharedHelper.name, user.getName());
                                            new SharedHelper().saveString(context, SharedHelper.type, user.getType());
                                            new SharedHelper().saveString(context, SharedHelper.email, user.getEmail());
                                            new SharedHelper().saveString(context, SharedHelper.phone, user.getPhone());
                                        }
                                        startActivity(new Intent(context, HomeActivity.class));
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
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
}