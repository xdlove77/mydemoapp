package com.example.dongao.mydemoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.dongao.mydemoapp.bean.ExpandaleFristBean;
import com.example.dongao.mydemoapp.bean.ExpandaleThridBean;
import com.example.dongao.mydemoapp.bean.ExpandaleTwoBean;
import com.example.dongao.mydemoapp.widget.CustomHeightGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ExpandableDemoActivity extends AppCompatActivity {
    private ExpandableListView elv1;
    private List<ExpandaleFristBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_demo);
        elv1= (ExpandableListView) findViewById(R.id.elv1);
        initData();
        FirstAdapter firstAdapter=new FirstAdapter(list,this);
        elv1.setAdapter(firstAdapter);

    }

    private void initData() {
        list=new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<ExpandaleThridBean> thridBeans=new ArrayList<>();
            for (int j = 0; j < 9-i; j++) {
                ExpandaleThridBean thridBean=new ExpandaleThridBean("class"+i+j);
                thridBeans.add(thridBean);
            }
            ExpandaleTwoBean twoBean=new ExpandaleTwoBean(thridBeans);
            ExpandaleFristBean fristBean=new ExpandaleFristBean("firstName"+i,"className"+i,twoBean);
            list.add(fristBean);
        }
    }


    public static class FirstAdapter extends BaseExpandableListAdapter{
        private List<ExpandaleFristBean> list;
        private Context context;

        public FirstAdapter(List<ExpandaleFristBean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).getBean();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.expandable_demo_first_layout,parent,false);
            }
            GroupViewHolder groupViewHolder = (GroupViewHolder) convertView.getTag();
            if (groupViewHolder==null){
                groupViewHolder=new GroupViewHolder();
                groupViewHolder.nametv= (TextView) convertView.findViewById(R.id.first_demo_tv);
                groupViewHolder.classNameTv= (TextView) convertView.findViewById(R.id.first_demo_class_name_tv);
                convertView.setTag(groupViewHolder);
            }

            if (isExpanded)
                groupViewHolder.classNameTv.setVisibility(View.VISIBLE);
            else
                groupViewHolder.classNameTv.setVisibility(View.GONE);
            groupViewHolder.classNameTv.setText(list.get(groupPosition).getClassName());
            groupViewHolder.nametv.setText(list.get(groupPosition).getKindName());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.expandable_demo_two_layout,parent,false);
            }
            ChildViewHolder childViewHolder = (ChildViewHolder) convertView.getTag();
            if (childViewHolder==null){
                childViewHolder=new ChildViewHolder();
                childViewHolder.recyclerView= (RecyclerView) convertView.findViewById(R.id.recyclerView);
                childViewHolder.recyclerView.setLayoutManager(new CustomHeightGridLayoutManager(context,3));
                childViewHolder.recyclerView.setNestedScrollingEnabled(false);

                convertView.setTag(childViewHolder);
            }
            childViewHolder.recyclerView.setAdapter(new ItemAdapter(context,list.get(groupPosition).getBean().getThridBeanList()));
            childViewHolder.recyclerView.addItemDecoration(new GridlayoutDivider(context));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
    
    
    static class GroupViewHolder{
        TextView nametv;
        TextView classNameTv;
    }

    static class ChildViewHolder{
        RecyclerView recyclerView;
    }

    static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        private Context context;
        private List<ExpandaleThridBean> list;

        public ItemAdapter(Context context, List<ExpandaleThridBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.expandable_demo_two_item_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.tv.setText(list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    static class GridlayoutDivider extends RecyclerView.ItemDecoration{
        private  Context context;

        public GridlayoutDivider(Context context) {
            this.context = context;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager){
                int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                Paint linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
                linePaint.setStyle(Paint.Style.FILL);
                int paintWidth = 2;
                linePaint.setStrokeWidth(paintWidth);
                linePaint.setColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                int screenW = context.getResources().getDisplayMetrics().widthPixels;
                int childCount = parent.getChildCount();
                int lineCount=childCount %spanCount !=0?childCount/spanCount+1:childCount/spanCount;
                View childView = parent.getChildAt(0);
                for (int i = 0; i <= lineCount; i++) {
                    if (lineCount!=1 && i==lineCount-1)
                        c.drawLine(0,i*childView.getMeasuredHeight()-paintWidth/2
                                ,screenW,i*childView.getMeasuredHeight()-paintWidth/2,linePaint);
                    else
                        c.drawLine(0,i*childView.getMeasuredHeight()
                                ,screenW,i*childView.getMeasuredHeight(),linePaint);

                }
                paintWidth = paintWidth/2;
                linePaint.setStrokeWidth(paintWidth);
                c.drawLine(screenW/spanCount,0,screenW/spanCount,parent.getMeasuredHeight(),linePaint);
                c.drawLine(screenW/spanCount*2,0,screenW/spanCount*2,parent.getMeasuredHeight(),linePaint);
            }
        }
    }
}
