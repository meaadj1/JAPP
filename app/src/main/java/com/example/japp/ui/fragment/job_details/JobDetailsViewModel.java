package com.example.japp.ui.fragment.job_details;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.R;
import com.example.japp.model.Job;
import com.example.japp.model.Requirement;
import com.example.japp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class JobDetailsViewModel extends ViewModel {

    private static final String TAG = "JobDetailsViewModel";

    public MutableLiveData<Boolean> isDone = new MutableLiveData<>();
    MutableLiveData<Boolean> isApplied = new MutableLiveData<>();
    MutableLiveData<User> userData = new MutableLiveData<>();
    MutableLiveData<User> companyData = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void applyJob(Context context, String uid, Job data, User user, ArrayList<Requirement> items) {
        final float[] validate = {0};
        HashMap<String, Integer> map;
        if (data.getMatching() != null)
            map = data.getMatching();
        else
            map = new HashMap<>();
        if (items != null) {
            items.forEach(requirement -> validate[0] += requirement.getValue());
            user.setMatchingList(items);
        }
        user.setMatching((int) validate[0]);
        map.put(uid, (int) validate[0]);
        data.setStatus("pending");
        data.setMatching(map);
        mDatabase.child("jobs").child(String.valueOf(data.getId())).setValue(data);
        mDatabase.child("users").child(uid).child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            mDatabase.child("users").child(uid).child("jobs").get().addOnSuccessListener(dataSnapshot13 -> {
                dataSnapshot13.getChildren().forEach(dataSnapshot12 -> {
                    if (Objects.equals(Objects.requireNonNull(dataSnapshot12.getValue(Job.class)).getTitle(), data.getTitle())) {
                        isApplied.setValue(true);
                        Toast.makeText(context, "you already applied this job before", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!Boolean.TRUE.equals(isApplied.getValue())) {
                    isApplied.setValue(false);
                    mDatabase.child("users").child(uid).child("jobs").child(String.valueOf(dataSnapshot13.getChildrenCount())).setValue(data);
                    mDatabase.child("users").child(data.getCompanyUid()).child("applicants").get().addOnSuccessListener(dataSnapshot1 -> {
                        user.setJobId(data.getId());
                        mDatabase.child("users").child(data.getCompanyUid()).child("applicants").child(String.valueOf(dataSnapshot1.getChildrenCount())).setValue(user);
                    });
                }
            });
            Toast.makeText(context, context.getString(R.string.apply_job), Toast.LENGTH_SHORT).show();
            isDone.setValue(true);
        });
    }

    public void rejectUser(Context context, String email, int jobId, String uid) {
        mDatabase.child("users").get().addOnSuccessListener(dataSnapshot -> dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
            User user = dataSnapshot1.getValue(User.class);
            assert user != null;
            if (Objects.equals(user.getEmail(), email)) {
                dataSnapshot1.child("jobs").child(String.valueOf(jobId)).child("status").getRef().setValue("reject");
                Toast.makeText(context, context.getString(R.string.reject_user), Toast.LENGTH_SHORT).show();
                isDone.setValue(true);
            }
        })).addOnFailureListener(e -> Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show());
        mDatabase.child("users").child(uid).child("applicants").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<User> list = new ArrayList<>();
            dataSnapshot.getChildren().forEach(dataSnapshot12 -> {
                User user = dataSnapshot12.getValue(User.class);
                assert user != null;
                if (Objects.equals(user.getEmail(), email) && user.getJobId() == jobId) {
                } else {
                    list.add(user);
                }
            });
            dataSnapshot.getRef().setValue(list);
        });
    }

    public void acceptUser(Context context, String email, int jobId, String uid) {
        mDatabase.child("users").get().addOnSuccessListener(dataSnapshot -> dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
            User user = dataSnapshot1.getValue(User.class);
            assert user != null;
            if (Objects.equals(user.getEmail(), email)) {
                dataSnapshot1.child("jobs").child(String.valueOf(jobId)).child("status").getRef().setValue("accept");
                Toast.makeText(context, context.getString(R.string.accept_user), Toast.LENGTH_SHORT).show();
                isDone.setValue(true);
            }
        })).addOnFailureListener(e -> Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show());
        mDatabase.child("users").child(uid).child("applicants").get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<User> list = new ArrayList<>();
            dataSnapshot.getChildren().forEach(dataSnapshot12 -> {
                User user = dataSnapshot12.getValue(User.class);
                assert user != null;
                if (Objects.equals(user.getEmail(), email) && user.getJobId() == jobId) {
                } else {
                    list.add(user);
                }
            });
            dataSnapshot.getRef().setValue(list);
        });
    }

    public void getUserData(Context context, String uid) {
        mDatabase.child("users").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userData.setValue(dataSnapshot.getValue(User.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userData.setValue(null);
            }
        });
    }

    public void getCompanyData(String uid) {
        mDatabase.child("users").child(uid).get().addOnSuccessListener(dataSnapshot -> companyData.setValue(dataSnapshot.getValue(User.class))).addOnFailureListener(e -> companyData.setValue(null));
    }
}
