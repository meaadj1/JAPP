package com.example.japp.ui.fragment.saved_jobs;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.Utils.SharedHelper;
import com.example.japp.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class SavedViewModel extends ViewModel {
    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getSavedJobs(Context context) {
        mDatabase.child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                if (item != null && item.getSaved() != null) {
                    for (int j = 0; j < Objects.requireNonNull(item).getSaved().size(); j++) {
                        if (Objects.equals(item.getSaved().get(j), new SharedHelper().getString(context, SharedHelper.uid)))
                            list.add(item);
                    }
                }
            }
            jobs.setValue(list);
        });
    }

    public void uploadNotification(String name, String jobTitle) {
        mDatabase.child("notification").get().addOnSuccessListener(dataSnapshot -> dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).getRef().setValue(name + " uploaded a new Job " + jobTitle));
    }
}
