package com.example.dongao.mydemoapp.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fishzhang on 2017/10/17.
 */

public class LeiDaView extends View {
    private static final int J=1;
    private static final int O=2;
    private static final int N=0;
    private int type=O;
    private int screenW;
    private int screenH;
    private int withoutCount=5;
    private Paint linePaint;
    private Paint bgPaint;
    private Paint textPaint;
    private Paint dataPaint;
    private Paint dataBGPaint;
    private Paint pointPaint;
    private float diameter;
    private float distTextPadding=10;
    List<LeiDaData> datas;
    private float textLength=0;
    private RectF area;
    private SparseArray<List<PointF>> map;
    private float p2pAngle;
    private boolean notNeedDraw;
    private DisplayMetrics displayMetrics;


    public LeiDaView(Context context) {
        this(context,null);
    }

    public LeiDaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        displayMetrics = context.getResources().getDisplayMetrics();
        distTextPadding=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,7,displayMetrics);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,14, displayMetrics));

        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#b2afb7"));
        linePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0.5f, displayMetrics));
        linePaint.setStyle(Paint.Style.STROKE);

        bgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#f6f6f6"));
        bgPaint.setStyle(Paint.Style.FILL);

        dataPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dataPaint.setColor(Color.parseColor("#ff8d34"));
        dataPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1.5f, displayMetrics));
        dataPaint.setStyle(Paint.Style.STROKE);

        dataBGPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dataBGPaint.setColor(Color.parseColor("#66ff8d34"));
        dataBGPaint.setStyle(Paint.Style.FILL);

        pointPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.parseColor("#ff8d34"));
        pointPaint.setStyle(Paint.Style.FILL);

        map=new SparseArray<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW=w;
        screenH=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (datas==null || datas.isEmpty())
            return;
        if (!notNeedDraw){
            measureDiameter();
            computeAllPoint();
        }
        drawRound(canvas);
        drawText(canvas);
        drawDataArea(canvas);
    }

    private void drawDataArea(Canvas canvas) {
        List<PointF> dataPoint=new ArrayList<>();
        float angle=90;
        float nAngle=angle;
        for (int i = 0; i < datas.size(); i++) {
            LeiDaData leiDaData = datas.get(i);
            dataPoint.add(getOnPoint(nAngle,leiDaData.getP()*leiDaData.getPrecent()*(diameter/2)));
            angle-=p2pAngle;
            if (angle<0)
                nAngle=360+angle;
            else
                nAngle=angle;
        }
        Path path=new Path();

        for (int i = 0; i < dataPoint.size(); i++) {
            PointF datap = dataPoint.get(i);
            canvas.drawCircle(datap.x,datap.y,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3,displayMetrics),pointPaint);
            if (i==0) {
                path.moveTo(datap.x,datap.y);
                continue;
            }
            path.lineTo(datap.x,datap.y);
        }
        path.close();
        canvas.drawPath(path,dataBGPaint);
        canvas.drawPath(path,dataPaint);
    }

    private void drawText(Canvas canvas) {
        List<PointF> textPoints = roundPoint(datas.size(), p2pAngle, diameter / 2 + distTextPadding);
        for (int i=0;i<datas.size();i++) {
            PointF tp = textPoints.get(i);
            LeiDaData leiDaData = datas.get(i);
            float x=tp.x;
            float y=tp.y;
            if (area.centerX()==tp.x){
                x=tp.x-textPaint.measureText(leiDaData.getName())/2;
                if (area.centerY()<tp.y){
                    y=tp.y+textPaint.getTextSize();
                }
            }else if (area.centerX() > tp.x){
                x=tp.x-textPaint.measureText(leiDaData.getName());
            }
            if (area.centerX()!=tp.x){
                y=tp.y+textPaint.getTextSize()/2;
            }
            canvas.drawText(leiDaData.getName(),x,y,textPaint);
        }
        notNeedDraw =true;
    }

    private void drawRound(Canvas canvas) {
        for (int i = 0; i < map.size(); i++) {
            Path path=new Path();
            List<PointF> points = map.get(i);
            for (int j = 0; j < points.size(); j++) {
                PointF point = points.get(j);
                if (j==0){
                    path.moveTo(point.x,point.y);
                    continue;
                }
                path.lineTo(point.x,point.y);
            }
            path.close();
            if (type == O ){
                if ((i+1)%2==0){
                    bgPaint.setColor(Color.parseColor("#f6f6f6"));
                }else{
                    bgPaint.setColor(Color.WHITE);
                }
            }else if (type ==J){
                if ((i+1)%2!=0){
                    bgPaint.setColor(Color.parseColor("#f6f6f6"));
                }else {
                    bgPaint.setColor(Color.WHITE);
                }
            }
            canvas.drawPath(path,bgPaint);
            canvas.drawPath(path,linePaint);
        }
        List<PointF> outPoints = map.get(0);
        for (int i = 0; i < outPoints.size(); i++) {
            canvas.drawLine(area.centerX(),area.centerY(),outPoints.get(i).x,outPoints.get(i).y,linePaint);
        }
    }

    private void computeAllPoint() {
        p2pAngle = 1.0f*360/datas.size();
        for (int i = 0; i < withoutCount; i++) {
            map.put(i,roundPoint(datas.size(), p2pAngle,diameter/2*(1f-i*0.2f)));
        }
    }

    private List<PointF> roundPoint(int pCount, float p2pAngle,float radius) {
        List<PointF> points=new ArrayList<>();
        float angle=90;
        float nAngle=angle;
        for (int i = 0; i < pCount; i++) {
            points.add(getOnPoint(nAngle,radius));
            angle-=p2pAngle;
            if (angle<0)
                nAngle=360+angle;
            else
                nAngle=angle;
        }
        return points;
    }

    private PointF getOnPoint(float angle,float radius){
        PointF pointF=new PointF();
        pointF.x= (float) (area.centerX()+Math.cos(Math.PI*angle/180)*radius);
        pointF.y= (float) (area.centerY()-Math.sin(Math.PI*angle/180)*radius);
        return pointF;
    }


    private void measureDiameter() {
        float distTop=getPaddingTop()+distTextPadding+textPaint.getTextSize();
        float distBottom=getPaddingBottom()+distTextPadding+textPaint.getTextSize();
        float distLeft=getPaddingLeft()+distTextPadding+textLength;
        float distRight=getPaddingRight()+distTextPadding+textLength;
        float height = screenH - distBottom - distTop;
        float width=screenW-distLeft-distRight;
        if (width>height){
            diameter=height;
            float cValue =width-height;
            area=new RectF(distLeft+cValue/2,distTop,distLeft+cValue/2+height,distTop+height);
        }else{
            diameter=width;
            float cValue=height-width;
            area=new RectF(distLeft,distTop+cValue/2,distLeft+width,distTop+cValue/2+width);
        }
    }

    public void setData(List<LeiDaData> datas){
        feedInternal(datas);
        if (this.datas==null || this.datas.isEmpty())
            return;
        notNeedDraw=false;
        postInvalidate();
    }

    public void setDataWithAnim(List<LeiDaData> datas){
        feedInternal(datas);
        if (this.datas==null || this.datas.isEmpty())
            return;
        notNeedDraw=false;
        for (int i = 0; i < datas.size(); i++) {
            final int index=i;
            ValueAnimator animator=ValueAnimator.ofFloat(0f,1f);
            animator.setDuration(1000);
            animator.setStartDelay(i*200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LeiDaData leiDaData = LeiDaView.this.datas.get(index);
                    leiDaData.setP((float) animation.getAnimatedValue());
                    postInvalidate();
                }
            });
            animator.start();
        }
    }

    private void feedInternal(List<LeiDaData> datas){
        if (datas == null || datas.isEmpty()) {
            return;
        }
        if (this.datas==null)
            this.datas=new ArrayList<>();
        this.datas.clear();
        this.datas.addAll(datas);
        for (LeiDaData data : datas) {
            float v = textPaint.measureText(data.getName());
            textLength=Math.max(textLength,v);
        }
    }

    public static class LeiDaData{
        private String name;
        private float precent;
        private float p=0f;

        public LeiDaData() {
        }

        public LeiDaData(String name, float precent) {
            this.name = name;
            this.precent = precent;
        }

        public float getP() {
            return p;
        }

        public void setP(float p) {
            this.p = p;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getPrecent() {
            return precent;
        }

        public void setPrecent(float precent) {
            this.precent = precent;
        }
    }

}
