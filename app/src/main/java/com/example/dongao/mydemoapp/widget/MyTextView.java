package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by fishzhang on 2018/1/19.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint baseLinePaint;
    private Paint topLinePaint;
    private Paint bottomLinePaint;
    private Paint ascentLinePaint;
    private Paint descentLinePaint;
    private Paint centerLinePaint;
    private Paint textPaint;

    public MyTextView(Context context) {
        this(context,null);

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Paint basePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setStrokeWidth(2);
        textPaint=new TextPaint(basePaint);
        textPaint.setTextSize(50);

        baseLinePaint=new Paint(basePaint);
        baseLinePaint.setColor(Color.RED);


        topLinePaint=new Paint(basePaint);
        topLinePaint.setColor(Color.BLUE);

        bottomLinePaint=new Paint(basePaint);
        bottomLinePaint.setColor(Color.BLUE);

        ascentLinePaint=new Paint(basePaint);
        ascentLinePaint.setColor(Color.GREEN);

        descentLinePaint=new Paint(basePaint);
        descentLinePaint.setColor(Color.GREEN);

        centerLinePaint=new Paint(basePaint);
        centerLinePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int height=getHeight();
        int width=getWidth();
        String text = "哈haHAЁ";

        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics fm = textPaint.getFontMetrics();

        float x=width/2-textWidth/2;
        float y=height/2-(fm.ascent+(-fm.ascent+fm.descent)/2);

        Log.d("hh","top = "+fm.top+" , bottom = "+fm.bottom +" , ascent = "+fm.ascent+" , descent = "+fm.descent+" , leading = "+fm.leading);
        canvas.drawText(text,x,y,textPaint);
        canvas.drawLine(x,y,x+textWidth,y,baseLinePaint);

        canvas.drawLine(x,y+fm.top,x+textWidth,y+fm.top,topLinePaint);

        canvas.drawLine(x,y+fm.bottom,x+textWidth,y+fm.bottom,bottomLinePaint);

        canvas.drawLine(x,y+fm.ascent,x+textWidth,y+fm.ascent,ascentLinePaint);

        canvas.drawLine(x,y+fm.descent,x+textWidth,y+fm.descent,descentLinePaint);

        canvas.drawLine(x,y+fm.ascent+(-fm.ascent+fm.descent)/2,x+textWidth,y+fm.ascent+(-fm.ascent+fm.descent)/2,centerLinePaint);
    }
}
