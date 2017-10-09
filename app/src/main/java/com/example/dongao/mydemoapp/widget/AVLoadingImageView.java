package com.example.dongao.mydemoapp.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishzhang on 2017/9/29.
 */

public class AVLoadingImageView extends View {
    private static final int DEFULT_COLOR= Color.LTGRAY;
    private static final float SCALE=0f;
    private static final long ANIM_S=1000;
    private int itemCount=4;
    private int itemWidth=0;
    private int itemMaxHeight=0;
    private float[] scale=new float[]{SCALE,SCALE,SCALE,SCALE};
    private long[] delays=new long[]{ANIM_S/2*2/6,ANIM_S/2*1/6,0,ANIM_S/2*3/6};
    private List<ValueAnimator> animators=new ArrayList<>();
    private Paint mPaint;

    public AVLoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(DEFULT_COLOR);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemMaxHeight=h;
        itemWidth=w/(itemCount*2-1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left=0;
        for (int i = 0; i < itemCount; i++) {
            RectF rectF=new RectF(left,itemMaxHeight-itemMaxHeight*scale[i],left+itemWidth,itemMaxHeight);
            canvas.drawRoundRect(rectF,3,3,mPaint);
            left+=itemWidth*2;
        }
    }

    public void startAnim(){
        if (animators.isEmpty()){
            for (int i = 0; i < itemCount; i++) {
                final int index=i;
                ValueAnimator va=ValueAnimator.ofFloat(0f,1f,0f);
                va.setStartDelay(delays[index]);
                va.setRepeatCount(ValueAnimator.INFINITE);
                va.setDuration(ANIM_S);
                va.setInterpolator(new LinearInterpolator());
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        scale[index]=(float)animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                va.start();
                animators.add(va);
            }
        }
    }

    public void stopAnim(){
        if (!animators.isEmpty()){
            for (ValueAnimator va : animators) {
                if (va!=null && va.isStarted()){
                    va.removeAllUpdateListeners();
                    va.cancel();
                }
            }
            animators.clear();
        }
    }

}
