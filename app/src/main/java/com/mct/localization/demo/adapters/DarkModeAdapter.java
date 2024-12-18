package com.mct.localization.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mct.localization.DarkMode;
import com.mct.localization.demo.databinding.LayoutSingleChoiceItemBinding;

import java.util.List;

public class DarkModeAdapter extends SingleChoiceAdapter<DarkMode, LayoutSingleChoiceItemBinding, DarkModeAdapter.ViewHolder> {

    public DarkModeAdapter(@NonNull List<DarkMode> items, DarkMode selectItem) {
        super(items, selectItem);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(LayoutSingleChoiceItemBinding.inflate(inflater, parent, false));
    }

    @Override
    protected void onBind(@NonNull ViewHolder holder, int position, @NonNull DarkMode nightMode) {
        holder.getBinding().tvTitle.setText(nightMode.getTitleRes());
        holder.getBinding().tvDescription.setVisibility(View.GONE);
    }

    protected static class ViewHolder extends SingleChoiceAdapter.ViewHolder<LayoutSingleChoiceItemBinding> {

        public ViewHolder(LayoutSingleChoiceItemBinding binding) {
            super(binding);
        }

        @Override
        protected void setChecked(boolean checked) {
            getBinding().rbChoice.setChecked(checked);
        }
    }

}
