package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqfly on 2016/12/12.
 */

public class FlowLayout extends ViewGroup {
    private Context context;
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width=0;
        int height=0;
        int lineWidth=0;
        int lineHeight=0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp= (MarginLayoutParams) childView.getLayoutParams();
            int childWidth=lp.leftMargin+lp.rightMargin+childView.getMeasuredWidth();
            int childHeight=lp.topMargin+lp.bottomMargin+childView.getMeasuredHeight();
            if (childWidth + lineWidth > widthSize){
                width=Math.max(lineWidth,childWidth);
                height+=lineHeight;
                lineWidth=0;
                lineHeight=childHeight;
            }
            lineWidth+=childWidth;
            lineHeight=Math.max(lineHeight,childHeight);

        }
        width=Math.max(lineWidth,width);
        height+=lineHeight;

        setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:width
                ,heightMode==MeasureSpec.EXACTLY?heightSize:height);

    }

    private List<List<View>> views=new ArrayList<>();
    private List<Integer> lineHeights=new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int lineWidth=0;
        int lineHeight=0;
        List<View> lineViews=new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) childView.getLayoutParams();
            int childWidth=lp.leftMargin+lp.rightMargin+childView.getMeasuredWidth();
            int childHeight=lp.topMargin+lp.bottomMargin+childView.getMeasuredHeight();
            if (childWidth + lineWidth >width){
                lineWidth=0;
                views.add(lineViews);
                lineHeights.add(lineHeight);
                lineViews=new ArrayList<>();
                lineHeight=childHeight;
            }
            lineHeight=Math.max(lineHeight,childHeight);
            lineWidth+=childWidth;
            lineViews.add(childView);
        }
        lineHeights.add(lineHeight);
        views.add(lineViews);

        int tops=0,lefts=0;
        for (int i = 0; i < lineHeights.size(); i++) {
            lineViews=views.get(i);
            lineHeight=lineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View childView = lineViews.get(j);
                MarginLayoutParams lp= (MarginLayoutParams) childView.getLayoutParams();
                int top=tops+lp.topMargin;
                int left=lefts+lp.leftMargin;
                int right=left+childView.getMeasuredWidth();
                int bottom=top+childView.getMeasuredHeight();
                childView.layout(left,top,right,bottom);
                lefts=right+lp.rightMargin;
            }
            tops+=lineHeight;
            lefts=0;
        }
    }
}
