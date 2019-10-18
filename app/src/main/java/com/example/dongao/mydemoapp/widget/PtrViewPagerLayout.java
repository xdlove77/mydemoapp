package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class PtrViewPagerLayout extends ViewGroup implements NestedScrollingParent2 {


    private RecyclerView rv;
    private Scroller scroller;

    public PtrViewPagerLayout(Context context) {
        this(context, null);
    }

    public PtrViewPagerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrViewPagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof RecyclerView) {
                this.rv = (RecyclerView) child;
                return;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == rv){
                child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
            if (i == childCount - 1) {
                child.layout(getMeasuredWidth(),0,child.getMeasuredWidth(),child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        if (axes == ViewCompat.SCROLL_AXIS_HORIZONTAL && target instanceof RecyclerView ){
            if (!rv.canScrollHorizontally(1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        scroller.startScroll(getScrollX(),0,-getScrollX(),0);
        invalidate();
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dxUnconsumed != 0 && !rv.canScrollHorizontally(1)){
            scrollTo(getScrollX()+dxUnconsumed,0);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (getScrollX() != 0 && rv.canScrollHorizontally(1)){
            scrollTo(getScrollX()+dx,0);
            consumed[0] = dx;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
        }
    }
}
