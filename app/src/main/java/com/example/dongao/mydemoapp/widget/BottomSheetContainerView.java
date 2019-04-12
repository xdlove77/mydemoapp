package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.dongao.mydemoapp.R;

public class BottomSheetContainerView extends FrameLayout {

    private RecyclerView recyclerView;
    private int itemH;

    public BottomSheetContainerView(Context context) {
        this(context,null);
    }

    public BottomSheetContainerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public BottomSheetContainerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemH = context.getResources().getDisplayMetrics().heightPixels / 3;
        addView(LayoutInflater.from(context).inflate(R.layout.bottom_sheet_container_view_layout,this,false));
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyAdapter(context));

    }

    class MyAdapter extends RecyclerView.Adapter<MyVH>{

        private String[] colors = {"#FF4081", "#3F51B5", "#303F9F"};

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = new View(context);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    itemH
            ));
            return new MyVH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyVH holder, int position) {
            holder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    class MyVH extends RecyclerView.ViewHolder{

        public MyVH(View itemView) {
            super(itemView);
        }
    }
}
