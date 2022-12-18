package com.example.japp.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.japp.R;
import com.example.japp.ui.fragment.active_jobs.ActiveJobsFragment;
import com.example.japp.ui.fragment.notification.NotificationFragment;
import com.example.japp.ui.fragment.pending_jobs.PendingJobsFragment;
import com.example.japp.ui.fragment.saved_jobs.SavedJobsFragment;

public class JobsPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public JobsPagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
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
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return context.getString(R.string.notification);
        else if (position == 1)
            return context.getString(R.string.saved_jobs);
        else if (position == 2)
            return context.getString(R.string.pending);
        else if (position == 3)
            return context.getString(R.string.active);
        else throw new IllegalArgumentException("Invalid position");
    }
}
