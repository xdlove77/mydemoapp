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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

    private static final int DEFULT_LINE_WIDTH=1;
    private static final int DEFULT_LINE_COLOR=0xffffffff;
    private static final int DEFULT_XYLINE_WIDTH=1;
    private static final int DEFULT_XYLINE_COLOR=0xffffffff;
    private static final int DEFULT_POINT_SIZE=2;
    private static final int DEFULT_POINT_COLOR=0xffffffff;
    private static final int DEFULT_TEXT_SIZE=6;
    private static final int DEFULT_TEXT_COLOR=0xffffffff;
    private static final int DEFULT_TRANSITION_START_COLOR=0xeeeeeeee;
    private static final int DEFULT_TRANSITION_END_COLOR=0x00eeeeee;

    private int lineType=LINE_TYPE_CURVE;

    private static final int PADDING=10;

    private Paint linePaint;
    private Paint xyPaint;
    private Paint textPaint;
    private Paint pointPaint;

    private int width;
    private int height;
    private float leftDataWidth;

    private float maxValue;
    private float minValue;

    private List<String> leftData;
    private ArrayMap<String,Float> datas;
    private float pointSize;

    private int transitionStartColor;
    private int transitionEndColor;
    private boolean showPoint;

    public QulineView(Context context) {
        this(context,null);
    }

    public QulineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QulineView);
        transitionStartColor=typedArray.getColor(R.styleable.QulineView_transitionStartColor
                ,DEFULT_TRANSITION_START_COLOR);
        transitionEndColor=typedArray.getColor(R.styleable.QulineView_transitionEndColor
                ,DEFULT_TRANSITION_END_COLOR);
        lineType=typedArray.getInt(R.styleable.QulineView_lineType,LINE_TYPE_CURVE);
        showPoint =typedArray.getBoolean(R.styleable.QulineView_showPoint,false);

        initPaint(typedArray,context.getResources().getDisplayMetrics());

        typedArray.recycle();
    }

    private void initPaint(TypedArray typedArray, DisplayMetrics displayMetrics) {
        float lineWidth=typedArray.getDimension(R.styleable.QulineView_line_Width
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFULT_LINE_WIDTH,displayMetrics));
        int lineColor=typedArray.getColor(R.styleable.QulineView_line_Color
                ,DEFULT_LINE_COLOR);

        float xyLineWidth=typedArray.getDimension(R.styleable.QulineView_xyLineWidth
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFULT_XYLINE_WIDTH,displayMetrics));
        int xyLineColor=typedArray.getColor(R.styleable.QulineView_xyLineColor
                ,DEFULT_XYLINE_COLOR);

        pointSize = typedArray.getDimension(R.styleable.QulineView_text_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFULT_POINT_SIZE,displayMetrics));
        int pointColor=typedArray.getColor(R.styleable.QulineView_text_color
                ,DEFULT_POINT_COLOR);

        float text_size=typedArray.getDimension(R.styleable.QulineView_text_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,DEFULT_TEXT_SIZE,displayMetrics));
        int text_color=typedArray.getColor(R.styleable.QulineView_text_color
                ,DEFULT_TEXT_COLOR);


        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);

        pointPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(pointColor);

        xyPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        xyPaint.setStyle(Paint.Style.STROKE);
        xyPaint.setStrokeWidth(xyLineWidth);
        xyPaint.setColor(xyLineColor);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(text_size);
        textPaint.setColor(text_color);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width =w-PADDING;
        height =h;
        //各曲线设置渐变色
