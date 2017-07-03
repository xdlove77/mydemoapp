package com.example.dongao.mydemoapp.widget.bezier;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by zqfly on 2016/10/8.
 */

public class PathMorphBezier extends View implements View.OnClickListener {
    private Paint mPaintBezier;
    private Paint mPaintAuxiliary;
    private Paint mPaintAuxiliaryText;

    private float mAuxiliaryOneX;
    private float mAuxiliaryOneY;
    private float mAuxiliaryTwoX;
    private float mAuxiliaryTwoY;

    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;

    private Path mPath;
    private ValueAnimator animator;

    public PathMorphBezier(Context context) {
        super(context);
    }

    public PathMorphBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath=new Path();
        mPaintBezier=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStrokeWidth(8);
        mPaintBezier.setStyle(Paint.Style.STROKE);

        mPaintAuxiliary=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliary.setStrokeWidth(2);
        mPaintAuxiliary.setStyle(Paint.Style.STROKE);

        mPaintAuxiliaryText=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliaryText.setStyle(Paint.Style.STROKE);
        mPaintAuxiliaryText.setTextSize(20);

        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStartX=w/4;
        mStartY=h/2-300;
        mEndX=w/4*3;
        mEndY=h/2-300;

        mAuxiliaryOneX=mStartX;
        mAuxiliaryOneY=mStartY;
        mAuxiliaryTwoX=mEndX;
        mAuxiliaryTwoY=mEndY;

        animator=ValueAnimator.ofFloat(mStartY,h);
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAuxiliaryOneY= (float) animation.getAnimatedValue();
                mAuxiliaryTwoY= (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartX,mStartY);
        canvas.drawText("起始点",mStartX,mStartY,mPaintAuxiliaryText);
        canvas.drawText("终止点",mEndX,mEndY,mPaintAuxiliaryText);
        canvas.drawText("控制点1",mAuxiliaryOneX,mAuxiliaryOneY,mPaintAuxiliaryText);
        canvas.drawText("控制点2",mAuxiliaryTwoX,mAuxiliaryTwoY,mPaintAuxiliaryText);

        canvas.drawLine(mStartX,mStartY,mAuxiliaryOneX,mAuxiliaryOneY,mPaintAuxiliary);
        canvas.drawLine(mEndX,mEndY,mAuxiliaryTwoX,mAuxiliaryTwoY,mPaintAuxiliary);
        canvas.drawLine(mAuxiliaryOneX,mAuxiliaryOneY,mAuxiliaryTwoX,mAuxiliaryTwoY,mPaintAuxiliary);

        mPath.cubicTo(mAuxiliaryOneX,mAuxiliaryOneY,mAuxiliaryTwoX,mAuxiliaryTwoY,mEndX,mEndY);
        canvas.drawPath(mPath,mPaintBezier);

    }

    @Override
    public void onClick(View v) {
        animator.start();
    }
}
