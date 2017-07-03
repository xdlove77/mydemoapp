package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.dongao.mydemoapp.R;


/**
 * Created by dongao on 2016/8/23.
 */
public class SideBar extends View {
    private static final int DEFULT_BG_COLOR=Color.TRANSPARENT;
    private static final int DEFULT_CHOOSE_BG_COLOR=Color.LTGRAY;
    private static final int DEFULT_TEXT_COLOR=Color.BLACK;
    private static final int DEFULT_TEXT_CHOOSE_COLOR=Color.GREEN;
    private static final int DEFULT_TEXT_SIZE=15;

    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private OnTouchingLetterChangedListener letterChangedListener;
    private int bgColor;
    private int txCColor;
    private int txColor;
    private Paint mPaint;
    private Context context;
    private int choose=-1;

    public SideBar(Context context) {
        this(context,null);

    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(typedArray.getDimensionPixelSize(R.styleable.SideBar_txSize
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFULT_TEXT_SIZE,context.getResources().getDisplayMetrics())));
        txColor=typedArray.getColor(R.styleable.SideBar_txColor, DEFULT_TEXT_COLOR);
        txCColor=typedArray.getColor(R.styleable.SideBar_textChooesColor,DEFULT_TEXT_CHOOSE_COLOR);
        bgColor=typedArray.getColor(R.styleable.SideBar_bgColor,DEFULT_BG_COLOR);
        mPaint.setColor(txColor);
        typedArray.recycle();
        setBackgroundColor(DEFULT_CHOOSE_BG_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float itemHeight = (float) (1.0*getHeight()/letters.length);
        int width = getWidth();
        for (int i=0;i<letters.length;i++){
            if (choose==i){
                mPaint.setColor(txCColor);
            }else{
                mPaint.setColor(txColor);
            }
            float textSize = mPaint.measureText(letters[i]);
            canvas.drawText(letters[i],width/2-textSize/2
                    ,itemHeight*i+itemHeight,mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int letterCount= (int) (event.getY()/getHeight()*letters.length);
        final int oldChoose=choose;
        choose=letterCount;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(bgColor);
                if (choose!=oldChoose){
                    if (letterChangedListener!=null){
                        letterChangedListener.onTouchingLetterChanged(letters[choose]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(DEFULT_CHOOSE_BG_COLOR);
                choose=-1;
                if (letterChangedListener!=null){
                    letterChangedListener.onTouchingLetterChanged(null);
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setLetterChangedListener(OnTouchingLetterChangedListener letterChangedListener) {
        this.letterChangedListener = letterChangedListener;
    }

    public interface OnTouchingLetterChangedListener{
       void onTouchingLetterChanged(String letter);
    }
}
