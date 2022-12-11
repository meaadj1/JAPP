package com.muhmmad.japp.ui.fragment.home;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhmmad.japp.model.Job;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<Job> list = new ArrayList();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    list.add(dataSnapshot.child(String.valueOf(i)).getValue(Job.class));
                }
                jobs.setValue(list);
            }
        });
    }
}
