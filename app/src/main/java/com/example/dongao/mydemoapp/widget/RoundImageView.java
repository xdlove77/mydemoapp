package com.example.dongao.mydemoapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.dongao.mydemoapp.R;


/**
 * Created by dongao on 2016/8/11.
 */
@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {
    private Paint mPaint;
    private int width;
    private int radius;

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
        width=widthSize>heightSize?heightSize:widthSize;
        radius = width / 2;
        setMeasuredDimension(width,width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = getBitmap();
        setBitmap(bitmap);

        canvas.drawCircle(radius,radius,radius,mPaint);
    }

    private Bitmap getBitmap(){
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public void setBitmap(Bitmap bitmap) {
        Matrix matrix=new Matrix();
        float scale=1;
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();
        if (bwidth > bheight){
            scale=1.0f*width/bheight;
        }else{
            scale=1.0f*width/bwidth;
        }
        matrix.setScale(scale,scale);
        if (bwidth > bheight){
            float translation = (bwidth*scale - width) / 2;
            if (translation >0){
                matrix.postTranslate(0,-translation);
            }else{
                matrix.postTranslate(0,translation);
            }
        }else{
            float translation = (bheight*scale - width) / 2;
            if (translation >0){
                matrix.postTranslate(0,-translation);
            }else{
                matrix.postTranslate(0,translation);
            }
        }

        BitmapShader shader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        mPaint.setShader(shader);
    }
}
