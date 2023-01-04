package com.example.japp.ui.fragment.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {
    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData<>();
    MutableLiveData<ArrayList<User>> applicants = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(e -> {
            jobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                list.add(dataSnapshot.child(String.valueOf(i)).getValue(Job.class));
            }
            jobs.setValue(list);
        });
    }

    public void getApplicants(Context context) {
        mDatabase.child("users").child(new SharedHelper().getString(context, SharedHelper.uid)).child("applicants").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<User> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                list.add(dataSnapshot.child(String.valueOf(i)).getValue(User.class));
            }
            applicants.setValue(list);
        }).addOnFailureListener(e -> {
            applicants.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        });
    }

    public void getFullTimeJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(e -> {
            jobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                assert item != null;
                if (Objects.equals(item.getType(), "Full time"))
                    list.add(item);
            }
            jobs.setValue(list);
        });
    }

    public void getPartTimeJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(e -> {
            jobs.setValue(null);
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(dataSnapshot -> {
            ArrayList<Job> list = new ArrayList<>();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                assert item != null;
                if (Objects.equals(item.getType(), "Part time"))
                    list.add(item);
            }
            jobs.setValue(list);
        });
    }

    public void savingJob(Context context, int id) {
        mDatabase.child("jobs").child(String.valueOf(id)).child("saved").get().addOnSuccessListener(dataSnapshot -> mDatabase.child("jobs").child(String.valueOf(id)).child("saved").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(new SharedHelper().getString(context, SharedHelper.uid)));
    }

    public void deleteJob(int id, String uid) {
        mDatabase.child("jobs").child(String.valueOf(id)).child("saved").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<String> list = new ArrayList<>();
            dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                String item = dataSnapshot1.getValue(String.class);
                if (!Objects.equals(item, uid))
                    list.add(item);
            });
            dataSnapshot.getRef().setValue(list);
        });
    }
}