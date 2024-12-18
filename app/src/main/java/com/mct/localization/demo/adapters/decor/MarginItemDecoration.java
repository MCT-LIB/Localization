package com.mct.localization.demo.adapters.decor;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @noinspection unused
 */
public final class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private int orientation;
    private int marginBetween;
    private final Rect margin;

    public MarginItemDecoration() {
        this.orientation = RecyclerView.VERTICAL;
        this.margin = new Rect();
    }

    @RecyclerView.Orientation
    public int getOrientation() {
        return orientation;
    }

    public MarginItemDecoration setOrientation(@RecyclerView.Orientation int orientation) {
        this.orientation = orientation;
        return this;
    }

    public int getMarginBetween() {
        return marginBetween;
    }

    public MarginItemDecoration setMarginBetween(int marginBetween) {
        this.marginBetween = marginBetween;
        return this;
    }

    public MarginItemDecoration setMargin(@NonNull Rect margin) {
        setMargin(margin.left, margin.top, margin.right, margin.bottom);
        return this;
    }

    public MarginItemDecoration setMargin(int margin) {
        setMargin(margin, margin, margin, margin);
        return this;
    }

    public MarginItemDecoration setMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        this.margin.left = marginLeft;
        this.margin.top = marginTop;
        this.margin.right = marginRight;
        this.margin.bottom = marginBottom;
        return this;
    }

    public MarginItemDecoration setMarginLeft(int marginLeft) {
        this.margin.left = marginLeft;
        return this;
    }

    public MarginItemDecoration setMarginTop(int marginTop) {
        this.margin.top = marginTop;
        return this;
    }

    public MarginItemDecoration setMarginRight(int marginRight) {
        this.margin.right = marginRight;
        return this;
    }

    public MarginItemDecoration setMarginBottom(int marginBottom) {
        this.margin.bottom = marginBottom;
        return this;
    }

    public MarginItemDecoration setMarginVertical(int marginVertical) {
        this.margin.top = marginVertical;
        this.margin.bottom = marginVertical;
        return this;
    }

    public MarginItemDecoration setMarginHorizontal(int marginHorizontal) {
        this.margin.left = marginHorizontal;
        this.margin.right = marginHorizontal;
        return this;
    }

    public int getMarginLeft() {
        return margin.left;
    }

    public int getMarginTop() {
        return margin.top;
    }

    public int getMarginRight() {
        return margin.right;
    }

    public int getMarginBottom() {
        return margin.bottom;
    }

    @NonNull
    public Rect getMargin() {
        return margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null) {
            return;
        }
        final int pxLeft = margin.left;
        final int pxTop = margin.top;
        final int pxRight = margin.right;
        final int pxBottom = margin.bottom;
        final int pxBetween = marginBetween / 2;

        final int count = parent.getAdapter().getItemCount();
        final int position = parent.getChildAdapterPosition(view);
        final boolean firstPosition = position == 0;
        final boolean lastPosition = position == count - 1;

        switch (orientation) {
            case RecyclerView.HORIZONTAL:
                outRect.top = pxTop;
                outRect.bottom = pxBottom;
                outRect.left = pxBetween;
                outRect.right = pxBetween;

                if (count == 1) {
                    outRect.left = pxLeft;
                    outRect.right = pxRight;
                } else {
                    if (firstPosition) {
                        outRect.left = pxLeft;
                    }
                    if (lastPosition) {
                        outRect.right = pxRight;
                    }
                }
                break;
            case RecyclerView.VERTICAL:
                outRect.left = pxLeft;
                outRect.right = pxRight;
                outRect.top = pxBetween;
                outRect.bottom = pxBetween;

                if (count == 1) {
                    outRect.top = pxTop;
                    outRect.bottom = pxBottom;
                } else {
                    if (firstPosition) {
                        outRect.top = pxTop;
                    }
                    if (lastPosition) {
                        outRect.bottom = pxBottom;
                    }
                }
                break;
            default:
                throw new IllegalStateException("There is no such orientation mode. " +
                        "Please, choose from Orientation.HORIZONTAL or Orientation.VERTICAL values. " +
                        "Or choose nothing to default Orientation.VERTICAL value.");
        }
    }

}