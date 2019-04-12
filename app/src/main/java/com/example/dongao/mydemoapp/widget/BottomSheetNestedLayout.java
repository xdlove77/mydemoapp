package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BottomSheetNestedLayout extends FrameLayout implements NestedScrollingParent2 {

    private NestedScrollingParentHelper parentHelper;
    private int maxScrollY;

    public BottomSheetNestedLayout(@NonNull Context context) {
        this(context, null);
    }

    public BottomSheetNestedLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSheetNestedLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        maxScrollY = (int) Math.max(child.getTranslationY(), maxScrollY);
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        parentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        parentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0) {
            int tY = getScrollY() + dyUnconsumed;
            scrollTo(0, tY < 0 ? 0 : tY);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0) {
            int tY = getScrollY() + dy;
            consumed[1] = tY > maxScrollY ? dy - (tY - maxScrollY) : dy;
            scrollTo(0, tY > maxScrollY ? maxScrollY : tY);
        }
    }
}
