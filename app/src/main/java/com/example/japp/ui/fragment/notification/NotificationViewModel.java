package com.example.japp.ui.fragment.notification;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.R;
import com.example.japp.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {

    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public void getJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(e -> {
            jobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            dataSnapshot.getChildren().forEach(dataSnapshot1 -> list.add(dataSnapshot1.getValue(Job.class)));
            jobs.setValue(list);
        });
    }

    public void getNotifications(String uid) {
        mDatabase.child("notification").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
        });
    }


}
