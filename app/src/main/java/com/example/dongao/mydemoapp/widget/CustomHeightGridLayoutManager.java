package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xd on 2017/5/27.
 */

public class CustomHeightGridLayoutManager extends GridLayoutManager {
    public CustomHeightGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View view = recycler.getViewForPosition(0);
        int measuredWidth =0;
        int measuredHeight=0;
        int height=0;
        if(view != null){
            measureChild(view, widthSpec, heightSpec);
            measuredWidth = View.MeasureSpec.getSize(widthSpec);
            measuredHeight = view.getMeasuredHeight();
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (i%getSpanCount()==0){
                height+=measuredHeight;
            }
        }
        setMeasuredDimension(measuredWidth, height);


    }
}
