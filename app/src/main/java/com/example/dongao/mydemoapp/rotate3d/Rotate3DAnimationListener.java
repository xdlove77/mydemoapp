package com.example.dongao.mydemoapp.rotate3d;

import android.view.animation.Animation;

/**
 * Created by fishzhang on 2018/4/4.
 */

public interface Rotate3DAnimationListener {
    void start(Animation animation);
    void center(Animation animation,boolean fromSecond);
    void finish(Animation animation);
}
