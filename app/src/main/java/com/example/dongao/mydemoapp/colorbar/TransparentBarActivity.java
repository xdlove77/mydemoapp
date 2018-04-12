package com.example.dongao.mydemoapp.colorbar;

import android.graphics.Color;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dongao.mydemoapp.R;

public class TransparentBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_bar);
        BarUtils.setBarColor(this, Color.GRAY,0,true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case TRIM_MEMORY_COMPLETE://你在lru的队列尾部，在不释放点内存 你tm就死定了
                break;
            case TRIM_MEMORY_MODERATE://你在lru的队列中间，是不是很侥幸，要是你不释放点内存等系统内存吃紧你也要像上面仁兄一样fuck off了
                break;
            case TRIM_MEMORY_BACKGROUND://美滋滋啊 在队列头部，不过系统快要回收你其他几个队列尾部的兄弟了 你也要尽量释放点内存让自己启动快速点
                break;
            case TRIM_MEMORY_UI_HIDDEN://当前UI不可见了（刚跑到后台） 趁机释放点内存
                break;

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ){
            switch (level){
                case TRIM_MEMORY_RUNNING_CRITICAL://诶我是前台进程 死不了 美滋滋，不过系统提示要杀死其他几位兄弟
                    // 之后会调用onLowMemory通知要杀人了  你要不释放点内存 会影响用户体验
                    break;
                case TRIM_MEMORY_RUNNING_LOW://你要不释放点内存 会影响用户体验
                    break;
                case TRIM_MEMORY_RUNNING_MODERATE://系统进入低内存状态  不过你放心 就你死不了
                    break;
            }
        }
    }
}
