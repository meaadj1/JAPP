package com.example.japp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.databinding.SkillItemBinding;

import java.util.ArrayList;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    ArrayList<String> skills;

    public SkillsAdapter(ArrayList<String> skills) {
        super();
        this.skills = skills;
    }

    @NonNull
    @Override
    public SkillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SkillItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAdapter.ViewHolder holder, int position) {
        holder.binding.textView.setText(skills.get(position));
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void addingItem(String item) {
        skills.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<String> getList() {
        return skills;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SkillItemBinding binding;

        public ViewHolder(SkillItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
