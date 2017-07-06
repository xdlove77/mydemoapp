package com.example.dongao.mydemoapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import com.example.dongao.mydemoapp.widget.BingImage;
import com.example.dongao.mydemoapp.widget.QulineView;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SafeSubscriber;

public class MySelfBingImageDemoActivity extends Activity {

    private BingImage bingImage;
    private QulineView qulineView;
    private boolean isPolyline=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_self_bing_image_demo);
        bingImage = (BingImage) findViewById(R.id.bingImage);
        List<BingImage.BingBean> data=new ArrayList<>();
        data.add(new BingImage.BingBean(Color.BLUE,0.08f,"","嘿嘿嘿税法法法法法嘿嘿税"));
        data.add(new BingImage.BingBean(Color.GRAY,0.01f,"","经济法嘿嘿嘿法法法嘿嘿税"));
        data.add(new BingImage.BingBean(Color.GREEN,0.01f,"","税法法法法法法法嘿嘿税"));
        data.add(new BingImage.BingBean(Color.BLACK,0.15f,"","嘿嘿法法法法法法嘿嘿税"));
        data.add(new BingImage.BingBean(Color.LTGRAY,0.01f,"","嘿嘿嘿法法法法法法嘿嘿税"));
        data.add(new BingImage.BingBean(Color.GREEN,0.74f,"","gg法法法法法法法法嘿嘿税"));

        int count =0;
        float totalPrecent=0;
        float total=0;
        List<BingImage.BingBean> ds=new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            BingImage.BingBean bingBean = data.get(i);
            if (bingBean.getPercent()<=0.1f){
                bingBean.setPercent(0.1f);
                count++;
            }else{
                totalPrecent+=bingBean.getPercent();
            }
            total+=bingBean.getPercent();
            ds.add(bingBean);
        }
        float part=1/total;
        for (int i = 0; i < ds.size() ; i++) {
            BingImage.BingBean bingBean = ds.get(i);
            bingBean.setPercent(bingBean.getPercent()*part);
        }
//        float num=1-count*0.09f;
//        for (int i = 0; i < ds.size(); i++) {
//            BingImage.BingBean bingBean = ds.get(i);
//            if (bingBean.getPercent()>0.09f){
//                bingBean.setPercent(num*bingBean.getPercent()/totalPrecent);
//            }
//        }

        bingImage.setData(ds);


        qulineView= (QulineView) findViewById(R.id.qulineView);
        ArrayMap<String,Float> datas=new ArrayMap<>();
        datas.put("1",10f);
        datas.put("3",100f);
        datas.put("4",110f);
        datas.put("5",80f);
        datas.put("6",50f);
        datas.put("7",200f);
        datas.put("8",140f);
        qulineView.setData(datas);
        List<String> list=new ArrayList<>();
        list.add("10m");
        list.add("57.5m");
        list.add("105m");
        list.add("152.5m");
        list.add("200m");
        qulineView.setLeftData(list);

    }

    public void onClick(View view){
        int type =(isPolyline =!isPolyline)?QulineView.LINE_TYPE_POLYLINE:QulineView.LINE_TYPE_CURVE;
        qulineView.setType(type);
    }
}
