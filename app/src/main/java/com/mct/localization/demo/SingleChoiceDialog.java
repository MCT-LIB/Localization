package com.mct.localization.demo;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mct.base.ui.binding.BaseBindingOverlayDialog;
import com.mct.base.ui.utils.ScreenUtils;
import com.mct.localization.demo.adapters.SingleChoiceAdapter;
import com.mct.localization.demo.adapters.decor.MarginItemDecoration;
import com.mct.localization.demo.databinding.DialogSingleChoiceBinding;

public class SingleChoiceDialog<I> extends BaseBindingOverlayDialog<DialogSingleChoiceBinding> {

    private static final int ITEM_MARGIN_BETWEEN = ScreenUtils.dp2px(8);

    private String title;
    private String positiveButton;
    private String negativeButton;
    private Consumer<SingleChoiceDialog<I>> onClickPositiveButton;
    private Consumer<SingleChoiceDialog<I>> onClickNegativeButton;

    private int spanCount = 1;
    private SingleChoiceAdapter<I, ?, ?> adapter;

    public SingleChoiceDialog(@NonNull Context context) {
        super(context);
    }

    public SingleChoiceDialog<I> setTitle(String title) {
        this.title = title;
        return this;
    }

    public SingleChoiceDialog<I> setPositiveButton(String positiveButton, Consumer<SingleChoiceDialog<I>> onClickPositiveButton) {
        this.positiveButton = positiveButton;
        this.onClickPositiveButton = onClickPositiveButton;
        return this;
    }

    public SingleChoiceDialog<I> setNegativeButton(String negativeButton, Consumer<SingleChoiceDialog<I>> onClickNegativeButton) {
        this.negativeButton = negativeButton;
        this.onClickNegativeButton = onClickNegativeButton;
        return this;
    }

    public SingleChoiceDialog<I> setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public SingleChoiceDialog<I> setAdapter(SingleChoiceAdapter<I, ?, ?> adapter) {
        this.adapter = adapter;
        return this;
    }

    @Override
    protected AppCompatDialog onCreateDialog(Context context) {
        return new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setPositiveButton(positiveButton, (dialog, which) -> onClickPositiveButton.accept(this))
                .setNegativeButton(negativeButton, (dialog, which) -> onClickNegativeButton.accept(this))
                .create();
    }

    @Nullable
    @Override
    protected DialogOption onCreateDialogOption() {
        return new DialogOption.Builder()
                .setCornerRadius(ScreenUtils.dp2px(8))
                .setBackgroundInsets(ScreenUtils.dp2px(16), ScreenUtils.dp2px(64), ScreenUtils.dp2px(16), ScreenUtils.dp2px(64))
                .setBackgroundColor(MaterialColors.getColor(getContext(), com.google.android.material.R.attr.colorSurface, 0))
                .build();
    }

    @Override
    public Class<DialogSingleChoiceBinding> getBindingClass() {
        return DialogSingleChoiceBinding.class;
    }

    @Override
    protected void onDialogCreated(@NonNull AppCompatDialog dialog, DialogOption dialogOption, View view) {
        super.onDialogCreated(dialog, dialogOption, view);
        for (int i = binding.rcvChoices.getItemDecorationCount() - 1; i >= 0; i--) {
            RecyclerView.ItemDecoration decoration = binding.rcvChoices.getItemDecorationAt(i);
            if (decoration instanceof MarginItemDecoration) {
                binding.rcvChoices.removeItemDecoration(decoration);
            }
        }
        binding.rcvChoices.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        binding.rcvChoices.addItemDecoration(new MarginItemDecoration().setMarginBetween(ITEM_MARGIN_BETWEEN));
        binding.rcvChoices.setAdapter(adapter);
        binding.rcvChoices.scrollToPosition(adapter != null ? adapter.getSelectedId() : -1);
    }

    public I getSelectedItem() {
        if (adapter == null) {
            return null;
        }
        return adapter.getSelectedItem();
    }

}
