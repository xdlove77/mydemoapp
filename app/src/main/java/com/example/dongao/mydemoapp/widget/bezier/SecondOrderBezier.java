package com.example.dongao.mydemoapp.widget.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zqfly on 2016/9/30.
 */

public class SecondOrderBezier extends View {
    private Paint mPaintBezier;
    private Paint mPaintAuxiliary;
    private Paint mPaintAuxiliaryText;

    private float mAuxiliaryX;
    private float mAuxiliaryY;

    private float mStartPointX;
    private float mStartPointY;

    private float mEndPointX;
    private float mEndPointY;

    private Path mPath;

    public SecondOrderBezier(Context context) {
        super(context);

    }

    public SecondOrderBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintBezier=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStyle(Paint.Style.STROKE);
        mPaintBezier.setStrokeWidth(8);

        mPaintAuxiliary=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliary.setStyle(Paint.Style.STROKE);
        mPaintAuxiliary.setStrokeWidth(2);

        mPaintAuxiliaryText=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliaryText.setStrokeWidth(20);
        mPaintAuxiliaryText.setTextSize(20);

        mPath=new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPointX=w/4;
        mStartPointY=h/2-200;

        mEndPointX=w/4*3;
        mEndPointY=h/2-200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartPointX,mStartPointY);

        canvas.drawPoint(mAuxiliaryX,mAuxiliaryY,mPaintAuxiliary);
        canvas.drawText("控制点",mAuxiliaryX,mAuxiliaryY,mPaintAuxiliaryText);
        canvas.drawText("起始点",mStartPointX,mStartPointY,mPaintAuxiliaryText);
        canvas.drawText("结束点",mEndPointX,mEndPointY,mPaintAuxiliaryText);

        canvas.drawLine(mStartPointX,mStartPointY,mAuxiliaryX,mAuxiliaryY,mPaintAuxiliary);
        canvas.drawLine(mEndPointX,mEndPointY,mAuxiliaryX,mAuxiliaryY,mPaintAuxiliary);

        mPath.quadTo(mAuxiliaryX,mAuxiliaryY,mEndPointX,mEndPointY);

        canvas.drawPath(mPath,mPaintBezier);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (action){
            case MotionEvent.ACTION_MOVE:
                mAuxiliaryX=rawX;
                mAuxiliaryY=rawY;
                invalidate();
                break;
        }

        return true;
    }
}
