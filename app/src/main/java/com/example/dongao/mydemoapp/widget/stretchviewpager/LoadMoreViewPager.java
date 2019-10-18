package com.example.dongao.mydemoapp.widget.stretchviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LoadMoreViewPager extends ViewPager{
    private static final int STATUS_PULL=0x00000001;
    private static final int STATUS_RELEASE=0x00000010;
    private static final int STATUS_LOADING=0x00000100;
    private static final int STATUS_END=0x00001000;
    private static final int STATUS_DONE=0x00010000;

    private int loadMode=STATUS_END;

    private View loadMoreView;

    public LoadMoreViewPager(Context context) {
        super(context);
    }

    public LoadMoreViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void setLoadMoreView(View loadMoreView){
        this.loadMoreView =loadMoreView;
    }

    public void setLoadMode(int loadMode) {
        this.loadMode = loadMode;
    }
}
