package com.example.dongao.mydemoapp.widget.ptrviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 *  pull等状态时回拉时事件可以传递给viewpager 不过有卡顿问题 （原因：传递给viewpager时回卡顿一下，估计是viewpager
 *  的touchSlop问题 不是很确定）
 */
@Deprecated
public class PtrViewPager extends ViewGroup {
    private static final int PULL = 1;
    private static final int NEED_RELEASE = PULL << 1;
    private static final int LOADING = PULL << 2;
    private static final int FINISH = PULL << 3;
    private static final int END_FOR_LOAD_MORE = PULL << 4;
    private static final int END_FOR_REFRESH = PULL << 5;
    private static final int ERROR = PULL << 6;
    private int loadMode = PULL;

    private Scroller scroller;
    private int screenWidth;
    private int screenHeight;
    private int touchSlop;

    private ViewPager viewPager;
    private View refreshView;
    private View loadMoreView;

    private PtrLoadListener loadListener;

    private PtrRefreshAndLoadMoreViewListener loadMoreListener;
    private PtrRefreshAndLoadMoreViewListener refreshListener;

    private boolean refreshEnable;
    private boolean loadMoreEnable;
    private boolean isLoadMore;
    private boolean isRefresh;
    private float downX;
    private float lastX;
    private float interceptX;
    private boolean needStopEvent;
    private int scaledPagingTouchSlop;

    public PtrViewPager(Context context) {
        this(context, null);
    }

