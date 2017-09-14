package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishzhang on 2017/9/1.
 */

public class ScrollMenuLayout extends ViewGroup {
    public static final int LEFT=0x000001;
    public static final int RIGHT=0x000100;
    public static final int BOTTOM=0x010000;


    private List<MenuItem> itemList;
    private int type;
    private int touchSlop;
    private Scroller scroller;
    private int leftBorder,rightBorder;
    private int canScrollWidth;
    private float lastX;

    public ScrollMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        itemList=new ArrayList<>();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        scroller=new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            rightBorder+=measuredWidth;
            if (i!=0){
                canScrollWidth+=measuredWidth;
            }

            childView.layout(rightBorder-measuredWidth,0,rightBorder,measuredHeight);
        }
        leftBorder=0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchSlop < Math.abs(x-lastX)){
                    lastX=x;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dx=lastX-x;
                if (getScrollX()+dx < leftBorder){
                    scrollTo(leftBorder,0);
                    return true;
                }else if (getScrollX() +dx > canScrollWidth){
                    scrollTo(canScrollWidth,0);
                    return true;
                }
                scrollBy((int) dx,0);
                lastX=x;
                break;
            case MotionEvent.ACTION_UP:
                int disx=0;
                if (getScrollX() > canScrollWidth/2){
                    disx=canScrollWidth-getScrollX();
                }else{
                    disx=-getScrollX();
                }
                scroller.startScroll(getScrollX(),0,disx,0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    public void setType(int position){
        if (position == LEFT || position == RIGHT || position == (LEFT|BOTTOM) || position == (RIGHT|BOTTOM)){
            type = position;
        }else{
            throw new RuntimeException("type is wrong");
        }
    }

    public void addMenuItem(MenuItem item){
        if (itemList.size() == 3 )
            return;
        itemList.add(item);
    }

    public static class MenuItem {
        private String name;
        private int imgRes;
        private int bgColor;
        private int textColor;

        public MenuItem(String name, int bgColor, int textColor) {
            this.name = name;
            this.bgColor = bgColor;
            this.textColor = textColor;
        }

        public MenuItem(@ColorInt int imgRes, int bgColor) {
            this.imgRes = imgRes;
            this.bgColor=bgColor;
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(@ColorInt int imgRes) {
            this.imgRes = imgRes;
        }

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }
    }
}
