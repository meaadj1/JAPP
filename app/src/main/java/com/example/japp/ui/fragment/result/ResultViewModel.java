package com.example.japp.ui.fragment.result;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.japp.model.Job;
import com.example.japp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class ResultViewModel extends ViewModel {
    MutableLiveData<ArrayList<User>> users = new MutableLiveData();
    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getUsersByCity(String city) {
        mDatabase.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<User> list = new ArrayList();
                dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        if (Objects.equals(user.getCity(), city) && Objects.equals(user.getType(), "JOB_SEEKER")) {
                            list.add(user);
                        }
                    }
                });
                users.setValue(list);
            }
        });
    }

    public void getJobsByCity(String city) {
        mDatabase.child("jobs").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<Job> list = new ArrayList();
                dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        Job job = dataSnapshot.getValue(Job.class);
                        assert job != null;
                        if (Objects.equals(job.getLocation(), city)) {
                            list.add(job);
                        }
                    }
                });
                jobs.setValue(list);
            }
        });
    }

    public void getJobsByCategory(String category) {
        mDatabase.child("jobs").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<Job> list = new ArrayList();
                dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        Job job = dataSnapshot.getValue(Job.class);
                        assert job != null;
                        if (Objects.equals(job.getCategory(), category)) {
                            list.add(job);
                        }
                    }
                });
                jobs.setValue(list);
            }
        });
    }
}
