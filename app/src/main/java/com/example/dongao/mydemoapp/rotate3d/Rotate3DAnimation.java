package com.example.dongao.mydemoapp.rotate3d;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by fishzhang on 2018/4/4.
 */

public class Rotate3DAnimation extends Animation {

    private float depthZ;
    private float fromDegress;
    private float toDegress;
    private float centerX;
    private float centerY;
    private boolean reverse;
    private Camera camera;
    private float scale;

    public Rotate3DAnimation(float depthZ, float fromDegress, float toDegress, float centerX, float centerY
            , boolean reverse, DisplayMetrics displayMetrics) {
        scale=displayMetrics.density;
        this.depthZ = depthZ;
        this.fromDegress = fromDegress;
        this.toDegress = toDegress;
        this.centerX = centerX;
        this.centerY = centerY;
        this.reverse = reverse;

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degress=fromDegress+(toDegress-fromDegress)*interpolatedTime;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (reverse)
            camera.translate(0,0,depthZ*interpolatedTime);
        else
            camera.translate(0,0,depthZ*(1-interpolatedTime));
        camera.rotateY(degress);
        camera.getMatrix(matrix);
        camera.restore();

        float[] mValues = {0,0,0,0,0,0,0,0,0};
        matrix.getValues(mValues);
        mValues[6] = mValues[6]/ scale;
        matrix.setValues(mValues);

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);
    }


}
