package com.example.japp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.databinding.RequirementItemBinding;

import java.util.ArrayList;
import java.util.List;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.ViewHolder> {

    private List<String> list;
    private ArrayList<String> checkedList = new ArrayList<>();

    public RequirementsAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RequirementItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvItem.setText(list.get(position));
        holder.binding.tvItem.setOnClickListener(v -> {
            if (holder.binding.tvItem.isChecked()) {
                holder.binding.tvItem.setChecked(false);
                checkedList.remove(holder.binding.tvItem.getText());
            } else {
                holder.binding.tvItem.setChecked(true);
                checkedList.add((String) holder.binding.tvItem.getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<String> getCheckedList() {
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
