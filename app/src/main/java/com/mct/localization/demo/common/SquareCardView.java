package com.mct.localization.demo.common;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

public class SquareCardView extends MaterialCardView {
    public SquareCardView(Context context) {
        super(context);
    }

    public SquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int size, int heightMeasureSpec) {
        super.onMeasure(size, size);
    }
}