    public PtrViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        scroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
        scaledPagingTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        viewPager = new ViewPager(context, attrs);
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
        addView(viewPager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                ? MeasureSpec.getSize(widthMeasureSpec)
                : screenWidth;
        int height = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                ? MeasureSpec.getSize(heightMeasureSpec)
                : screenHeight;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            final LayoutParams lp = child.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    0, lp.width);
            child.measure(childWidthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (child == refreshView) {
                child.layout(-childWidth, 0, 0, height);
            } else if (child == loadMoreView) {
                child.layout(width, 0, width + childWidth, height);
            } else {
                child.layout(0, 0, width, height);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (loadMoreEnable || refreshEnable) {
            float touchX = ev.getX();
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (ev.getPointerId(ev.getActionIndex())==0){
                        lastX = downX = touchX;
                        downScrollX = getScrollX();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isRefresh || isLoadMore)
                        return true;

                    float scrollX = touchX - lastX;
                    if (loadMoreEnable && !viewPager.canScrollHorizontally(1) && scrollX < 0) {
                        isLoadMore = true;
                        isRefresh = false;
                        if (loadMode != END_FOR_LOAD_MORE)
                            loadMode = PULL;
                        else{
                            loadMoreListener.end();
                            needStopEvent = false;
                        }
                        interceptX = touchX;
                        return true;
                    } else if (refreshEnable && !viewPager.canScrollHorizontally(-1) && scrollX > 0) {
                        isRefresh = true;
                        isLoadMore = false;
                        if (loadMode != END_FOR_REFRESH)
                            loadMode = PULL;
                        else{
                            refreshListener.end();
                            needStopEvent = false;
                        }
                        interceptX = touchX;
                        return true;
                    }
                    isLoadMore = false;
                    isRefresh = false;
                    lastX = touchX;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    private int downScrollX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (needStopEvent) {
            return true;
        }
        PtrRefreshAndLoadMoreViewListener listener;
        int viewWidth;
        if (isLoadMore) {
            listener = loadMoreListener;
            viewWidth = loadMoreView.getMeasuredWidth();
        } else if (isRefresh) {
            listener = refreshListener;
            viewWidth = refreshView.getMeasuredWidth();
        } else
            return true;
        float touchX = event.getX();
        int scrollDist = getScrollX();
        float value = downX - touchX;
        int scrollX =  Math.round(value >= 2f || value <= -2f ? value / 2 : value);
//        if (isLoadMore) {
//            if (scrollX <= 0 && (int)lastX - touchX <= 0 && viewPager.canScrollHorizontally(-1) && scrollDist == 0) {
//                lastX = touchX;
//                Log.d("hhhh","++++++++++++++++");
//                return viewPager.onTouchEvent(event);
//            } else if (viewPager.canScrollHorizontally(1)) {
//                lastX = touchX;
//                return viewPager.onTouchEvent(event);
//            }
//        } else {
//            if (scrollX >= 0 && (int)lastX - touchX >= 0 && viewPager.canScrollHorizontally(1) && scrollDist == 0) {
//                lastX = touchX;
//                return viewPager.onTouchEvent(event);
//            } else if (viewPager.canScrollHorizontally(-1)) {
//                lastX = touchX;
//                return viewPager.onTouchEvent(event);
//            }
//        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerId(event.getActionIndex())==0){
                    int tempScrollX;
                    if (loadMode != PULL && loadMode != NEED_RELEASE) {
                        tempScrollX = scrollX + downScrollX;
                    } else {
                        tempScrollX = scrollX;
                    }
                    Log.d("hhhh", "touchX=" + touchX + "; lastX=" + lastX +"; scrollX="+scrollX+"; scrollDist=" + scrollDist + "; tempScrollX=" + tempScrollX+"; downScrollX="+downScrollX);
                    if ((tempScrollX <= 0 && scrollDist > 0) || (tempScrollX >= 0 && scrollDist < 0)) {
                        Log.d("hhhh","------------------");
                        tempScrollX = 0;
//                        scrollTo(0, 0);
//                        event.setAction(MotionEvent.ACTION_DOWN);
//                        viewPager.onTouchEvent(event);
//                        event.setLocation(touchX+scaledPagingTouchSlop,event.getY());
//                        event.setAction(MotionEvent.ACTION_MOVE);
//                        return viewPager.onTouchEvent(event);
                    }
                    scrollTo(tempScrollX, 0);
                    if (loadMode == PULL || loadMode == NEED_RELEASE) {
                        float dist = 1.0f * Math.abs(scrollDist) / viewWidth;
                        if (dist >= 1) {
                            if (loadMode != NEED_RELEASE) {
                                listener.needRelease();
                                loadMode = NEED_RELEASE;
                            }
                        } else {
                            listener.pull(dist);
                            loadMode = PULL;
                        }
                    }
                    lastX = touchX;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(event.getActionIndex())==0){
                    switch (loadMode) {
                        case PULL:
                            viewPager.onTouchEvent(event);
                            scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                            break;
                        case NEED_RELEASE:
                            loadMode = LOADING;
                            scroller.startScroll(scrollDist, 0, isLoadMore ? viewWidth - scrollDist : scrollDist + viewWidth, 0);
                            listener.loading();
                            if (isLoadMore)
                                loadListener.loadMore();
                            else
                                loadListener.refresh();
                            break;
                        case LOADING:
                            if (isLoadMore && (scrollDist - viewWidth) > 0) {
                                scroller.startScroll(scrollDist, 0, viewWidth - scrollDist, 0);
                            } else if (isRefresh && (scrollDist + viewWidth) < 0) {
                                scroller.startScroll(scrollDist, 0, Math.abs(scrollDist) - viewWidth, 0);
                            }
                            break;
                        case END_FOR_LOAD_MORE:
                        case END_FOR_REFRESH:
                            scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                            break;
                    }
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
            if (scroller.getCurrX() == 0) {
                switch (loadMode) {
                    case END_FOR_LOAD_MORE:
                    case END_FOR_REFRESH:
                    case ERROR:
                    case FINISH:
                        needStopEvent = false;
                        break;
                    case LOADING:
                        break;
                    case PULL:
                        isLoadMore = false;
                        isRefresh = false;
                        break;
                }
            }
        }
    }

    public void loadOrRefreshFinish() {
        finish(FINISH);
    }

    public void refreshEnd() {
        finish(END_FOR_REFRESH);
    }

    public void loadMoreEnd(){
        finish(END_FOR_LOAD_MORE);
    }

    public void loadError() {
        finish(ERROR);
    }

    private void finish(int mode) {
        needStopEvent = true;
        loadMode = mode;
        PtrRefreshAndLoadMoreViewListener listener = null;
        if (isLoadMore) {
            listener = loadMoreListener;
        } else if (isRefresh) {
            listener = refreshListener;
        }
        if (listener !=null){
            switch (mode) {
                case FINISH:
                    listener.finish();
                    break;
                case END_FOR_LOAD_MORE:
                case END_FOR_REFRESH:
                    listener.end();
                    break;
                case ERROR:
                    listener.error();
                    break;
            }
            isLoadMore = false;
            isRefresh = false;
        }else {
            return;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollX = getScrollX();
                scroller.startScroll(scrollX, 0, -scrollX, 0);
                invalidate();
            }
        }, 300);
    }

    public void setRefreshView(View view) {
        if (!(view instanceof PtrRefreshAndLoadMoreViewListener))
            throw new IllegalArgumentException("view need implements PtrRefreshAndLoadMoreViewListener");
        if (refreshView != null)
            removeView(refreshView);
        refreshEnable = true;
        refreshListener = (PtrRefreshAndLoadMoreViewListener) view;
        refreshView = view;
        addView(view, 0);
    }

    public void setLoadMoreView(View view) {
        if (!(view instanceof PtrRefreshAndLoadMoreViewListener))
            throw new IllegalArgumentException("view need implements PtrRefreshAndLoadMoreViewListener");
        if (loadMoreView != null)
            removeView(loadMoreView);
        loadMoreEnable = true;
        loadMoreListener = (PtrRefreshAndLoadMoreViewListener) view;
        loadMoreView = view;
        addView(view);
    }


    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setLoadListener(PtrLoadListener loadListener) {
        this.loadListener = loadListener;
    }
}
