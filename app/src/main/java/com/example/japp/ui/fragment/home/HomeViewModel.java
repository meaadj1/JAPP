package com.example.japp.ui.fragment.home;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.japp.R;
import com.example.japp.Utils.SharedHelper;
import com.example.japp.model.Job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Job>> jobs = new MutableLiveData();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void getJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                jobs.setValue(null);
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
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

    public void getFullTimeJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                jobs.setValue(null);
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<Job> list = new ArrayList();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                    assert item != null;
                    if (Objects.equals(item.getType(), "Full time"))
                        list.add(item);
                }
                jobs.setValue(list);
            }
        });
    }

    public void getPartTimeJobs(Context context) {
        mDatabase.child("jobs").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                jobs.setValue(null);
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<Job> list = new ArrayList();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    Job item = dataSnapshot.child(String.valueOf(i)).getValue(Job.class);
                    assert item != null;
                    if (Objects.equals(item.getType(), "Part time"))
                        list.add(item);
                }
                jobs.setValue(list);
            }
        });
    }

    public void savingJob(Context context, int id) {
        mDatabase.child("jobs").child(String.valueOf(id)).child("saved").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mDatabase.child("jobs").child(String.valueOf(id)).child("saved").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(new SharedHelper().getString(context, SharedHelper.uid));
            }
        });
    }
}