package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fishzhang on 2017/7/26.
 */

public class DrawHeartView extends View {

    private Path path;
    private Paint paint;

    public DrawHeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path=new Path();
        path.addArc(200,200,400,400,-225,225);
        path.arcTo(400,200,600,400,-180,225,false);
        path.lineTo(400,560);

        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawPath(path,paint);

        paint.setColor(Color.BLACK);
    }
}
