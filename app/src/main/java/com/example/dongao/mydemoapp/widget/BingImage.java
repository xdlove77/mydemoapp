package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.dongao.mydemoapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xd on 2017/5/4.
 */

public class BingImage extends View {
    private List<BingBean> bingBeenList;
    private float centerWhiteCircleRadius;
    private int circleStrokeWidth=10;
    private float circleRadius;
    private float scale=0.6f;
    private int width,height;
    private Paint mPaint;
    private Paint xcirclePaint;
    private Paint textPaint;
    private float startAngle=270;
    private float angle=360f;
    private RectF rectF;
    private PointF center;
    private float rcircleRadius;
    private int xRadius=10;
    private int slashLength=40;
    private int leftAndRightPadding=30;

    public BingImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(circleStrokeWidth);

        xcirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        xcirclePaint.setStyle(Paint.Style.FILL);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);
        float v1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics());
        float v = context.getResources().getDisplayMetrics().density*10 + 0.5f;
        textPaint.setTextSize(v);
        if (isInEditMode()){
            bingBeenList=new ArrayList<>();
            bingBeenList.add(new BingImage.BingBean(Color.BLACK,0.4f,"审计","gg"));
            bingBeenList.add(new BingImage.BingBean(Color.BLUE,0.1f,"会计","10min"));
            bingBeenList.add(new BingImage.BingBean(Color.GRAY,0.25f,"经济法","20min"));
            bingBeenList.add(new BingImage.BingBean(Color.GREEN,0.05f,"税法","hahaha"));
            bingBeenList.add(new BingImage.BingBean(Color.LTGRAY,0.2f,"哈哈","嘿嘿嘿"));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        center = new PointF(w/2,h/2);
        rectF=new RectF();
        if (w>h){
            circleRadius=scale*h/2;
            rectF.left=center.x-circleRadius;
            rectF.top=(h-circleRadius*2)/2;
            rectF.right=center.x+circleRadius;
            rectF.bottom=h-(h-circleRadius*2)/2;
        }else{
            circleRadius=scale*w/2;
            rectF.left=center.x-circleRadius;
            rectF.top=(w-circleRadius*2)/2;
            rectF.right=center.x+circleRadius;
            rectF.bottom=w-(w-circleRadius*2)/2;
        }
        rcircleRadius =circleRadius+20;
        centerWhiteCircleRadius=circleRadius*0.4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bingBeenList !=null && bingBeenList.size()>0){
            for (BingBean bingBean : bingBeenList) {

                //画百分比圆
                mPaint.setColor(bingBean.getColor());
                float sweepAngle = bingBean.getPercent() * angle;
                canvas.drawArc(rectF,startAngle,sweepAngle,true,mPaint);

                //画外围的小圆
                xcirclePaint.setColor(bingBean.getColor());
                float cangle = sweepAngle / 2 + startAngle;
                float circleAngle=cangle==angle?0
                        : cangle <angle? cangle
                        : cangle-angle;
                float x=0,y=0;
                if (circleAngle >=0 && circleAngle <=90){
                    x= (float) (center.x+Math.cos(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                    y= (float) (center.y+Math.sin(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                }else if (circleAngle > 90 && circleAngle <= 180){
                    x= (float) (center.x+Math.cos(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                    y= (float) (center.y+Math.sin(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                }else if (circleAngle > 180 && circleAngle <= 270){
                    x= (float) (center.x+Math.cos(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                    y= (float) (center.y+Math.sin(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                }else if (circleAngle > 270 && circleAngle < angle){
                    x= (float) (center.x+Math.cos(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                    y= (float) (center.y+Math.sin(Math.PI * circleAngle / 180 )*(rcircleRadius+mPaint.getStrokeWidth()/2));
                }
                canvas.drawCircle(x,y, xRadius,xcirclePaint);

                //设置百分比圆的偏移角度
                startAngle=sweepAngle+startAngle==angle?0
                        : sweepAngle+startAngle<angle?sweepAngle+startAngle
                        : sweepAngle+startAngle-angle;

                //画斜线
                float endX=0,endY=0;
                if (y<center.y){
                    if (x>center.x){
                        float length = (float) Math.sqrt(slashLength * slashLength / 2);
                        endX=x+length;
                        endY=y-length;
                        canvas.drawLine(x,y,endX,endY,xcirclePaint);
                    }else{
                        float length = (float) Math.sqrt(slashLength * slashLength / 2);
                        endX=x-length;
                        endY=y-length;
                        canvas.drawLine(x,y,endX,endY,xcirclePaint);
                    }
                }else{
                    if (x>center.x){
                        float length = (float) Math.sqrt(slashLength * slashLength / 2);
                        endX=x+length;
                        endY=y+length;
                        canvas.drawLine(x,y,endX,endY,xcirclePaint);
                    }else{
                        float length = (float) Math.sqrt(slashLength * slashLength / 2);
                        endX=x-length;
                        endY=y+length;
                        canvas.drawLine(x,y,endX,endY,xcirclePaint);
                    }
                }

                //画横线和字

                float textHight = textPaint.getTextSize();
                String description = bingBean.getDescription();
                String name = bingBean.getName();
                if (endX<center.x){
                    canvas.drawLine(endX,endY,leftAndRightPadding,endY,xcirclePaint);
                    float contentSize = endX - leftAndRightPadding;
                    float textSize = textPaint.measureText(description)+leftAndRightPadding+textPaint.measureText("...");
                    if (contentSize < textSize){
                        float size = textSize - contentSize ;
                        float ysize = size / textPaint.getTextSize();
                        int num= ysize > 1? Math.round(ysize) :1;
                        description=description.substring(0,description.length()-num)+"...";
                    }
                    canvas.drawText(description,leftAndRightPadding,endY-textHight+textHight/2,textPaint);
                    canvas.drawText(name,leftAndRightPadding,endY+textHight+5,textPaint);
                }else{
                    canvas.drawLine(endX,endY,width-leftAndRightPadding,endY,xcirclePaint);
                    float contentSize = width - leftAndRightPadding - endX;
                    float textSize = textPaint.measureText(description)+leftAndRightPadding+textPaint.measureText("...");
                    if (contentSize < textSize){
                        float size = textSize - contentSize ;
                        float ysize = size / textPaint.getTextSize();
                        int num= ysize > 1? Math.round(ysize) :1;
                        description=description.substring(0,description.length()-num)+"...";
                    }
                    float nameLength = textPaint.measureText(name);
                    float desLength = textPaint.measureText(description);
                    canvas.drawText(description,width-leftAndRightPadding-desLength,endY-textHight+textHight/2,textPaint);
                    canvas.drawText(name,width-leftAndRightPadding-nameLength,endY+textHight+5,textPaint);
                }
            }
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(center.x,center.y,centerWhiteCircleRadius,mPaint);
        }
    }

    public void setData(List<BingBean> data){
        if (data == null || data.size()<=0){
            throw new NullPointerException();
        }
        bingBeenList=data;
        invalidate();
    }

    public static class BingBean{
        private int color;
        private float percent;
        private String name;
        private String description;

        public BingBean(int color, float percent,String name,String description) {
            this.color = color;
            this.percent = percent;
            this.name=name;
            this.description=description;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
