package com.example.dongao.mydemoapp.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

/**
 * Created by xd on 2017/4/12.
 */

public class MyLoadMoreScrollview extends LinearLayout implements NestedScrollingParent{
    private Context context;
    private NestedScrollingParentHelper helper;
    private LinearLayout headerView;
    private LinearLayout footerView;
    private MyRecycleView contentView;
    private boolean fling;
    private boolean canRefresh=true;
    private boolean canLoadMore=true;
    private int totalY;

    public MyLoadMoreScrollview(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public MyLoadMoreScrollview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        helper=new NestedScrollingParentHelper(this);
        contentView=new MyRecycleView(context);
        headerView=new LinearLayout(context);
        headerView.setOrientation(VERTICAL);
        headerView.setBackgroundColor(Color.GRAY);
        addView(contentView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(headerView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, totalY));
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
        if (dy > 0 && !contentView.isCanPullUp()) {
            fling=true;
            totalY += dy;
            if (totalY/3 >= 0){
                headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,totalY/2));
                scrollTo(0,totalY/3);
                consumed[1]=dy;
            }
        }

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onStopNestedScroll(View child) {
        if (fling ){
            fling=false;
            ValueAnimator viewAnimator=ValueAnimator.ofInt(0,totalY/3);
            viewAnimator.setInterpolator(new LinearInterpolator());
            viewAnimator.setDuration(200);
            viewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue= (int) animation.getAnimatedValue();
                    headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,totalY/2-animatedValue));
                    scrollTo(0,totalY/3-animatedValue);
                }
            });
            viewAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    totalY=0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            viewAnimator.start();
        }
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
