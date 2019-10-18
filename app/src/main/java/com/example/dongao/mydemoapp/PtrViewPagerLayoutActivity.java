package com.example.dongao.mydemoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dongao.mydemoapp.widget.PtrViewPagerLayout;

import java.util.ArrayList;
import java.util.List;

public class PtrViewPagerLayoutActivity extends AppCompatActivity {

    private PtrViewPagerLayout ptrViewPagerLayout;
    private RecyclerView rv;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_view_pager_layout);
        ptrViewPagerLayout = findViewById(R.id.ptrViewPagerLayout);

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rv);

        adapter = new MyAdapter(this);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add("index i = "+i);
        }
        adapter.setData(data);
        rv.setAdapter(adapter);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyVH> {

        private final int[] colors = {0xffffff00,0xffcccccc,0xffc03a56};
        private ArrayList<String> list;
        private Context context;

        public MyAdapter(Context context) {
            this.list = new ArrayList<>();
            this.context = context;
        }

        public void setData(List<String> list){
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyVH(LayoutInflater.from(context).inflate(R.layout.ptr_view_pager_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyVH holder, int position) {
            holder.tv.setText(list.get(position));
            holder.tv.setBackgroundColor(colors[position%colors.length]);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class MyVH extends RecyclerView.ViewHolder {
        public TextView tv;
        public MyVH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textView);
        }
    }
}
