package com.example.japp.ui.fragment.pending_jobs;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class PendingViewModel extends ViewModel {
    MutableLiveData<ArrayList<Job>> pendingJobs = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getPendingJobs(Context context) {
        mDatabase.child("users").child(new SharedHelper().getString(context, SharedHelper.uid)).child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                assert item != null;
                if (Objects.equals(item.getStatus(), "pending"))
                    list.add(item);
            }
            pendingJobs.setValue(list);
        }).addOnFailureListener(e -> {
            pendingJobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        });
    }

    public void getPosts(Context context) {
        mDatabase.child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                assert item != null;
                if (Objects.equals(item.getCompanyUid(), new SharedHelper().getString(context, SharedHelper.uid)))
                    list.add(item);
            }
            pendingJobs.setValue(list);
        }).addOnFailureListener(e -> {
            pendingJobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        });
    }
}
