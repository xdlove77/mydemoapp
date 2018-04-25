package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.example.dongao.mydemoapp.R;

/**
 * Created by fishzhang on 2018/4/19.
 */

public class ScrollTabsLayout extends ViewGroup {

    private int columns;
    private int rows;
    private int pageSize;

    private Scroller scroller;

    private int screenWidth;
    private int lineHeight;
    private ScrollTabListener scrollTabListener;
    private int touchSlop;
    private float downX;
    private float firstX;
    private int left;
    private int right;
    private int scrollDuration;

    public ScrollTabsLayout(Context context) {
        this(context, null);
    }

    public ScrollTabsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTabsLayout);
        columns = typedArray.getInt(R.styleable.ScrollTabsLayout_columns, 4);// todo 默认4列
        rows = typedArray.getInt(R.styleable.ScrollTabsLayout_rows, 1);// todo 默认1行
        lineHeight = typedArray.getDimensionPixelSize(R.styleable.ScrollTabsLayout_lineHeight, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 100.0f, context.getResources().getDisplayMetrics()));// todo 每行高度
        scrollDuration=typedArray.getInt(R.styleable.ScrollTabsLayout_scrollDuration, 200);// todo 默认200ms
        int interpolatorId=typedArray.getResourceId(R.styleable.ScrollTabsLayout_scrollDuration,-1);// todo 默认线性插值器
        Interpolator interpolator=null;
        if (interpolatorId==-1)
            interpolator=new LinearInterpolator();
        else
            interpolator= AnimationUtils.loadInterpolator(context, interpolatorId);
        scroller = new Scroller(context,interpolator);

        if (columns <= 0 || rows <= 0 || lineHeight < 0)
            throw new IllegalArgumentException("看源码吧 sb");
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                ? MeasureSpec.getSize(widthMeasureSpec)
                :screenWidth;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize / columns, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        if (childCount>0){
            int count=childCount/(rows*columns);
            int yu = childCount%(rows*columns);
            pageSize=count>0
                    ?yu>0
                        ?count+1
                        :count
                    :1;
        }
        setMeasuredDimension(widthSize,lineHeight*rows);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount()<=0)
            return;
        right=pageSize*getMeasuredWidth();
        int left=0,top=0,childCount=0;
        for (int i = 0; i < pageSize; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < columns; k++) {
                    View childView = getChildAt(childCount);
                    int right = left+childView.getMeasuredWidth();
                    int bottom = top+childView.getMeasuredHeight();
                    childView.layout(left,top,right,bottom);
                    left=right;
                    childCount++;
                    if (childCount==getChildCount())
                        return;
                }
                left=getMeasuredWidth()*i;
                top+=lineHeight;
            }
            top=0;
            left=getMeasuredWidth()*(i+1);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX = downX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs((firstX = x) - downX)>touchSlop){
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                downX=x;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                int scrollx= (int) (downX-x);
                if (getScrollX()+scrollx<left){
                    scrollTo(left,0);
                    return true;
                }else if (getScrollX() + scrollx +getWidth() > right){
                    scrollTo(right-getWidth(),0);
                    return true;
                }else{
                    scrollBy(scrollx,0);
                }
                downX=x;
                break;
            case MotionEvent.ACTION_UP:
                boolean scrollGoLeft=event.getX() - firstX >0;
                int index=(getScrollX()+getWidth()/2+ (scrollGoLeft?-getWidth()/6:getWidth()/6))/getWidth();
                int dist=index*getWidth()-getScrollX();
                scroller.startScroll(getScrollX(),0,dist,0,200);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (scrollTabListener!=null){
            scrollTabListener.scrolling(l-oldl);
            if (l==scroller.getFinalX())
                scrollTabListener.scrollEnd(scroller.getFinalX()/getWidth());
        }
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public int getScrollDuration() {
        return scrollDuration;
    }

    public void setInterpolator(Interpolator interpolator){
        scroller=new Scroller(getContext(),interpolator);
    }

    public void setScrollDuration(int scrollDuration) {
        this.scrollDuration = scrollDuration;
    }



    public void setScrollTabListener(ScrollTabListener scrollTabListener) {
        this.scrollTabListener = scrollTabListener;
    }

    public interface ScrollTabListener{
        void scrollEnd(int page);
        void scrolling(int dist);
    }
}