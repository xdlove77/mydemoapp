package com.example.dongao.mydemoapp.widget.ptrviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 去掉pull状态时回拉的touch事件可以传递给viewpager 并删除头部view（原因：短时间内顾及不到头部的功能 所以阉割掉）
 */
public class PtrNoRefreshViewPager extends ViewGroup {
    private static final int PULL = 1;
    private static final int NEED_RELEASE = PULL << 1;
    private static final int LOADING = PULL << 2;
    private static final int FINISH = PULL << 3;
    private static final int END_FOR_LOAD_MORE = PULL << 4;
    private static final int ERROR = PULL << 5;
    private int loadMode = PULL;

    private Scroller scroller;
    private int screenWidth;
    private int screenHeight;
    private boolean isLoadMore;

    private ViewPager viewPager;
    private View loadMoreView;

    private PtrLoadListener loadListener;
    private PtrRefreshAndLoadMoreViewListener loadMoreListener;
    private boolean loadMoreEnable;
    private float downX;
    private float lastX;
    private boolean needStopEvent;
    private int downScrollX;

    public PtrNoRefreshViewPager(Context context) {
        this(context, null);
    }

    public PtrNoRefreshViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        removeAllViews();
        scroller = new Scroller(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        viewPager = new ViewPager(context, attrs);
        viewPager.setLayoutParams(new LayoutParams(
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
            if (child == loadMoreView) {
                child.layout(width, 0, width + childWidth, height);
            } else {
                child.layout(0, 0, width, height);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (loadMoreEnable) {
            float touchX = ev.getX();
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = downX = touchX;
                    downScrollX = getScrollX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ev.getPointerId(ev.getActionIndex()) == 0) {
                        if (isLoadMore)
                            return isLoadMore;
                        float scrollX = touchX - lastX;
                        if (!viewPager.canScrollHorizontally(1) && scrollX < 0) {
                            isLoadMore = true;
                            if (loadMode == LOADING) {
                                loadMoreListener.loading();
                                needStopEvent = false;
                            } else if (loadMode != END_FOR_LOAD_MORE)
                                loadMode = PULL;
                            else {
                                loadMoreListener.end();
                                needStopEvent = false;
                            }
                            return true;
                        }
                        isLoadMore = false;
                        lastX = touchX;
                    }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (needStopEvent) {
            return false;
        }
        if (loadMoreListener == null || !isLoadMore) {
            return false;
        }
        int viewWidth = loadMoreView.getMeasuredWidth();
        float touchX = event.getX();
        int scrollDist = getScrollX();
        float value = downX - touchX;
        int scrollX = (int) (value >= 2f || value <= -2f ? value / 2 : value);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    if (touchX > lastX && scrollDist > 0) {
                        needStopEvent = true;
                        scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                        invalidate();
                        return false;
                    }
                    int tempScrollX;
                    if (loadMode == LOADING) {
                        tempScrollX = scrollX + downScrollX;
                    } else {
                        tempScrollX = scrollX;
                    }
                    scrollTo(tempScrollX, 0);
                    if (loadMode == PULL || loadMode == NEED_RELEASE) {
                        float dist = 1.0f * Math.abs(scrollDist) / viewWidth;
                        if (dist >= 1) {
                            if (loadMode != NEED_RELEASE) {
                                loadMoreListener.needRelease();
                                loadMode = NEED_RELEASE;
                            }
                        } else {
                            loadMoreListener.pull(dist);
                            loadMode = PULL;
                        }
                    }
                    lastX = touchX;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                switch (loadMode) {
                    case PULL:
                        scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                        break;
                    case NEED_RELEASE:
                        loadMode = LOADING;
                        scroller.startScroll(scrollDist, 0, viewWidth - scrollDist, 0);
                        loadMoreListener.loading();
                        loadListener.loadMore();
                        break;
                    case LOADING:
                        if (scrollDist - viewWidth > 0) {
                            scroller.startScroll(scrollDist, 0, viewWidth - scrollDist, 0);
                        } else {
                            scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                        }
                        break;
                    case END_FOR_LOAD_MORE:
                        scroller.startScroll(scrollDist, 0, -scrollDist, 0);
                        break;
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (loadMode == PULL && loadMoreListener != null)
                loadMoreListener.pull(1.0f * scroller.getCurrX() / loadMoreView.getMeasuredWidth());
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
            if (scroller.getCurrX() == 0) {
                isLoadMore = false;
                needStopEvent = false;
            }
        }
    }

    public void loadFinish() {
        finish(FINISH);
    }

    public void loadEnd() {
        finish(END_FOR_LOAD_MORE);
    }

    public void loadError() {
        finish(ERROR);
    }

    public void setLoading(boolean isLoading) {
        if (isLoading)
            loadMode = LOADING;
        else
            loadMode = PULL;
    }

    private void finish(int mode) {
        isLoadMore = false;
        needStopEvent = true;
        loadMode = mode;
        if (loadMoreListener != null) {
            switch (mode) {
                case FINISH:
                    loadMoreListener.finish();
                    break;
                case END_FOR_LOAD_MORE:
                    loadMoreListener.end();
                    break;
                case ERROR:
                    loadMoreListener.error();
                    break;
            }
        } else {
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


    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setLoadListener(PtrLoadListener loadListener) {
        this.loadListener = loadListener;
    }
}
