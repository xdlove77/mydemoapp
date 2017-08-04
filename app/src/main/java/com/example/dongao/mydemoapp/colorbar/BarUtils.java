package com.example.dongao.mydemoapp.colorbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by fishzhang on 2017/8/3.
 */

public class BarUtils {

    private static final int ALPHA=0xff;

    public static void setBarColor(Activity activity, int color){
        setBarColor(activity,color,ALPHA);
    }

    public static void setBarColor(Activity activity,int color,int alpha){
        setBarColor(activity,color,alpha,false);
    }

    public static void setBarColor(Activity activity,int color,boolean withNavBar){
        setBarColor(activity,color,ALPHA,withNavBar);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setBarColor(Activity activity, @ColorInt int color, int alpha, boolean withNavBar){
        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();

        int finalColor = alpha==ALPHA? color
                : alpha == 0 ? Color.TRANSPARENT
                : Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (alpha < ALPHA && alpha >= 0){
                int option= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
            }
            window.setStatusBarColor(finalColor);
            if (withNavBar){
                window.setNavigationBarColor(finalColor);
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            decorView.addView(createStatusBar(activity,finalColor));
            if (withNavBar){
                if (hasNavigationBar(activity)){
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    decorView.addView(createNavigationBar(activity,finalColor));
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setFullScreen(Activity activity){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private static View createNavigationBar(Activity activity, int color){
        View navigationBar=new View(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                measureNavigationBarHeight(activity)
        );
        lp.gravity= Gravity.BOTTOM;
        navigationBar.setLayoutParams(lp);
        navigationBar.setBackgroundColor(color);
        return navigationBar;
    }

    private static View createStatusBar(Activity activity, int color){
        View statusBarView=new View(activity);
        statusBarView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                measureStatusBarHeight(activity)
        ));
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static int measureStatusBarHeight(Activity activity) {
        int result=0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId>0){
            result=activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static int measureNavigationBarHeight(Activity activity) {
        int result=0;
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId>0){
            result=activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private static boolean hasNavigationBar(Activity activity) {

        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplay=new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            display.getRealMetrics(realDisplay);
        }

        int realDisplayWidth=realDisplay.widthPixels;
        int realDisplayHeight=realDisplay.heightPixels;

        DisplayMetrics displayMetrics=new DisplayMetrics();

        display.getMetrics(displayMetrics);

        int displayWidth=displayMetrics.widthPixels;
        int displayHeight=displayMetrics.heightPixels;

        return (realDisplayWidth-displayWidth)>0 || (realDisplayHeight-displayHeight)>0;
    }
}
