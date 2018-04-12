package com.example.dongao.mydemoapp.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dongao.mydemoapp.R;

/**
 * Created by fishzhang on 2018/3/13.
 */

public class ZhedieView extends View implements View.OnClickListener {

    private Camera camera;
    private int centerX,centerY;
    private Bitmap bitmap;
    private int left,top;
    private Paint mPaint;
    private float degree;
    private float degreeY=30;
    private float degreeFixY;

    public ZhedieView(Context context) {
        this(context,null);
    }

    public ZhedieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        camera = new Camera();
        setOnClickListener(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.aa);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX=w/2;
        centerY=h/2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        left=centerX-width/2;
        top=centerY-height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(centerX,centerY);
        canvas.rotate(-degree);
        camera.save();
        camera.rotateY(-degreeY);
        camera.restore();
        camera.applyToCanvas(canvas);
        canvas.clipRect(0,-centerY,centerX,centerY);
        canvas.rotate(degree);
        canvas.translate(-centerX,-centerY);
        canvas.drawBitmap(bitmap,left,top,mPaint);
        canvas.restore();

        canvas.save();
        camera.save();
        canvas.translate(centerX,centerY);
        canvas.rotate(-degree);
        camera.rotateY(degreeFixY);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-centerX,-centerY,0,centerY);
        canvas.rotate(degree);
        canvas.translate(-centerX,-centerY);
        canvas.drawBitmap(bitmap,left,top,mPaint);
        camera.restore();
        canvas.restore();
    }


    @Override
    public void onClick(View v) {
        ValueAnimator va1=ValueAnimator.ofFloat(0,1);
        va1.setDuration(1000);
        va1.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            degree= animatedValue*270;
            invalidate();
        });
        ValueAnimator va2=ValueAnimator.ofFloat(0,degreeY);
        va2.setDuration(1000);
        va2.addUpdateListener(animation -> {
            degreeFixY = (float) animation.getAnimatedValue();
            invalidate();
        });
//        ValueAnimator va3=ValueAnimator.ofFloat(0,1);
//        va1.setDuration(1000);
//        va1.addUpdateListener(animation -> {
//
//            invalidate();
//        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(va1).before(va2);
        animatorSet.start();
    }
}
