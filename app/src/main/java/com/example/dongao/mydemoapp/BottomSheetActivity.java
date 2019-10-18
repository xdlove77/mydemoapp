package com.example.dongao.mydemoapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.dongao.mydemoapp.widget.BottomSheetNestedLayout;
import com.example.dongao.mydemoapp.widget.BottomSheetContainerView;
import com.example.dongao.mydemoapp.widget.layoutmanager.NestedScrollLinearLayoutManager;

public class BottomSheetActivity extends AppCompatActivity {

    private static final int BOTTOM_TAG = 0;

    private FrameLayout bottomSheetLayout;
    private RecyclerView rootRecyclerView;
    private View titleTv;
    private int screenH;
    private int titleH;
    private BottomSheetContainerView bottomSheetContainerView;
    private int peekHeight;
    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    private BottomSheetNestedLayout bottomSheetNestedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        initView();
    }

    private void initView() {
        titleTv = findViewById(R.id.titleTv);
        titleTv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titleH = titleTv.getBottom();
                titleTv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setBottomSheetH();
            }
        });
        
        rootRecyclerView = findViewById(R.id.recyclerView);
        rootRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                View targetChild = null;
                for (int i = 0; i < layoutManager.getChildCount(); i++) {
                    View child = layoutManager.getChildAt(i);
                    if (recyclerView.getChildViewHolder(child) instanceof BottomSheetVH){
                        targetChild = child;
                        break;
                    }
                }
                if (targetChild != null){
                    RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(targetChild);
                    if (vh instanceof BottomSheetVH){
                        ViewGroup rootView = (ViewGroup) vh.itemView;
                        int h = screenH - targetChild.getTop() - titleH;
                        if (peekHeight >= h){
                            if (rootView.findViewWithTag(BOTTOM_TAG) != null){
                                rootView.removeAllViews();
                            }
                            if (bottomSheetLayout.findViewWithTag(BOTTOM_TAG) == null){
                                bottomSheetLayout.setVisibility(View.VISIBLE);
                                bottomSheetLayout.addView(bottomSheetContainerView);
                            }
                        } else {
                            if (bottomSheetLayout.findViewWithTag(BOTTOM_TAG) != null){
                                bottomSheetLayout.removeAllViews();
                                bottomSheetLayout.setVisibility(View.GONE);
                            }
                            if (rootView.findViewWithTag(BOTTOM_TAG) == null){
                                rootView.addView(bottomSheetContainerView);
                            }
                        }
                    }
                }
            }
        });
        rootRecyclerView.setAdapter(new RootRecyclerAdapter(this));

        bottomSheetNestedLayout = findViewById(R.id.bottomSheetNestedLayout);
        bottomSheetNestedLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenH = bottomSheetNestedLayout.getBottom();
                NestedScrollLinearLayoutManager lm = (NestedScrollLinearLayoutManager) rootRecyclerView.getLayoutManager();
                lm.setScreenH(screenH);
                setBottomSheetH();
                bottomSheetNestedLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        bottomSheetLayout = findViewById(R.id.bottomLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        peekHeight = bottomSheetBehavior.getPeekHeight();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        
        bottomSheetContainerView = new BottomSheetContainerView(this);
        bottomSheetContainerView.setTag(BOTTOM_TAG);
        bottomSheetLayout.addView(bottomSheetContainerView);
        bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setBottomSheetH() {
        if (screenH == 0 || titleH == 0)
            return;
        bottomSheetLayout.getLayoutParams().height = screenH - titleH;
        bottomSheetLayout.requestLayout();
    }

    private void recycleBottomSheetVH(BottomSheetVH vh) {
        View view = vh.itemView.findViewWithTag(BOTTOM_TAG);
        if (view != null){
            ((ViewGroup)(vh.itemView)).removeAllViews();
        }
        if (bottomSheetLayout.findViewWithTag(BOTTOM_TAG) == null){
            bottomSheetLayout.setVisibility(View.VISIBLE);
            bottomSheetLayout.removeAllViews();
            bottomSheetLayout.addView(bottomSheetContainerView);
        }
    }

    private void checkBottomSheetVHBind(BottomSheetVH vh){
        final ViewGroup itemView = (ViewGroup) vh.itemView;
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int h = screenH - itemView.getTop() - titleH;
                if (peekHeight >= h){
                    if (itemView.findViewWithTag(BOTTOM_TAG) != null){
                        itemView.removeAllViews();
                    }
                    if (bottomSheetLayout.findViewWithTag(BOTTOM_TAG) == null){
                        bottomSheetLayout.setVisibility(View.VISIBLE);
                        bottomSheetLayout.addView(bottomSheetContainerView);
                    }
                } else {
                    if (bottomSheetLayout.findViewWithTag(BOTTOM_TAG) != null){
                        bottomSheetLayout.removeAllViews();
                        bottomSheetLayout.setVisibility(View.GONE);
                    }
                    if (itemView.findViewWithTag(BOTTOM_TAG) == null){
                        itemView.addView(bottomSheetContainerView);
                    }
                }
            }
        });
    }


    class RootRecyclerAdapter extends RecyclerView.Adapter<BaseVH> {
        private int size = 4;
        private Context context;
        private String[] colors = {"#ff0000", "#00ff00"};

        public RootRecyclerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            return (position + 1) % size == 0 ? 1 : 0;
        }

        @NonNull
        @Override
        public BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BaseVH vh = null;
            if (viewType == 0) {
                View view = new View(context);
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                vh = new OtherVH(view);
            } else if (viewType == 1) {
                View view = new FrameLayout(context);
                view.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                vh = new BottomSheetVH(view);
            }
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseVH holder, int position) {
            if (holder instanceof OtherVH){
                holder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
            } else if (holder instanceof BottomSheetVH){
                checkBottomSheetVHBind((BottomSheetVH) holder);
            }
        }

        @Override
        public void onViewRecycled(@NonNull BaseVH holder) {
            super.onViewRecycled(holder);
            holder.onViewRecycled();
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull BaseVH holder) {
            super.onViewDetachedFromWindow(holder);
            holder.onViewDetachedFromWindow();
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }

    class OtherVH extends BaseVH {

        public OtherVH(View itemView) {
            super(itemView);
        }
    }

    class BottomSheetVH extends BaseVH {

        public BottomSheetVH(View itemView) {
            super(itemView);
        }

        @Override
        public void onViewRecycled() {
            super.onViewRecycled();
        }

        @Override
        public void onViewDetachedFromWindow() {
            super.onViewDetachedFromWindow();
            recycleBottomSheetVH(this);
        }
    }

    abstract class BaseVH extends RecyclerView.ViewHolder{

        public BaseVH(View itemView) {
            super(itemView);
        }

        public void onViewRecycled(){
        }

        public void onViewDetachedFromWindow(){

        }
    }
}
