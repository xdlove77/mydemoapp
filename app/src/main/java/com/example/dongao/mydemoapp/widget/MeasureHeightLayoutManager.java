package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xd on 2017/7/17.
 */

public class MeasureHeightLayoutManager extends LinearLayoutManager {


    public MeasureHeightLayoutManager(Context context) {
        super(context);
    }

    public MeasureHeightLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (state.getItemCount()>0){
            int height=0;
            if (getOrientation() == VERTICAL){
                for (int i = 0; i < state.getItemCount(); i++) {
                    View childView = recycler.getViewForPosition(i);
                    measureChild(childView, View.MeasureSpec.getSize(widthSpec),View.MeasureSpec.getSize(heightSpec));
                    height+=childView.getMeasuredHeight();
                }
            }else{
                for (int i = 0; i < state.getItemCount(); i++) {
                    View childView = recycler.getViewForPosition(i);
                    measureChild(childView, View.MeasureSpec.getSize(widthSpec),View.MeasureSpec.getSize(heightSpec));
                    height=Math.max(height,childView.getMeasuredHeight());
                }
            }
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec),height);
        }else{
            super.onMeasure(recycler,state,widthSpec,heightSpec);
        }
    }
}
