package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;

import com.example.dongao.mydemoapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xd on 2017/6/20.
 */

public class QulineView extends View {
    public static final int LINE_TYPE_CURVE=1;
    public static final int LINE_TYPE_POLYLINE=2;
    public static final int PADDING=10;
    private Paint mPaint;
    private Paint xyPaint;
    private Paint textPaint;
    private List<Point> ps;
    private int width;
    private int height;
    private int lineType=LINE_TYPE_CURVE;
    private List<String> leftData;
    private float leftDataWidth;
    private ArrayMap<String,Float> datas;
    private float maxValue;
    private LinearGradient linearGradient;


    public QulineView(Context context) {
        this(context,null);
    }

    public QulineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QulineView);

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);

        xyPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        xyPaint.setColor(Color.GRAY);
        xyPaint.setStyle(Paint.Style.STROKE);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setColor(Color.GRAY);
        ps=new ArrayList<>();

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width =w-PADDING;
        height =h;
        float sh=height;
        linearGradient=new LinearGradient(0f,0f,0f,sh,new int[]{ Color.BLUE,Color.GRAY ,Color.BLUE},null, Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        isNoData();
        float ypx=leftDataWidth+10;
        float ypy=height-textPaint.getTextSize()-10;

        float xlenghtScale=(width-ypx)/datas.size();
        xlenghtScale+=xlenghtScale/(datas.size()-1);
        float ylenghtScale=(ypy-PADDING)/maxValue;

        mPaint.setStyle(Paint.Style.STROKE);

        if (lineType == LINE_TYPE_CURVE){
            for (int i = 0; i < datas.size()-1; i++) {
                float svalue = datas.valueAt(i);
                float evalue = datas.valueAt(i+1);
                PointF sp;
                if (i==0){
                    sp=new PointF(xlenghtScale*i+ypx,ypy);
                }else{
                    sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale+PADDING);
                }

                PointF ep=new PointF(xlenghtScale*(i+1)+ypx,ypy-evalue*ylenghtScale+PADDING);
                float wt=(sp.x+ep.x)/2;
                PointF p1=new PointF(wt,sp.y);
                PointF p2=new PointF(wt,ep.y);
                Path path=new Path();
                path.moveTo(sp.x,sp.y);
                path.cubicTo(p1.x,p1.y,p2.x,p2.y,ep.x,ep.y);
                canvas.drawPath(path,mPaint);

                float btx=sp.x;
                canvas.drawText(datas.keyAt(i),btx-textPaint.measureText(datas.keyAt(i))/2,height,textPaint);
                if (i==datas.size()-2){
                    btx=ep.x;
                    canvas.drawText(datas.keyAt(i+1),btx-textPaint.measureText(datas.keyAt(i))/2,height,textPaint);
                }

            }
        }else if (lineType == LINE_TYPE_POLYLINE){
            for (int i = 0; i < datas.size()-1; i++) {
                float svalue = datas.valueAt(i);
                float evalue = datas.valueAt(i+1);
                PointF sp;
                if (i==0){
                    sp=new PointF(xlenghtScale*i+ypx,ypy);
                }else{
                    sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale+PADDING);
                }
                PointF ep=new PointF(xlenghtScale*(i+1)+ypx,ypy-evalue*ylenghtScale+PADDING);
                Path path=new Path();
                path.moveTo(sp.x,sp.y);
                path.lineTo(ep.x,ep.y);
                canvas.drawPath(path,mPaint);

                float btx=sp.x;
                canvas.drawText(datas.keyAt(i),btx-textPaint.measureText(datas.keyAt(i))/2,height,textPaint);
                if (i==datas.size()-2){
                    btx=ep.x;
                    canvas.drawText(datas.keyAt(i+1),btx-textPaint.measureText(datas.keyAt(i))/2,height,textPaint);
                }
            }
        }else{
            throw new RuntimeException("don't fuck blind set type");
        }


        canvas.drawLine(ypx-3,ypy,width,ypy,xyPaint);
        canvas.drawLine(ypx,ypy+3,ypx,PADDING,xyPaint);

        float leftScale=(ypy-PADDING)/leftData.size();
        leftScale+=leftScale/(leftData.size()-1);
        for (int i = 0; i < leftData.size(); i++) {
            float y=ypy-i*leftScale;
            float x=leftDataWidth-textPaint.measureText(leftData.get(i));
            if (i==leftData.size()-1){
                y=PADDING+textPaint.getTextSize()/2;
            }
            canvas.drawText(leftData.get(i),x,y,textPaint);
        }

        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < datas.size(); i++) {
            float svalue = datas.valueAt(i);
            PointF sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale+PADDING);
            if (i==0){
                sp=new PointF(xlenghtScale*i+ypx,ypy);
            }
            canvas.drawCircle(sp.x,sp.y,10,mPaint);
        }

    }

    public void setType(int lineType){
        this.lineType =lineType;
        postInvalidate();
    }

    public void setLeftData(List<String> leftData){
        if (leftData==null || leftData.size()<=0){
            throw new NullPointerException("leftData is null or size is 0");
        }
        this.leftData = leftData;
        leftDataWidth = 0;
        for (int i = 0; i < leftData.size(); i++) {
            float mwidth = textPaint.measureText(leftData.get(i));
            leftDataWidth = leftDataWidth <mwidth?mwidth: leftDataWidth;
        }
        postInvalidate();
    }

    public void setData(ArrayMap<String,Float> datas){
        if (datas ==null || datas.size()<=0){
            throw new NullPointerException("bottomData is null or size is 0");
        }
        this.datas = datas;
        maxValue =0;
        for (int i = 0; i < datas.size(); i++) {
            float mv = datas.valueAt(i);
            maxValue = maxValue <mv?mv: maxValue;
        }
        postInvalidate();
    }

    public void isNoData() {
        if (leftData == null || leftData.size()<=0){
            throw new RuntimeException("you need setLeftData");
        }
        if (datas ==null || datas.size()<=0){
            throw new RuntimeException("you need setData");
        }
    }
}
