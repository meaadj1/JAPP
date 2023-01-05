package com.example.japp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.R;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.SkillItemBinding;

import java.util.ArrayList;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    ArrayList<String> skills = new ArrayList<>();

    public SkillsAdapter(ArrayList<String> skills) {
        if (skills != null)
            this.skills.addAll(skills);
    }

    @NonNull
    @Override
    public SkillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SkillItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAdapter.ViewHolder holder, int position) {
        holder.binding.textView.setText(skills.get(position));
        holder.binding.ivDelete.setOnClickListener(v -> {
            skills.remove(skills.get(position));
            notifyDataSetChanged();
        });

        holder.binding.ivEdit.setOnClickListener(v -> editItem(holder.binding.getRoot().getContext(), position));
    }

    @Override
    public int getItemCount() {
        if (skills != null)
            return skills.size();
        else
            return 0;
    }

    public void addingItem(String item) {
        skills.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<String> getList() {
        return skills;
    }

    public void setList(ArrayList<String> list) {
        skills = list;
        notifyDataSetChanged();
    }

    void editItem(Context context, int position) {
        AddingItemLayoutBinding dialogBinding = AddingItemLayoutBinding.inflate(LayoutInflater.from(context), null, false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.edtItem.setText(skills.get(position));
        dialogBinding.tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.tvSave.setOnClickListener(v -> {
            if (dialogBinding.edtItem.getText() != null) {
                skills.set(position, dialogBinding.edtItem.getText().toString());
                notifyDataSetChanged();
                dialog.dismiss();
            } else
                Toast.makeText(context, context.getString(R.string.edt_alert), Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SkillItemBinding binding;

        public ViewHolder(SkillItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
