package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by xd on 2017/4/14.
 */

public class MyViewPager extends ViewGroup {
    private Scroller scroller;
    private int touchSlop;
    private int left;
    private int right;
    private float downX;


    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
        touchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView=getChildAt(i);
            childView.measure(widthMeasureSpec,heightMeasureSpec);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=x;
                break;
            case MotionEvent.ACTION_MOVE:
                float absx = Math.abs(x - downX);
                downX=x;
                if (absx>touchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                int index =(getScrollX() + getWidth()/2) / getWidth();
                int dist = index * getWidth() - getScrollX();
                scroller.startScroll(getScrollX(),0,dist,0,200);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX= (int) (downX-x);
                if (getScrollX() + scrollX <left){
                    scrollTo(left,0);
                    return true;
                }else if (getScrollX() + getWidth() + scrollX > right){
                    scrollTo(right-getWidth(),0);
                    return true;
                }
                scrollBy(scrollX,0);
                downX=x;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i*childView.getMeasuredWidth(),0
                    ,childView.getMeasuredWidth()+i*childView.getMeasuredWidth()
                    ,childView.getMeasuredHeight());
            right+=childView.getMeasuredWidth();
        }
    }
}
