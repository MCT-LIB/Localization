package com.mct.localization.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mct.localization.Language;
import com.mct.localization.demo.databinding.LayoutSingleChoiceItemBinding;

import java.util.List;

public class LanguageAdapter extends SingleChoiceAdapter<Language, LayoutSingleChoiceItemBinding, LanguageAdapter.ViewHolder> {

    public LanguageAdapter(@NonNull List<Language> items, Language selectItem) {
        super(items, selectItem);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(LayoutSingleChoiceItemBinding.inflate(inflater, parent, false));
    }

    @Override
    protected void onBind(@NonNull ViewHolder holder, int position, @NonNull Language language) {
        holder.getBinding().tvTitle.setText(language.getDisplayLanguage());
        holder.getBinding().tvDescription.setText(language.getDisplayLanguageBySelf());
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
