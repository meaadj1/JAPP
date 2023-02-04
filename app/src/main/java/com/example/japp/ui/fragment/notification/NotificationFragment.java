package com.example.japp.ui.fragment.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.japp.adapter.NotificationAdapter;
import com.example.japp.databinding.FragmentNotificationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.function.Consumer;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private static final String TAG = "NotificationFragment";

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("notification").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<String> notifications = new ArrayList<>();
                dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                    notifications.add(dataSnapshot1.getValue(String.class));
                });
                if (!notifications.isEmpty()) {
                    binding.ivNotFound.setVisibility(View.GONE);
                    binding.tvNotFound.setVisibility(View.GONE);
                    binding.rvNotification.setVisibility(View.VISIBLE);
                    binding.rvNotification.setAdapter(new NotificationAdapter(notifications));
                } else {
                    binding.ivNotFound.setVisibility(View.VISIBLE);
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                    binding.rvNotification.setVisibility(View.GONE);
                }
            }
        });
    }
}