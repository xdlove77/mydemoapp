package com.example.dongao.mydemoapp;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dongao.mydemoapp.widget.ptrviewpager.LoadMoreView;
import com.example.dongao.mydemoapp.widget.ptrviewpager.PtrLoadListener;
import com.example.dongao.mydemoapp.widget.ptrviewpager.PtrNoRefreshViewPager;
import com.example.dongao.mydemoapp.widget.ptrviewpager.PtrViewPager;
import com.example.dongao.mydemoapp.widget.ptrviewpager.PtrViewPager2;

import java.util.ArrayList;

public class LoadMoreViewPagerActivity extends AppCompatActivity {
    private PtrNoRefreshViewPager viewPager;
    ArrayList<View> views;
    private int[] colors = {0xffffffff,0xffcccccc,0xffff0000,0xff00ff00,0xffffff00};
    private TestAdapter adapter;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more_view_pager);
        handler = new Handler();
        viewPager=(PtrNoRefreshViewPager)findViewById(R.id.viewPager);
        viewPager.setLoadMoreView(new LoadMoreView(this));
        views = new ArrayList<>();
        adapter = new TestAdapter();
        loadData();
        viewPager.getViewPager().setAdapter(adapter);
        viewPager.setLoadListener(new PtrLoadListener() {

            private boolean isError = true;

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (views.size()>=5 && isError) {
                            isError = false;
                            viewPager.loadError();
                            return;
                        }
                        loadData();
//                        viewPager.loadMoreEnd();
                        viewPager.loadFinish();
//                        viewPager.loadOrRefreshFinish();
                    }
                },5000);
            }

            @Override
            public void refresh() {

            }
        });

    }

    private void loadData() {
        for (int i = 0;i<5;i++){
            TextView view = new TextView(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            view.setText("child num = "+i);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundColor(colors[i]);
            views.add(view);
        }
        adapter.notifyDataSetChanged();
    }

    private class TestAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
