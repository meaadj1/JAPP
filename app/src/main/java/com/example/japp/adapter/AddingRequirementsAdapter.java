package com.example.japp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japp.R;
import com.example.japp.databinding.AddingItemLayoutBinding;
import com.example.japp.databinding.SkillItemBinding;
import com.example.japp.model.Requirement;

import java.util.ArrayList;
import java.util.Objects;

public class AddingRequirementsAdapter extends RecyclerView.Adapter<AddingRequirementsAdapter.ViewHolder> {

    ArrayList<Requirement> skills = new ArrayList<>();
    int count = 0;

    public AddingRequirementsAdapter(ArrayList<Requirement> skills) {
        if (skills != null)
            this.skills.addAll(skills);
    }

    @NonNull
    @Override
    public AddingRequirementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SkillItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddingRequirementsAdapter.ViewHolder holder, int position) {
        holder.binding.edtValue.setVisibility(View.VISIBLE);
        holder.binding.textView.setText(skills.get(position).getText());
        holder.binding.edtValue.setText(String.valueOf(skills.get(position).getValue()));
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

    public void addingItem(Requirement item) {
        skills.add(item);
        count += item.getValue();
        notifyDataSetChanged();
    }

    public ArrayList<Requirement> getList() {
        return skills;
    }

    public int getListCount() {
        return count;
    }

    public void setList(ArrayList<Requirement> list) {
        skills = list;
        notifyDataSetChanged();
    }

    void editItem(Context context, int position) {
        AddingItemLayoutBinding dialogBinding = AddingItemLayoutBinding.inflate(LayoutInflater.from(context), null, false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.cvEdtValue.setVisibility(View.VISIBLE);
        dialogBinding.edtItem.setText(skills.get(position).getText());
        dialogBinding.edtValue.setText(String.valueOf(skills.get(position).getValue()));
        dialogBinding.tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.tvSave.setOnClickListener(v -> {
            if (dialogBinding.edtItem.getText() != null || dialogBinding.edtValue.getText() != null) {
                skills.set(position, new Requirement(dialogBinding.edtItem.getText().toString(), Integer.parseInt(Objects.requireNonNull(dialogBinding.edtValue.getText()).toString())));
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
