package com.example.japp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.japp.R;
import com.example.japp.ui.fragment.active_jobs.ActiveJobsFragment;
import com.example.japp.ui.fragment.notification.NotificationFragment;
import com.example.japp.ui.fragment.pending_jobs.PendingJobsFragment;
import com.example.japp.ui.fragment.saved_jobs.SavedJobsFragment;

import java.util.Objects;

public class JobsPagerAdapter extends FragmentStateAdapter {

    Context context;
    String type;

    public JobsPagerAdapter(Context context, FragmentActivity activity, String type) {
        super(activity);
        this.context = context;
        this.type = type;

    }

    public CharSequence getPageTitle(int position) {
        if (Objects.equals(type, "JOB_SEEKER")) {
            if (position == 0)
                return context.getString(R.string.notification);
            else if (position == 1)
                return context.getString(R.string.saved_jobs);
            else if (position == 2)
                return context.getString(R.string.pending);
            else if (position == 3)
                return context.getString(R.string.active);
            else throw new IllegalArgumentException("Invalid position");
        } else {
            if (position == 0)
                return context.getString(R.string.notification);
            else if (position == 1)
                return context.getString(R.string.add_post);
            else if (position == 2)
                return context.getString(R.string.jobs);
            else throw new IllegalArgumentException("Invalid position");
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new NotificationFragment();
        else if (position == 1)
            return new SavedJobsFragment();
        else if (position == 2)
            return new PendingJobsFragment();
        else if (position == 3)
            return new ActiveJobsFragment();
        else throw new IllegalArgumentException("Invalid position");
    }

    @Override
    public int getItemCount() {
        if (Objects.equals(type, "JOB_SEEKER"))
            return 4;
        else
            return 3;
    }
}