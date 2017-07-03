package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xd on 2017/1/17.
 */

public class DanCanvas extends View {

    private Paint mPaint;
    protected List<DanContentTextBean> textBeans;
    private Random mRandom;

    public DanCanvas(Context context) {
        this(context,null);
    }

    public DanCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mRandom=new Random();
        textBeans=new ArrayList<>();

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);

        DanThread thread=new DanThread();
        thread.start();
    }

    public synchronized void setContentText(String text){
        Log.d("hhh","list length = "+textBeans.size());
        DanContentTextBean bean=null;
        for (int i = 0; i < textBeans.size(); i++) {
            if (isCanReuse(textBeans.get(i))){
                bean=textBeans.get(i);
                break;
            }
        }
        if (bean==null){
            bean=new DanContentTextBean();
            textBeans.add(bean);
        }

        bean.setLocX(getContext().getResources().getDisplayMetrics().widthPixels);
        bean.setLocY(mRandom.nextInt((int) (getContext().getResources().getDisplayMetrics().heightPixels
                                - mPaint.getTextSize()))+mPaint.getTextSize());
        bean.setText(text);
        bean.setStep(mRandom.nextInt(17)+3);
        bean.setTextLenght(mPaint.measureText(text));
        bean.setTextColor(Color.rgb(mRandom.nextInt(255),mRandom.nextInt(255),mRandom.nextInt(255)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < textBeans.size(); i++) {
            DanContentTextBean bean = textBeans.get(i);
            if (!isCanReuse(bean)){
                mPaint.reset();
                mPaint.setColor(bean.getTextColor());
                mPaint.setTextSize(30);
                canvas.drawText(bean.getText(),bean.getLocX(),bean.getLocY(),mPaint);
            }
        }
    }

    private boolean isCanReuse(DanContentTextBean textBean){
        if (textBean!=null ){
            if (textBean.getLocX()<0 &&
                    - textBean.getTextLenght() >textBean.getLocX()){
                return true;
            }
        }
        return false;
    }

    public class DanThread extends Thread{
        @Override
        public void run() {
            try {
                while(true){
                    for (DanContentTextBean bean : textBeans) {
                        if (!isCanReuse(bean)){
                            bean.setLocX(bean.getLocX()-bean.getStep());
                        }
                    }
                    postInvalidate();
                    Thread.sleep(60);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DanContentTextBean{
        private String text;
        private float locX,locY;
        private float textLength;
        private int step;
        private int textColor;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getLocX() {
            return locX;
        }

        public void setLocX(float locX) {
            this.locX = locX;
        }

        public float getLocY() {
            return locY;
        }

        public void setLocY(float locY) {
            this.locY = locY;
        }

        public float getTextLenght() {
            return textLength;
        }

        public void setTextLenght(float textLength) {
            this.textLength = textLength;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }
    }
}
