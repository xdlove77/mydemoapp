package com.example.dongao.mydemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;

import com.example.dongao.mydemoapp.widget.MyQulineView;
import com.example.dongao.mydemoapp.widget.QulineView;

import java.util.ArrayList;
import java.util.List;

public class KuaiJiYunQuLineDemoActivity extends AppCompatActivity {
    private MyQulineView qulineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuai_ji_yun_qu_line_demo);

        qulineView= (MyQulineView) findViewById(R.id.qulineView);
        ArrayMap<String,Float> datas=new ArrayMap<>();
        datas.put("1",110f);
        datas.put("3",10f);
        datas.put("6",80f);
        datas.put("7",200f);
        datas.put("10",140f);
        datas.put("13",100f);
        datas.put("14",20f);
        qulineView.setData(datas);

        List<String> list=new ArrayList<>();
        list.add("0m");
        list.add("100m");
        list.add("200m");
        qulineView.setLeftData(list);

        List<MyQulineView.MyQulineBottomBean> bottomBeens=new ArrayList<>();
        bottomBeens.add(new MyQulineView.MyQulineBottomBean("5月1日"));
        bottomBeens.add(new MyQulineView.MyQulineBottomBean("2"));
        bottomBeens.add(new MyQulineView.MyQulineBottomBean("3"));
        bottomBeens.add(new MyQulineView.MyQulineBottomBean("4"));
        bottomBeens.add(new MyQulineView.MyQulineBottomBean("5月5日",true));
//        bottomBeens.add(new MyQulineView.MyQulineBottomBean("6",true));
//        bottomBeens.add(new MyQulineView.MyQulineBottomBean("7"));
        qulineView.setBottomData(bottomBeens);
    }
}
