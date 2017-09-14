package com.example.dongao.mydemoapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dongao.mydemoapp.widget.BingImage;
import com.example.dongao.mydemoapp.widget.DanCanvas;
import com.example.dongao.mydemoapp.widget.MyLoadMoreScrollview;
import com.example.dongao.mydemoapp.widget.MyNestedScrollview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class Main2Activity extends AppCompatActivity {

//    private DanCanvas danCanvas;
    private String[] texts={"adfadf","轶可赛艇","66666","多赛艇","因缺思厅","主播6666"};
    private DanCanvas danCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        List<String> datas=new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            datas.add("haha="+ i);
        }
        MyNestedAdapter myNestedAdapter=new MyNestedAdapter(this,datas);
//        MyLoadMoreScrollview myNestedScrollview= (MyLoadMoreScrollview) findViewById(R.id.MyNestedScrollview);
//        myNestedScrollview.setRVLayoutManager(new LinearLayoutManager(this),myNestedAdapter);

//        danCanvas = (DanCanvas) findViewById(R.id.danCanvas);
//        danCanvas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text();
//            }
//        });
        for (int i = 0; i < texts.length; i++) {

        }

    }
    public void text(){
        for (int i = 0; i < 10; i++) {
            for (String text :
                    texts) {
                danCanvas.setContentText(text);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    class MyNestedAdapter extends RecyclerView.Adapter<MyNestedViewHolder>{
        private Context context;
        private List<String> datas;
        public MyNestedAdapter(Context context,List<String> datas) {
            this.context=context;
            this.datas=datas;
        }

        @Override
        public MyNestedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyNestedViewHolder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false));
        }

        @Override
        public void onBindViewHolder(MyNestedViewHolder holder, int position) {
            holder.textView.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class MyNestedViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public MyNestedViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
