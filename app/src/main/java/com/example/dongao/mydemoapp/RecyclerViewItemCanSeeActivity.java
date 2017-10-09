package com.example.dongao.mydemoapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dongao.mydemoapp.widget.LockView;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewItemCanSeeActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bt)
    FloatingActionButton bt;

    List<String> data;
    int fposition=15;
    int lposition=20;
    int fpt=0;
    int lpt=0;
    boolean isGoUp=false;
    boolean isGoDown=false;
    RefreshLayout refreshLayout;
    boolean isBtActionDown;
    private boolean isFirstInNeedScroll;
    int itemHeight;
    int rscrolly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_item_can_see);
        ButterKnife.bind(this);

        data=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("data position : "+i);
        }
        bt = (FloatingActionButton) findViewById(R.id.bt);
        bt.setVisibility(View.INVISIBLE);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtActionDown=true;
                bt.setVisibility(View.GONE);
                if (isGoDown) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    View child = layoutManager.getChildAt(fpt);
                    if ( itemHeight <=0 && child!=null ) {
                        itemHeight =child.getMeasuredHeight();
                    }
                    recyclerView.smoothScrollBy(0, (fposition)* itemHeight - rscrolly);
                } else if (isGoUp) {
                    recyclerView.smoothScrollToPosition(fposition);
                }
            }
        });

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);

        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(250,true);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(250,true);
            }
        });
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader refreshHeader, float v, int i, int i1, int i2) {

            }

            @Override
            public void onHeaderReleasing(RefreshHeader refreshHeader, float v, int i, int i1, int i2) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader refreshHeader, int i, int i1) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader refreshHeader, boolean b) {
                refreshLayout.setRefreshFinished(true);
            }

            @Override
            public void onFooterPulling(RefreshFooter refreshFooter, float v, int i, int i1, int i2) {

            }

            @Override
            public void onFooterReleasing(RefreshFooter refreshFooter, float v, int i, int i1, int i2) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter refreshFooter, int i, int i1) {

            }

            @Override
            public void onFooterFinish(RefreshFooter refreshFooter, boolean b) {
                refreshLayout.setLoadmoreFinished(true);
            }

            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState refreshState, RefreshState refreshState1) {

            }
        });

        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(data,this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int fp = layoutManager.findFirstVisibleItemPosition();
                int lp= layoutManager.findLastVisibleItemPosition();
                rscrolly+=dy;
                if (fpt != fp || lpt != lp){
                    if (fposition-1 >= 0 && fposition-1>=lp){
                        if (!isBtActionDown) {
                            isGoDown = true;
                            isGoUp = false;
                            bt.setVisibility(View.VISIBLE);
                            bt.setBackgroundDrawable(RecyclerViewItemCanSeeActivity.this.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }else if(lposition+1<=data.size() && lposition +1 <= fp){
                        if (!isBtActionDown) {
                            isGoDown=false;
                            isGoUp=true;
                            bt.setVisibility(View.VISIBLE);
                            bt.setBackgroundDrawable(RecyclerViewItemCanSeeActivity.this.getResources().getDrawable(R.mipmap.aa));
                        }
                    }else{
                        isBtActionDown=false;
                        isGoDown=false;
                        isGoUp=false;
                        bt.setVisibility(View.GONE);
                    }
                }
                fpt=fp;
                lpt=lp;

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isBtActionDown && newState == RecyclerView.SCROLL_STATE_IDLE){

                }
                if (isFirstInNeedScroll && newState == RecyclerView.SCROLL_STATE_IDLE){
                    isFirstInNeedScroll =false;
                    refreshLayout.setEnableLoadmore(true);
                }
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN && isBtActionDown){
                    isBtActionDown=false;
                    bt.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

//        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
//            @Override
//            public void onChildViewAttachedToWindow(View view) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int measuredHeight = layoutManager.getChildAt(fpt).getMeasuredHeight();
//                if (measuredHeight!=0){
//                    int cp=-1;
//                    for (int i = 0; i < data.size(); i++) {
//                        LiveListBean liveListBean = data.get(i);
//                        if (liveListBean.isIsToday() && fposition==-1){
//                            fposition=i;
//                        }
//                        if (liveListBean.isIsToday() && i==data.size()-1){
//                            lposition=i;
//                        } else if (!liveListBean.isIsToday() && fposition!=-1 && lposition==-1){
//                            lposition=i-1;
//                        }
//                        if (cp==-1 && liveListBean.isIsFuture()){
//                            cp=i;
//                        }
//                    }
//
//                    if (fposition != -1 ){
//                        refreshLayout.setEnableLoadmore(false);
//                        haveTodayData=true;
//                        isFirstInNeedScroll=true;
//                        recyclerView.smoothScrollBy(0, (fposition)*measuredHeight);
//                    }else if (cp!=-1){
//                        refreshLayout.setEnableLoadmore(false);
//                        isFirstInNeedScroll=true;
//                        recyclerView.smoothScrollBy(0, (cp)*measuredHeight);
//                    }
//
//                    recyclerView.removeOnChildAttachStateChangeListener(this);
//                }
//            }
//
//            @Override
//            public void onChildViewDetachedFromWindow(View view) {
//
//            }
//        });

    }


    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private List<String> data;
        private Context context;

        public MyAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_recycler_view_item_can_see_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(data.get(position));
            int color=Color.BLACK;
            switch (position%3){
                case 0:
                    color= Color.GRAY;
                    break;
                case 1:
                    color=Color.GREEN;
                    break;
                case 2:
                    color=Color.BLUE;
                    break;
            }
            holder.layout.setBackgroundColor(color);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
