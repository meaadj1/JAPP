package com.example.japp.ui.fragment.job_details;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.R;
import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class JobDetailsViewModel extends ViewModel {

    private static final String TAG = "JobDetailsViewModel";

    public MutableLiveData<Boolean> isDone = new MutableLiveData<>();
    MutableLiveData<Boolean> isApplied = new MutableLiveData<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void applyJob(Context context, String uid, Job data, User user) {
        isApplied.setValue(false);
        mDatabase.child("users").child(uid).child("jobs").get().addOnSuccessListener(dataSnapshot -> {
            float validate = 0;
            if (data.getRequirements() != null) {
                for (int i = 0; i < data.getRequirements().size(); i++) {
                    if (user.getSkills() != null) {
                        for (int j = 0; j < user.getSkills().size(); j++) {
                            if (Objects.equals(data.getRequirements().get(i), user.getSkills().get(j)))
                                validate++;
                        }
                    }
                }
                validate = (validate / data.getRequirements().size()) * 100;
            }
            user.setMatching(validate);
            data.setStatus("pending");
            mDatabase.child("users").child(uid).child("jobs").get().addOnSuccessListener(dataSnapshot13 -> dataSnapshot13.getChildren().forEach(dataSnapshot12 -> {
                if (Objects.equals(Objects.requireNonNull(dataSnapshot12.getValue(Job.class)).getTitle(), data.getTitle())) {
                    isApplied.setValue(true);
                }
            }));
            if (!isApplied.getValue()) {
                mDatabase.child("users").child(uid).child("jobs").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(data);
                mDatabase.child("users").child(data.getCompanyUid()).child("applicants").get().addOnSuccessListener(dataSnapshot1 -> {
                    user.setJobId(data.getId());
                    mDatabase.child("users").child(data.getCompanyUid()).child("applicants").child(String.valueOf(dataSnapshot1.getChildrenCount())).setValue(user);
                });
            }
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
}
