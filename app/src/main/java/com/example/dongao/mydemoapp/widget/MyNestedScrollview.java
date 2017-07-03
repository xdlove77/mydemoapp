package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by xd on 2017/4/12.
 */

public class MyNestedScrollview extends LinearLayout implements NestedScrollingParent{
    private Context context;
    private NestedScrollingParentHelper helper;
    private LinearLayout headerView;
    private LinearLayout footerView;
    private MyRecycleView contentView;
    private boolean fling;
    private boolean canRefresh=true;
    private boolean canLoadMore=true;
    private int totalY;

    public MyNestedScrollview(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public MyNestedScrollview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        helper=new NestedScrollingParentHelper(this);
        contentView=new MyRecycleView(context);
        headerView=new LinearLayout(context);
        footerView=new LinearLayout(context);
        headerView.setOrientation(VERTICAL);
        footerView.setOrientation(VERTICAL);
        headerView.setBackgroundColor(Color.GRAY);
        footerView.setBackgroundColor(Color.LTGRAY);
        addView(headerView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
        addView(footerView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        addView(contentView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setRVLayoutManager(RecyclerView.LayoutManager layoutManager,RecyclerView.Adapter adapter){
        contentView.setMyLayoutManager(layoutManager);
        contentView.setAdapter(adapter);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && contentView.isCanPullUp() && totalY < 100) {
            fling=true;
            totalY += dy;
            if (totalY >= 100){
                totalY=100;
                scrollTo(0,totalY);
                consumed[1]=dy;
                fling=false;
                return;
            }
            scrollBy(0,dy);
            consumed[1]=dy;
        }

        if (dy < 0 && totalY > 0){
            fling=true;
            totalY += dy;
            if (totalY <= 0){
                totalY=0;
                scrollTo(0,totalY);
                consumed[1]=dy;
                fling=false;
                return;
            }
            scrollBy(0,dy);
            consumed[1]=dy;

        }

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return fling;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return fling;
    }

    public static class MyRecycleView extends RecyclerView{

        private LinearLayoutManager linearLayoutManager;
        private StaggeredGridLayoutManager staggeredGridLayoutManager;
        private GridLayoutManager gridLayoutManager;

        public MyRecycleView(Context context) {
            super(context);
            setOverScrollMode(OVER_SCROLL_NEVER);
            setHorizontalFadingEdgeEnabled(false);
            setVerticalFadingEdgeEnabled(false);
            setHorizontalScrollBarEnabled(false);
            setVerticalScrollBarEnabled(false);
            setItemAnimator(new DefaultItemAnimator());
        }

        private void setMyLayoutManager(LayoutManager layoutManager) {
            if (layoutManager instanceof LinearLayoutManager) {
                linearLayoutManager= (LinearLayoutManager) layoutManager;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                staggeredGridLayoutManager= (StaggeredGridLayoutManager) layoutManager;
            } else if (layoutManager instanceof GridLayoutManager) {
                gridLayoutManager= (GridLayoutManager) layoutManager;
            }
            setLayoutManager(layoutManager);
            if (!isVertical()){
                throw new RuntimeException("");
            }
        }

        private boolean isVertical() {
            if (linearLayoutManager!=null){
                return linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL;
            }else if (staggeredGridLayoutManager!=null){
                return staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL;
            }else if (gridLayoutManager!=null){
                return staggeredGridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL;
            }
            return false;
        }

        private boolean isCanPullUp() {
            return canScrollVertically(1);
        }

        private boolean isCanPullDown() {
            return canScrollVertically(-1);
        }
    }

}