//        float sh=height;
//        linearGradient=new LinearGradient(0f,0f,0f,sh,new int[]{ Color.BLUE,Color.GRAY ,Color.BLUE},null, Shader.TileMode.REPEAT);
//        linePaint.setShader(linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        isNoData();
        //获取原点
        float ypx=leftDataWidth+10;
        float ypy=height-textPaint.getTextSize()-10;

        //根据数值算对应占用高度和宽度的比例
        float xlenghtScale=(width-ypx)/datas.size();
        xlenghtScale+=xlenghtScale/(datas.size()-1);
        float ylenghtScale=(ypy-PADDING)/(maxValue- minValue);

        Path linePath=new Path();
        Path shadowPath=new Path();

        if (lineType == LINE_TYPE_CURVE){
            for (int i = 0; i < datas.size()-1; i++) {
                float svalue = datas.valueAt(i) - minValue;
                float evalue = datas.valueAt(i+1) - minValue;
                PointF sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale);
                PointF ep=new PointF(xlenghtScale*(i+1)+ypx,ypy-evalue*ylenghtScale);
                float wt=(sp.x+ep.x)/2;
                PointF p1=new PointF(wt,sp.y);
                PointF p2=new PointF(wt,ep.y);
                if (i==0){
                    linePath.moveTo(sp.x,sp.y);
                    shadowPath.moveTo(sp.x,sp.y);
                }
                linePath.cubicTo(p1.x,p1.y,p2.x,p2.y,ep.x,ep.y);
                shadowPath.cubicTo(p1.x,p1.y,p2.x,p2.y,ep.x,ep.y);

                //每个值对应的x轴数字
                float btx=sp.x;
                float textSize = textPaint.measureText(datas.keyAt(i));
                canvas.drawText(datas.keyAt(i),btx- textSize /2,ypy+10+textSize,textPaint);
                if (i==datas.size()-2){
                    btx=ep.x;
                    canvas.drawText(datas.keyAt(i+1),btx- textSize /2,ypy+10+textSize,textPaint);
                }

            }
        }else if (lineType == LINE_TYPE_POLYLINE){
            for (int i = 0; i < datas.size()-1; i++) {
                float svalue = datas.valueAt(i) - minValue;

                float evalue = datas.valueAt(i+1) - minValue;
                PointF sp;
                if (i==0){
                    sp=new PointF(xlenghtScale*i+ypx,ypy);
                }else{
                    sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale);
                }
                PointF ep=new PointF(xlenghtScale*(i+1)+ypx,ypy-evalue*ylenghtScale);
                if (i==0){
                    linePath.moveTo(sp.x,sp.y);
                    shadowPath.moveTo(sp.x,sp.y);
                }
                linePath.lineTo(ep.x,ep.y);
                shadowPath.lineTo(ep.x,ep.y);

                //每个值对应的x轴数字
                float btx=sp.x;
                float textSize = textPaint.measureText(datas.keyAt(i));
                canvas.drawText(datas.keyAt(i),btx- textSize /2,ypy+10+textSize,textPaint);
                if (i==datas.size()-2){
                    btx=ep.x;
                    canvas.drawText(datas.keyAt(i+1),btx- textSize /2,ypy+10+textSize,textPaint);
                }
            }
        }else{
            throw new RuntimeException("don't fuck blind set type");
        }

        //加渐变色
        Paint shadowPaint =new Paint();
        shadowPaint.setShader(new LinearGradient(0,0,0,height,transitionStartColor
                ,transitionEndColor, Shader.TileMode.CLAMP));
        shadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        shadowPath.lineTo(width, ypy);
        shadowPath.lineTo(ypx, ypy);
        canvas.drawPath(shadowPath,shadowPaint);

        //画曲线
        canvas.drawPath(linePath,linePaint);

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

        if (showPoint)
            //画点
            drawPoint(xlenghtScale,ylenghtScale,ypx,ypy,canvas);

    }

    /**
     * 根据比例和原点画出每个值对应的点
     * @param xlenghtScale
     * @param ylenghtScale
     * @param ypx
     * @param ypy
     * @param canvas
     */
    private void drawPoint(float xlenghtScale, float ylenghtScale, float ypx, float ypy, Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            float svalue = datas.valueAt(i) - minValue;
            PointF sp=new PointF(xlenghtScale*i+ypx,ypy-svalue*ylenghtScale);
            canvas.drawCircle(sp.x,sp.y,pointSize,pointPaint);
        }
    }

    /**
     * 设置曲线或者折线 显示
     * @param lineType
     */
    public void setType(int lineType){
        this.lineType =lineType;
        invalidate();
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
        invalidate();
    }

    public void setData(ArrayMap<String,Float> datas){
        if (datas ==null || datas.size()<=0){
            throw new NullPointerException("bottomData is null or size is 0");
        }
        this.datas = datas;
        maxValue =0;
        minValue=datas.valueAt(0);
        for (int i = 0; i < datas.size(); i++) {
            float mv = datas.valueAt(i);
            maxValue = Math.max(mv,maxValue);
            minValue = Math.min(mv,minValue);
        }
        invalidate();
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
