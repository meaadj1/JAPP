package com.example.japp.ui.fragment.active_jobs;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ActiveViewModel extends ViewModel {
    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getActiveJobs(Context context) {
        mDatabase.child("users").child(new SharedHelper().getString(context, SharedHelper.uid)).child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                assert item != null;
                if (Objects.equals(item.getStatus(), "reject") || Objects.equals(item.getStatus(), "accept"))
                    list.add(item);
            }
            jobs.setValue(list);
        });
    }
}
