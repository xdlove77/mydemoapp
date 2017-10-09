package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishzhang on 2017/9/19.
 */

public class LockView extends View {

    Region[][] regions=new Region[3][3];
    private List<Pointer> pointerIndexList;
    private Paint pointPaint;
    private Paint selectPP;
    private int color=Color.BLACK;
    private int selectColor=color;
    private int errorColor=Color.parseColor("#A21A25");
    private Paint linePaint;
    private List<Integer> nums;
    private float movingX;
    private float movingY;
    private Context context;
    private boolean needLine=true;
    private DrawDataCallBack drawDataCallBack;
    private int dataSize=4;
    private int lineSize=2;
    private boolean isNoNeedAllPoint=true;
    private long errorTime=1000;

    public LockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        selectPP=new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPP.setColor(selectColor);

        pointPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(lineSize);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(selectColor);

        pointerIndexList=new ArrayList<>();
        nums=new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int size=w>h?h-getPaddingTop()-getPaddingBottom()-lineSize:w-getPaddingLeft()-getPaddingRight()-lineSize;
        int left=0,top=0,right=0,bottom=0;
        if (w>h){
            left=(w/2)-(size/2);
            top=getPaddingTop()+lineSize/2;
            right=left+size;
            bottom=top+size;
        }else{
            left=getPaddingLeft()+lineSize/2;
            top=h/2-size/2;
            right=left+size;
            bottom=top+size;
        }
        Rect totalArea=new Rect(left,top,right,bottom);

        int cpadding=60;

        int itemSize = (totalArea.width()- cpadding * 2) / 3;

        int itemTop=totalArea.top,itemLeft=totalArea.left;

        for (int i = 0; i < regions.length; i++) {

            for (int j = 0; j < regions[i].length; j++) {

                regions[i][j]=new Region(itemLeft,itemTop
                        ,itemLeft+itemSize,itemTop+itemSize);
                itemLeft+=itemSize+cpadding;
            }
            itemLeft=totalArea.left;
            itemTop+=(itemSize+cpadding);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (!pointerIndexList.isEmpty() && needLine){
            for (int i = 0; i < pointerIndexList.size(); i++) {
                Pointer pointer = pointerIndexList.get(i);
                if (i== pointerIndexList.size()-1){
                    Rect pointR=regions[pointer.getxIndex()][pointer.getyIndex()].getBounds();
                    canvas.drawLine(pointR.centerX(),pointR.centerY(),movingX,movingY,linePaint);
                }else{
                    Pointer pointer2=pointerIndexList.get(i+1);
                    Rect point1=regions[pointer.getxIndex()][pointer.getyIndex()].getBounds();
                    Rect point2=regions[pointer2.getxIndex()][pointer2.getyIndex()].getBounds();
                    canvas.drawLine(point1.centerX(),point1.centerY(),point2.centerX(),point2.centerY(),linePaint);
                }
            }
        }
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                Rect pointR=regions[i][j].getBounds();

                pointPaint.setStyle(Paint.Style.FILL);
                pointPaint.setColor(Color.WHITE);
                canvas.drawCircle(pointR.centerX(),pointR.centerY(),pointR.width()/2,pointPaint);

                pointPaint.setStyle(Paint.Style.STROKE);
                pointPaint.setStrokeWidth(lineSize);
                pointPaint.setColor(Color.GRAY);
                canvas.drawCircle(pointR.centerX(),pointR.centerY(),pointR.width()/2,pointPaint);
            }
        }
        if (!pointerIndexList.isEmpty() && needLine) {
            for (int i = 0; i < pointerIndexList.size(); i++) {
                Pointer pointer = pointerIndexList.get(i);
                Rect bounds = regions[pointer.getxIndex()][pointer.getyIndex()].getBounds();

                selectPP.setStyle(Paint.Style.STROKE);
                selectPP.setColor(Color.WHITE);
                selectPP.setStrokeWidth(10);
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2+lineSize, selectPP);

                selectPP.setColor(selectColor);
                selectPP.setStyle(Paint.Style.STROKE);
                selectPP.setStrokeWidth(lineSize);
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, selectPP);

                selectPP.setStyle(Paint.Style.FILL);
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 5, selectPP);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(runnable);
                linePaint.setColor(color);
                selectColor=color;
                if (!pointerIndexList.isEmpty()){
                    pointerIndexList.clear();
                    nums.clear();
                    invalidate();
                }

                Pointer firstPoint = isContainPoint((int) event.getX(), (int) event.getY());
                if (firstPoint != null) {
                    movingX=event.getX();
                    movingY =event.getY();
                    pointerIndexList.add(firstPoint);
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                movingX =event.getX();
                movingY =event.getY();
                invalidate();
                Pointer point = isContainPoint((int) event.getX(), (int) event.getY());
                if (point != null) {
                    pointerIndexList.add(point);
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                if (!pointerIndexList.isEmpty()){

                    Pointer pointer = pointerIndexList.get(pointerIndexList.size() - 1);
                    Rect bounds = regions[pointer.getxIndex()][pointer.getyIndex()].getBounds();
                    movingX= bounds.centerX();
                    movingY= bounds.centerY();
                    invalidate();

                    if (dataSize > 0 && pointerIndexList.size() < dataSize){
                        if (drawDataCallBack!=null){
                            drawDataCallBack.sizeIsSmall("数量不能小于"+dataSize);
                            break;
                        }
                    }

                    StringBuffer sb=new StringBuffer();
                    for (Integer num: nums) {
                        sb.append(num+"");
                    }
                    if (drawDataCallBack!=null){
                        drawDataCallBack.returnData(sb.toString());
                    }
                    Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void reset(){
        if (!pointerIndexList.isEmpty()){
            pointerIndexList.clear();
            nums.clear();
        }
        invalidate();
    }

    private Handler handler=new Handler(Looper.getMainLooper());

    private Runnable runnable;

    public void errorStatus(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!pointerIndexList.isEmpty()){
                    pointerIndexList.clear();
                    nums.clear();
                }
                invalidate();
            }
        };
        selectColor=errorColor;
        linePaint.setColor(errorColor);
        invalidate();
        handler.postDelayed(runnable,errorTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacks(runnable);
        runnable=null;
        super.onDetachedFromWindow();

    }

    private Pointer isContainPoint(int x, int y){
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                if (regions[i][j].contains(x,y)){
                    if (isNoNeedAllPoint){
                        for (int k = 0; k < pointerIndexList.size(); k++) {
                            Pointer p = pointerIndexList.get(k);
                            if (p.getxIndex()==i && p.getyIndex()==j){
                                return null;
                            }
                        }
                    }
                    int data=0;
                    if (nums.size()>0){
                        data = nums.get(nums.size() - 1);
                    }
                    if (data != (i*3+j+1))
                        nums.add(i*3+j+1);
                    return new Pointer(i, j);
                }
            }
        }
        return null;
    }

    public void setDrawDataCallBack(DrawDataCallBack drawDataCallBack) {
        this.drawDataCallBack = drawDataCallBack;
    }

    public interface DrawDataCallBack{
        void returnData(String data);
        void sizeIsSmall(String errorMsg);
    }

    private static class Pointer{
        private int xIndex;
        private int yIndex;

        public Pointer(int xIndex, int yIndex) {
            this.xIndex = xIndex;
            this.yIndex = yIndex;
        }

        public int getxIndex() {
            return xIndex;
        }

        public void setxIndex(int xIndex) {
            this.xIndex = xIndex;
        }

        public int getyIndex() {
            return yIndex;
        }

        public void setyIndex(int yIndex) {
            this.yIndex = yIndex;
        }
    }
}
