package com.example.dongao.mydemoapp.widget.bezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by zqfly on 2016/10/8.
 */

public class WaveBezier extends View implements View.OnClickListener {

    private int waveCount;
    private int waveLength=1000;
    private int centerY;
    private int screenWidth;
    private int screenHeight;
    private Paint mPaint;
    private int offset;
    private Path mPath;

    public WaveBezier(Context context) {
        this(context,null);
    }

    public WaveBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.BLUE);
        mPath=new Path();
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenHeight=h;
        screenWidth=w;
        centerY=h/2;
        waveCount= (int) Math.round(1.0f*screenWidth/waveLength+1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(-waveLength+offset,centerY);
        for (int i = 0; i <waveCount ; i++) {
            mPath.quadTo(-waveLength*3/4+offset+i*waveLength,centerY+60
                    ,-waveLength/2+offset+i*waveLength,centerY);
            mPath.quadTo(-waveLength*1/4+offset+i*waveLength,centerY-60
                    ,offset+i*waveLength,centerY);
        }
        mPath.lineTo(screenWidth,screenHeight);
        mPath.lineTo(0,screenHeight);
        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public void onClick(View v) {
        ValueAnimator animator=ValueAnimator.ofInt(0,waveLength);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}
