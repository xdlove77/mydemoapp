package com.example.dongao.mydemoapp;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;
import android.view.Choreographer;

/**
 * Created by fishzhang on 2018/3/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Looper.getMainLooper().setMessageLogging(MyPrinter.getInstance());
    }

    private static class MyPrinter implements Printer{

        private static Printer printer=new MyPrinter();
        private HandlerThread handlerThread;
        private Handler handler;
        private static Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Thread mainThread = Looper.getMainLooper().getThread();
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
                StackTraceElement[] stackTrace = mainThread.getStackTrace();
                for (StackTraceElement stack: stackTrace) {
                    Log.d("hhh" , stack.toString());
                }
            }
        };


        public static Printer getInstance(){
            return printer;
        }

        private MyPrinter(){
            handlerThread=new HandlerThread("print");
            handlerThread.start();
            handler=new Handler(handlerThread.getLooper());
        }

        @Override
        public void println(String x) {
            if (x.contains(">>>>> Dispatching")){
                handler.post(runnable);
            }else if (x.contains("<<<<< Finished")){
                handler.removeCallbacks(runnable);
            }
        }
    }
}
