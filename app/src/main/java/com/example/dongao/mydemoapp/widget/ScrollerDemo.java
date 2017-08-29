package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishzhang on 2017/8/28.
 */

public class ScrollerDemo extends View {

    private Scroller scroller;
    private Paint mPaint;
    private VelocityTracker velocityTracker;
    private int maxVelocity;
    private float offset;
    private float lastX;
    private float maxLength=5000;
    private float maxOffset;
    private PointF centerP;
    private RectF viewArea;
    private List<PointF> pointFs;
    private float orientationX;


    public ScrollerDemo(Context context) {
        this(context,null);
    }

    public ScrollerDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(50);
        mPaint.setShadowLayer(10,10,10, Color.LTGRAY);
        setLayerType(LAYER_TYPE_SOFTWARE,mPaint);

        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        scroller=new Scroller(context);

        pointFs=new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewArea = new RectF(0,0,w,h);
        maxOffset=maxLength-viewArea.right;
        centerP = new PointF(w/2,h/2);

        for (int i = 0; i < maxLength / 500; i++) {
            pointFs.add(new PointF(i*500,centerP.y));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        canvas.save();
        canvas.translate(offset,0);
        for (int i = 0; i < pointFs.size(); i++) {
            PointF pointF = pointFs.get(i);
            if (pointF.x>=Math.abs(offset) && pointF.x<=Math.abs(offset)+viewArea.right){
                canvas.drawText("hh"+i,pointF.x,pointF.y,mPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=event.getX();
                initOrResetVeloctiry();
                velocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                lastX=event.getX(0);
                break;
            case MotionEvent.ACTION_MOVE:
                orientationX = event.getX() - lastX;
                onScroll(orientationX);
                lastX=event.getX();
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int minID = event.getPointerId(0);
                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (event.getPointerId(i)<=minID){
                        minID=event.getPointerId(i);
                    }
                }
                if (event.getPointerId(event.getActionIndex())==minID){
                    minID=event.getPointerId(event.getActionIndex()+1);
                }
                lastX=event.getX(event.findPointerIndex(minID));
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000,maxVelocity);
                int velocityX= (int) velocityTracker.getXVelocity();
                velocityTracker.clear();
                if ( offset != 0  && Math.abs(offset) != maxOffset ){
                    scroller.fling((int)event.getX(),(int)event.getY(),velocityX/2,0,Integer.MIN_VALUE,Integer.MAX_VALUE,0,0);
                    invalidate();
                }
                lastX=event.getX();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void onScroll(float x) {
        offset+=x;
        offset=offset > 0 ? 0 : Math.abs(offset) > maxOffset ? -maxOffset : offset;
        invalidate();
    }

    private void initOrResetVeloctiry() {
        if (velocityTracker ==null ){
            velocityTracker=VelocityTracker.obtain();
        }else{
            velocityTracker.clear();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            onScroll(scroller.getCurrX()-lastX);
            lastX=scroller.getCurrX();
            postInvalidate();
        }
    }
}
