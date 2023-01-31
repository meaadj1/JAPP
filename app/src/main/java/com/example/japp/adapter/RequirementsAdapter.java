package com.example.japp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.databinding.RequirementItemBinding;
import com.example.japp.model.Requirement;

import java.util.ArrayList;
import java.util.List;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.ViewHolder> {

    private List<Requirement> list;
    private ArrayList<Requirement> checkedList = new ArrayList<>();

    public RequirementsAdapter(List<Requirement> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RequirementItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvItem.setText(list.get(position).getText());
        holder.binding.tvItem.setOnClickListener(v -> {
            if (holder.binding.tvItem.isChecked()) {
                holder.binding.tvItem.setChecked(false);
                checkedList.remove(holder.binding.tvItem.getText());
            } else {
                holder.binding.tvItem.setChecked(true);
                checkedList.add(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Requirement> getCheckedList() {
        return checkedList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RequirementItemBinding binding;

        public ViewHolder(RequirementItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
