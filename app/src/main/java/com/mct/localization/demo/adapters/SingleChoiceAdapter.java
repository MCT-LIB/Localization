package com.mct.localization.demo.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SingleChoiceAdapter<I, B extends ViewDataBinding, VH extends SingleChoiceAdapter.ViewHolder<B>> extends BaseQuickAdapter<I, VH> {

    private final Set<VH> boundViewHolders;

    private OnItemClickListener<I> listener;
    private int selectedId;

    public SingleChoiceAdapter(List<I> items) {
        this(items, null);
    }

    public SingleChoiceAdapter(List<I> items, I selectItem) {
        if (items == null) {
            items = new ArrayList<>();
        }
        setItems(items);
        this.selectedId = items.indexOf(selectItem);
        this.boundViewHolders = new HashSet<>();
    }

    public int getSelectedId() {
        return selectedId;
    }

    @Nullable
    public I getSelectedItem() {
        return getItem(selectedId);
    }

    public void setSelectedItem(@Nullable I item) {
        if (item == null) {
            selectedId = -1;
        } else {
            selectedId = itemIndexOfFirst(item);
        }
        invalidateChecked();
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
        invalidateChecked();
    }

    public void setListener(OnItemClickListener<I> listener) {
        this.listener = listener;
    }

    public Set<VH> getBoundViewHolders() {
        return Collections.unmodifiableSet(boundViewHolders);
    }

    @Override
    protected final void onBindViewHolder(@NonNull VH holder, int position, @Nullable I item) {
        if (item == null) {
            return;
        }
        onBind(holder, position, item);
        holder.setChecked(position == selectedId);
        holder.getClickableItem().setOnClickListener(v -> {
            int newId = holder.getBindingAdapterPosition();
            if (newId == selectedId) {
                return;
            }
            selectedId = newId;
            invalidateChecked();
            if (listener != null) {
                listener.onItemClick(item, newId);
            }
        });
        if (holder.isRecyclable()) {
            boundViewHolders.add(holder);
        }
    }

    private void invalidateChecked() {
        for (VH holder : boundViewHolders) {
            holder.setChecked(holder.getBindingAdapterPosition() == selectedId);
        }
    }

    protected abstract void onBind(@NonNull VH holder, int position, @NonNull I item);

    protected static abstract class ViewHolder<B extends ViewDataBinding> extends DataBindingHolder<B> {

        public ViewHolder(B binding) {
            super(binding);
        }

        protected View getClickableItem() {
            return getBinding().getRoot();
        }

        protected abstract void setChecked(boolean checked);
    }

    public interface OnItemClickListener<I> {
        void onItemClick(I item, int position);
    }
}
