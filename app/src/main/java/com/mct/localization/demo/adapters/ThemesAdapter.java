package com.mct.localization.demo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.color.MaterialColors;
import com.mct.localization.Theme;
import com.mct.localization.demo.databinding.LayoutThemeItemBinding;

import java.util.List;

public class ThemesAdapter extends SingleChoiceAdapter<Theme, LayoutThemeItemBinding, ThemesAdapter.ViewHolder> {

    private static final int[] COLOR_ATTRS = {
            com.google.android.material.R.attr.colorPrimary,
            com.google.android.material.R.attr.colorSecondary,
            com.google.android.material.R.attr.colorTertiary,
            com.google.android.material.R.attr.colorPrimaryContainer
    };

    public ThemesAdapter(List<Theme> items, Theme selectItem) {
        super(items, selectItem);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(LayoutThemeItemBinding.inflate(inflater, parent, false));
    }

    @Override
    protected void onBind(@NonNull ViewHolder holder, int position, @NonNull Theme theme) {

        int[] colors = theme.getColors(getContext(), COLOR_ATTRS);

        holder.getBinding().palettePrimary.setBackgroundColor(MaterialColors.harmonize(colors[0], Color.GRAY));
        holder.getBinding().paletteSecondary.setBackgroundColor(MaterialColors.harmonize(colors[1], Color.GRAY));
        holder.getBinding().paletteTertiary.setBackgroundColor(MaterialColors.harmonize(colors[2], Color.GRAY));

        int rippleColor = MaterialColors.compositeARGBWithAlpha(colors[3], 180);
        holder.getBinding().cardRoot.setRippleColor(ColorStateList.valueOf(rippleColor));

        holder.getBinding().ivChecked.setBackgroundColor(colors[3]);
        holder.getBinding().ivChecked.setImageTintList(ColorStateList.valueOf(colors[0]));

        holder.getBinding().tvName.setText(theme.getName());
    }

    protected static class ViewHolder extends SingleChoiceAdapter.ViewHolder<LayoutThemeItemBinding> {

        public ViewHolder(LayoutThemeItemBinding binding) {
            super(binding);
        }

        @Override
        protected View getClickableItem() {
            return getBinding().cardRoot;
        }

        @Override
        protected void setChecked(boolean checked) {
            getBinding().ivChecked.setVisibility(checked ? View.VISIBLE : View.GONE);
        }
    }
}
