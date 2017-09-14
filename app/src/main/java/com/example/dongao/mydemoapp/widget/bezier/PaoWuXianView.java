package com.example.dongao.mydemoapp.widget.bezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by fishzhang on 2017/9/14.
 */

public class PaoWuXianView extends View implements View.OnClickListener {
    private Paint mPaint;

    private PointF cp;



    public PaoWuXianView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        cp= new PointF(20,20);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cp.x, cp.y,20,mPaint);
    }

    @Override
    public void onClick(View v) {
        ValueAnimator animator=ValueAnimator.ofFloat(0,1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF bezierPoint = getBezierPoint(animation.getAnimatedFraction(), new PointF(20,20), new PointF(200, 100), new PointF(300, 300));
                cp.x=bezierPoint.x;
                cp.y=bezierPoint.y;
                invalidate();
            }
        });
        animator.start();
    }

    //德卡斯特里奥算法
    public PointF getBezierPoint(float t,PointF... pfs){
        PointF[] ps=new PointF[pfs.length-1];

        for (int i = 0; i < ps.length ; i++) {
            ps[i]=new PointF();
        }

        for (int i = 1; i < pfs.length; i++) {
            for (int j = 0; j < pfs.length - i; j++) {
                if (i==1){
                    ps[j].x= pfs[j+1].x*t+(1-t)*pfs[j].x;
                    ps[j].y= pfs[j+1].y*t+(1-t)*pfs[j].y;
                    continue;
                }

                ps[j].x= ps[j+1].x*t+(1-t)*ps[j].x;
                ps[j].y= ps[j+1].y*t+(1-t)*ps[j].y;
            }
        }

        return ps[0];
    }
}
