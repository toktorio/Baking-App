package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.databinding.ListItemStepsBinding;

import java.util.List;

class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private final List<Step> steps;
    private final OnItemClickListener listener;
    private boolean multiPaneSelectionMode = false;
    private int selectedItemPosition = -1;

    RecipeStepsAdapter(@NonNull List<Step> steps, @NonNull OnItemClickListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    void setMultiPaneSelectionMode(boolean enabled) {
        this.multiPaneSelectionMode = enabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_steps, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.binding.setStep(step);

        if (step.getVideoURL() == null || TextUtils.isEmpty(step.getVideoURL())) {
            holder.binding.playVideoIcon.setVisibility(View.INVISIBLE);
        }

        if (multiPaneSelectionMode) {
            holder.binding.getRoot().setBackgroundColor(position == selectedItemPosition ? Color.GREEN : Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemStepsBinding binding;

        ViewHolder(ListItemStepsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (multiPaneSelectionMode) {
                        notifyItemChanged(selectedItemPosition);
                        selectedItemPosition = getAdapterPosition();
                        notifyItemChanged(selectedItemPosition);
                    }
                    Step step = steps.get(getAdapterPosition());
                    listener.onItemClick(step, getAdapterPosition());
                }
            });
        }
    }

    interface OnItemClickListener {

        void onItemClick(Step step, int position);
    }
}