package com.example.dongao.mydemoapp.widget.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by zqfly on 2016/9/30.
 */

public class DrawPadBezier extends View {

    private Path mPath;
    private int slop=ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private float lmovex;
    private float lmovey;
    private float mx;
    private float my;
    private Paint mPaint;

    public DrawPadBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath=new Path();
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x,y);
                lmovex=x;
                lmovey=y;
                break;
            case MotionEvent.ACTION_MOVE:
                float ax = Math.abs(x - lmovex);
                float ay = Math.abs(y - lmovey);
                if (ax >slop || ay >slop){
                    float hfx=(x+lmovex)/2;
                    float hfy=(y+lmovey)/2;
                    mPath.quadTo(lmovex,lmovey,hfx,hfy);
                    invalidate();
                    lmovex=x;
                    lmovey=y;
                }
                break;
        }

        return true;
    }

}
