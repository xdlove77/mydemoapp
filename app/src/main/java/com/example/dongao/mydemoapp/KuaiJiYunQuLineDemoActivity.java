package com.example.dongao.mydemoapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.ConnectionService;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.dongao.mydemoapp.widget.AVLoadingImageView;
import com.example.dongao.mydemoapp.widget.CircleProgressView;
import com.example.dongao.mydemoapp.widget.LeiDaView;
import com.example.dongao.mydemoapp.widget.MyQulineView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class KuaiJiYunQuLineDemoActivity extends AppCompatActivity {
    private MyQulineView qulineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuai_ji_yun_qu_line_demo);
        qulineView= (MyQulineView) findViewById(R.id.qulineView);
        List<Float> datas=new ArrayList<>();
        datas.add(110f);
        datas.add(10f);
        datas.add(80f);
        datas.add(200f);
        datas.add(140f);
        datas.add(100f);
        datas.add(20f);
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

        final TextView progressTv= (TextView) findViewById(R.id.progressTv);
        CircleProgressView circleProgressView = (CircleProgressView) findViewById(R.id.CircleProgressView);
        circleProgressView.setDataWithAnim(0.6f,3000,new LinearInterpolator());
        circleProgressView.setProgressListener(new CircleProgressView.CircleProgressLinstener() {
            @Override
            public void onProgress(float precent) {
                progressTv.setText((int)(precent*100)+"%");
            }
        });


//        Glide.with(this)
//                .asBitmap()
//                .load("")
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .apply(RequestOptions.placeholderOf(123))
//                .apply(RequestOptions.errorOf(123))
//                .apply(RequestOptions.overrideOf(10,10))
//                .into(new ImageView(this));

//        AVLoadingImageView avimage= (AVLoadingImageView) findViewById(R.id.avimage);
//        avimage.startAnim();

        List<LeiDaView.LeiDaData> data=new ArrayList<>();
        data.add(new LeiDaView.LeiDaData("计划执行力",0.7f));
        data.add(new LeiDaView.LeiDaData("记忆力",0.8f));
        data.add(new LeiDaView.LeiDaData("学习态度",0.7f));
        data.add(new LeiDaView.LeiDaData("理解力",1f));
        data.add(new LeiDaView.LeiDaData("学习效果",0.9f));

//        LeiDaView leiDaView= (LeiDaView) findViewById(R.id.leidaview);
//        leiDaView.setDataWithAnim(data);

    }

}
