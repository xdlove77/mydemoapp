package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by xd on 2017/5/15.
 */

public class SurfaceDan extends LinearLayout {

    private SurfaceView surfaceView;

    public SurfaceDan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        surfaceView = new SurfaceView(context);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceView.setLayoutParams(lp);
        setOrientation(VERTICAL);
        addView(surfaceView);

        SurfaceHolder holder = surfaceView.getHolder();
        Canvas canvas = holder.lockCanvas();

    }




}
