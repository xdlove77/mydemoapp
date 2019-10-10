package com.example.dongao.mydemoapp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.*;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.dongao.mydemoapp.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class LockPatternView extends View {

    public static final int VIRTUAL_BASE_VIEW_ID = 1;

    private static final int DEFAULT_CELL_SIZE = 3;
    private static final int DEFAULT_ERROR_COLOR = 0xffff9502;
    private static final int DEFAULT_SELECTED_COLOR = 0xff237aef;
    private static final float DEFAULT_CLEAR_ERROR_STATUS_DELAYED_TIME = 1f;

    private PatternExploreByTouchHelper mExploreByTouchHelper;

    private OnPatternListener mOnPatternListener;

    private Handler mHandler = new Handler();
    private Runnable clearErrorStatusRunnable;
    private float mErrorDelayedTime;

    private Rect mCellsArea;
    private Rect[][] mCellRects;
    private boolean[][] mPatternDrawLookup;
    private List<Cell> mCellList;
    private boolean mPatternInProgress = false;

    private int mCellCount;
    private int mCellSize;
    private int mCellPadding;
    private boolean mInStealthMode;
    private int mLineWidth;
    private boolean mCanRepeat = false;
    private int mNormalColor;
    private int mCellColor;
    private int mErrorColor;
    private Drawable mNormalDrawable;
    private Drawable mSelectedDrawable;
    private Drawable mErrorDrawable;
    private boolean mLineBelowCell;
    private boolean mDrawDefault;
    private Paint mPaint;
    private Paint mLinePaint;
    private boolean mIsErrorStatus;
    private int mInProgressX;
    private int mInProgressY;
    private int cellWidth;

    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LockPatternView);
        mCellSize = typedArray.getInt(R.styleable.LockPatternView_cellSize, DEFAULT_CELL_SIZE);
        mCellPadding = typedArray.getDimensionPixelOffset(R.styleable.LockPatternView_cellPadding, -1);
        mInStealthMode = typedArray.getBoolean(R.styleable.LockPatternView_inStealthMode, false);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.LockPatternView_lineWidth, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3f, displayMetrics
        ));
        mCanRepeat = typedArray.getBoolean(R.styleable.LockPatternView_repeat, false);

        mNormalColor = typedArray.getColor(R.styleable.LockPatternView_normalColor, DEFAULT_SELECTED_COLOR);
        mCellColor = typedArray.getColor(R.styleable.LockPatternView_cellColor, DEFAULT_SELECTED_COLOR);
        mErrorColor = typedArray.getColor(R.styleable.LockPatternView_errorColor, DEFAULT_ERROR_COLOR);

        int flag = 0;
        mErrorDrawable = typedArray.getDrawable(R.styleable.LockPatternView_errorDrawable);
        flag += mErrorDrawable != null ? 1 : 0;
        mNormalDrawable = typedArray.getDrawable(R.styleable.LockPatternView_normalDrawable);
        flag += mNormalDrawable != null ? 1 : 0;
        mSelectedDrawable = typedArray.getDrawable(R.styleable.LockPatternView_selectedDrawable);
        flag += mSelectedDrawable != null ? 1 : 0;

        mErrorDelayedTime = typedArray.getFloat(R.styleable.LockPatternView_clearErrorDelayedTime, DEFAULT_CLEAR_ERROR_STATUS_DELAYED_TIME);
        mLineBelowCell = typedArray.getBoolean(R.styleable.LockPatternView_lineBelowCell, true);

        typedArray.recycle();

        if (flag == 0) {
            mDrawDefault = true;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        } else if (flag != 3) {
            throw new InvalidParameterException("error、normal、select的drawable 有一个不为空就都不能为空");
        }

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(mLineWidth);

        mPatternDrawLookup = new boolean[mCellSize][mCellSize];

        mCellCount = mCellSize * mCellSize;
        mCellRects = new Rect[mCellSize][mCellSize];
        mCellList = new ArrayList<>();
        mCellsArea = new Rect();

        clearErrorStatusRunnable = new Runnable() {
            @Override
            public void run() {
                reset();
            }
        };

        mExploreByTouchHelper = new PatternExploreByTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mExploreByTouchHelper);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 宽高算上padding后，会有3种情况： （减去padding）
         * 1、绘制区域被拉伸
         * 2、绘制区域保证不被拉伸： 1、宽或高过多的一方居中（当前是这种）   2、宽或高过多的一方左右padding需要加上多出的长度
         */
        final int width = w - getPaddingLeft() - getPaddingRight();
        final int height = h - getPaddingTop() - getPaddingBottom();

        int mCellRectSize = Math.min(width, height);

        int left, top, right, bottom;
        if (width > height) { //如果宽大于高  以高为基准  水平居中  保证绘制出来的整片区域是正方形
            left = (w - mCellRectSize) / 2;
            top = getPaddingTop();
            right = left + mCellRectSize;
            bottom = top + mCellRectSize;
        } else {//如果宽小于高  以宽为基准  垂直居中
            left = getPaddingLeft();
            top = (h - mCellRectSize) / 2;
            right = left + mCellRectSize;
            bottom = top + mCellRectSize;
        }

        mCellsArea.set(left, top, right, bottom);
        //生成每个cell对应的区域  实际绘制的区域（大小）
        cellWidth = (right - left) / mCellSize;
        if (mCellPadding < 0)
            mCellPadding = cellWidth / 6; //设置点相对自身宽高的内间距
        int tempTop = top, tempLeft = left;
        for (int i = 0; i < mCellRects.length; i++) {
            for (int j = 0; j < mCellRects[i].length; j++) {
                mCellRects[i][j] = new Rect(tempLeft + mCellPadding, tempTop + mCellPadding
                        , tempLeft + cellWidth - mCellPadding, tempTop + cellWidth - mCellPadding);
                tempLeft += cellWidth;
            }
            tempLeft = left;
            tempTop += cellWidth;
        }

        if (!mDrawDefault) {
            int dLeft = left + mCellPadding;
            int dTop = left + mCellPadding;
            mNormalDrawable.setBounds(dLeft, dTop, cellWidth, cellWidth);
            mSelectedDrawable.setBounds(dLeft, dTop, cellWidth, cellWidth);
            mErrorDrawable.setBounds(dLeft, dTop, cellWidth, cellWidth);
        }

        mExploreByTouchHelper.invalidateRoot();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineBelowCell) {
            drawLine(canvas);
            drawCell(canvas);
        } else {
            drawCell(canvas);
            drawLine(canvas);
        }
    }

    private void drawCell(Canvas canvas) {
        for (int i = 0; i < mPatternDrawLookup.length; i++) {
            for (int j = 0; j < mPatternDrawLookup[i].length; j++) {
                Rect cellRect = mCellRects[i][j];
                boolean partOfPattern = mPatternDrawLookup[i][j];
                if (mDrawDefault) {
                    drawDefaultCell(canvas, partOfPattern, cellRect);
                } else {
                    drawDrawableCell(canvas, partOfPattern, cellRect);
                }
            }
        }
    }

    private void drawDefaultCell(Canvas canvas, boolean partOfPattern, Rect cellRect) {
        float strokeWidth = 4f;
        if (!partOfPattern || mInStealthMode) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            int currentColor = getCurrentColor(false);
            mPaint.setColor(currentColor);
            canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), (cellRect.width() - strokeWidth) / 2, mPaint);
        } else {//绘制选中情况
            int currentColor = getCurrentColor(true);
            mPaint.setColor(currentColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), (cellRect.width() - strokeWidth) / 2, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), 1.0f * cellRect.width() / 5.5f, mPaint);
        }
    }

    private void drawDrawableCell(Canvas canvas, boolean partOfPattern, Rect cellRect) {
        if (!partOfPattern || mInStealthMode) {
            mNormalDrawable.setBounds(cellRect);
            mNormalDrawable.draw(canvas);
        } else {
            if (mIsErrorStatus) {
                mErrorDrawable.setBounds(cellRect);
                mErrorDrawable.draw(canvas);
            } else {
                mSelectedDrawable.setBounds(cellRect);
                mSelectedDrawable.draw(canvas);
            }
        }
    }

    private void drawLine(Canvas canvas) {
        if (mInStealthMode || mCellList.isEmpty())
            return;
        mLinePaint.setColor(getCurrentColor(true));
        for (int i = 0; i < mCellList.size(); i++) {
            Cell cell = mCellList.get(i);
            Rect cellRect = mCellRects[cell.getRowIndex()][cell.getColIndex()];
            if (i == mCellList.size() - 1) {
                canvas.drawLine(cellRect.centerX(), cellRect.centerY(), mInProgressX, mInProgressY, mLinePaint);
            } else {
                Cell nextCell = mCellList.get(i + 1);
                Rect nextCellRect = mCellRects[nextCell.getRowIndex()][nextCell.getColIndex()];
                canvas.drawLine(cellRect.centerX(), cellRect.centerY(), nextCellRect.centerX(), nextCellRect.centerY(), mLinePaint);
            }
        }
    }

    private int getCurrentColor(boolean partOfPattern) {
        if (mIsErrorStatus && !mInStealthMode) {
            return mErrorColor;
        } else if (partOfPattern) {
            return mCellColor;
        } else {
            return mNormalColor;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                removeErrorStatusRunnable();
                reset();
                Cell cell = isContainCell(x, y);
                if (cell != null) {
                    setPatternInProgress(true);
                    notifyPatternStarted();
                }
                mInProgressX = x;
                mInProgressY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize + 1; i++) {
                    x = (int) (i < historySize ? event.getHistoricalX(i) : event.getX());
                    y = (int) (i < historySize ? event.getHistoricalY(i) : event.getY());
                    Cell cellByMove = isContainCell(x, y);
                    if (cellByMove != null && mCellList.size() == 1) {
                        setPatternInProgress(true);
                        notifyPatternStarted();
                    }
                }
                mInProgressX = (int) event.getX();
                mInProgressY = (int) event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (!mCellList.isEmpty()) {
                    Cell lastCell = mCellList.get(mCellList.size() - 1);
                    Rect rect = mCellRects[lastCell.getRowIndex()][lastCell.getColIndex()];
                    mInProgressX = rect.centerX();
                    mInProgressY = rect.centerY();
                    setPatternInProgress(false);
                    invalidate();
                    notifyPatternDetected();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (mPatternInProgress) {
                    setPatternInProgress(false);
                    reset();
                    notifyPatternCleared();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void notifyPatternCleared() {
        sendAccessEvent(R.string.pattern_cleared);
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternCleared();
        }
    }

    private void notifyPatternDetected() {
        sendAccessEvent(R.string.pattern_detected);
        if (mOnPatternListener != null) {
            StringBuilder sb = new StringBuilder();
            for (Cell cell : mCellList) {
                sb.append(cell.rowIndex * 3 + cell.getColIndex() + 1);
            }
            mOnPatternListener.onPatternDetected(sb.toString());
        }
    }

    private void notifyPatternStarted() {
        sendAccessEvent(R.string.pattern_start);
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternStart();
        }
    }

    private void setPatternInProgress(boolean progress) {
        mPatternInProgress = progress;
        mExploreByTouchHelper.invalidateRoot();
        if (!progress){ //清空焦点
            mExploreByTouchHelper.sendEventForVirtualView(mExploreByTouchHelper.getAccessibilityFocusedVirtualViewId()
                    , AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        }
    }

    private Cell isContainCell(int x, int y) {
        int rowIndex = getRowHit(y);
        int colIndex = getColumnHit(x);
        if (rowIndex < 0 || colIndex < 0)
            return null;
        else {
            return checkAndAddCell(rowIndex, colIndex);
        }
    }

    private Cell checkAndAddCell(int rowIndex, int colIndex) {
        if (!mCellList.isEmpty()) {
            if (mCanRepeat) {
                Cell cell = mCellList.get(mCellList.size() - 1);
                if (cell.getRowIndex() == rowIndex && cell.getColIndex() == colIndex)
                    return null;
            } else {
                for (int i = 0; i < mCellList.size(); i++) {
                    Cell cell = mCellList.get(i);
                    if (cell.getColIndex() == colIndex && cell.getRowIndex() == rowIndex) {
                        return null;
                    }
                }
            }
        }
        Cell cell = new Cell(rowIndex, colIndex);
        mCellList.add(cell);
        mPatternDrawLookup[rowIndex][colIndex] = true;
        mExploreByTouchHelper.invalidateRoot();
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                        | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        return cell;
    }

    private int getRowHit(int y) {
        int distTop = y - mCellsArea.top;
        if (distTop - mCellPadding < 0)
            return -1;
        int rowIndex = distTop / cellWidth;
        if (rowIndex > mCellSize || (rowIndex == mCellSize && (distTop % cellWidth) > 0)) {
            return -1;
        }
        if (distTop > (rowIndex * cellWidth + mCellPadding) && distTop < ((rowIndex + 1) * cellWidth - mCellPadding)) {
            return rowIndex;
        }
        return -1;
    }

    private int getColumnHit(int x) {
        int distLeft = x - mCellsArea.left;
        if (distLeft - mCellPadding < 0)
            return -1;
        int columnIndex = distLeft / cellWidth;
        if (columnIndex > mCellSize || (columnIndex == mCellSize && (distLeft % cellWidth) > 0)) {
            return -1;
        }
        if (distLeft > (columnIndex * cellWidth + mCellPadding) && distLeft < ((columnIndex + 1) * cellWidth - mCellPadding)) {
            return columnIndex;
        }
        return -1;
    }

    public void reset() {
        mIsErrorStatus = false;
        mCellList.clear();
        for (int i = 0; i < mPatternDrawLookup.length; i++) {
            for (int j = 0; j < mPatternDrawLookup[i].length; j++) {
                mPatternDrawLookup[i][j] = false;
            }
        }
        invalidate();
    }

    public void error() {
        removeErrorStatusRunnable();
        mIsErrorStatus = true;
        mHandler.postDelayed(clearErrorStatusRunnable, (long) (mErrorDelayedTime * 1000));
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeErrorStatusRunnable();
    }

    private void removeErrorStatusRunnable() {
        mHandler.removeCallbacks(clearErrorStatusRunnable);
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_HOVER_ENTER:
                event.setAction(MotionEvent.ACTION_DOWN);
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                event.setAction(MotionEvent.ACTION_MOVE);
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                event.setAction(MotionEvent.ACTION_UP);
                break;
            case MotionEvent.ACTION_CANCEL:
                event.setAction(MotionEvent.ACTION_CANCEL);
                break;
        }
        onTouchEvent(event);
        event.setAction(action);
        return super.onHoverEvent(event);
    }

    @Override
    public boolean dispatchHoverEvent(MotionEvent event) {
        boolean handled = super.dispatchHoverEvent(event);
        handled |= mExploreByTouchHelper.dispatchHoverEvent(event);
        return handled;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendAccessEvent(int resId) {
        announceForAccessibility(getContext().getString(resId));
    }

    public void setOnPatternListener(OnPatternListener mOnPatternListener) {
        this.mOnPatternListener = mOnPatternListener;
    }

    private final class PatternExploreByTouchHelper extends ExploreByTouchHelper {
        private Rect mTempRect = new Rect();
        private final SparseArray<VirtualViewContainer> mItems = new SparseArray<>();

        class VirtualViewContainer {
            public VirtualViewContainer(CharSequence description) {
                this.description = description;
            }

            CharSequence description;
        }

        public PatternExploreByTouchHelper(View forView) {
            super(forView);
            for (int i = VIRTUAL_BASE_VIEW_ID; i < VIRTUAL_BASE_VIEW_ID + mCellCount; i++) {
                mItems.put(i, new VirtualViewContainer(getTextForVirtualView(i)));
            }
        }

        private CharSequence getTextForVirtualView(int virtualViewId) {
            final Resources res = getResources();
            return res.getString(R.string.pattern_cell_added_verbose,
                    virtualViewId);
        }


        @Override
        protected int getVirtualViewAt(float x, float y) {
            // This must use the same hit logic for the screen to ensure consistency whether
            // accessibility is on or off.
            int id = getVirtualViewIdForHit(x, y);
            return id;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            if (!mPatternInProgress) {
                return;
            }
            for (int i = VIRTUAL_BASE_VIEW_ID; i < VIRTUAL_BASE_VIEW_ID + mCellCount; i++) {
                // Add all views. As views are added to the pattern, we remove them
                // from notification by making them non-clickable below.
                virtualViewIds.add(i);
            }
        }

        @Override
        protected void onPopulateEventForVirtualView(int virtualViewId, @NonNull AccessibilityEvent event) {
            // Announce this view
            VirtualViewContainer container = mItems.get(virtualViewId);
            if (container != null) {
                event.getText().add(container.description);
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {

            // Node and event text and content descriptions are usually
            // identical, so we'll use the exact same string as before.
            node.setText(getTextForVirtualView(virtualViewId));
            node.setContentDescription(getTextForVirtualView(virtualViewId));

            if (mPatternInProgress) {
                node.setFocusable(true);
                if (isClickable(virtualViewId)) {
                    // Mark this node of interest by making it clickable.
                    node.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                    node.setClickable(isClickable(virtualViewId));
                }
            }
            // Compute bounds for this object
            final Rect bounds = getBoundsForVirtualView(virtualViewId);
            node.setBoundsInParent(bounds);
        }

        @Override
        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            if (!mPatternInProgress) {
                CharSequence contentDescription = getContext().getText(R.string.pattern_area);
                event.setContentDescription(contentDescription);
            }
        }

        private boolean isClickable(int virtualViewId) {
            // Dots are clickable if they're not part of the current pattern.
            if (virtualViewId != ExploreByTouchHelper.INVALID_ID) {
                int row = (virtualViewId - VIRTUAL_BASE_VIEW_ID) / mCellSize;
                int col = (virtualViewId - VIRTUAL_BASE_VIEW_ID) % mCellSize;
                return !mPatternDrawLookup[row][col];
            }
            return false;
        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action,
                                                        Bundle arguments) {
            switch (action) {
                case AccessibilityNodeInfo.ACTION_CLICK:
                    // Click handling should be consistent with
                    // onTouchEvent(). This ensures that the view works the
                    // same whether accessibility is turned on or off.
                    return onItemClicked(virtualViewId);
                default:
            }
            return false;
        }

        boolean onItemClicked(int index) {

            // Since the item's checked state is exposed to accessibility
            // services through its AccessibilityNodeInfo, we need to invalidate
            // the item's virtual view. At some point in the future, the
            // framework will obtain an updated version of the virtual view.
            invalidateVirtualView(index);

            // We need to let the framework know what type of event
            // happened. Accessibility services may use this event to provide
            // appropriate feedback to the user.
            sendEventForVirtualView(index, AccessibilityEvent.TYPE_VIEW_CLICKED);

            return true;
        }

        private Rect getBoundsForVirtualView(int virtualViewId) {
            int ordinal = virtualViewId - VIRTUAL_BASE_VIEW_ID;
            final Rect bounds = mTempRect;
            final int row = ordinal / mCellSize;
            final int col = ordinal % mCellSize;
            Rect rect = mCellRects[row][col];
            bounds.set(rect);
            return bounds;
        }

        private int getVirtualViewIdForHit(float x, float y) {
            final int rowHit = getRowHit((int) y);
            if (rowHit < 0) {
                return ExploreByTouchHelper.INVALID_ID;
            }
            final int columnHit = getColumnHit((int) x);
            if (columnHit < 0) {
                return ExploreByTouchHelper.INVALID_ID;
            }
            boolean dotAvailable = mPatternDrawLookup[rowHit][columnHit];
            int dotId = (rowHit * 3 + columnHit) + VIRTUAL_BASE_VIEW_ID;
            return dotAvailable ? dotId : ExploreByTouchHelper.INVALID_ID;
        }
    }

    private static class Cell {
        private int rowIndex;
        private int colIndex;

        public Cell(int rowIndex, int colIndex) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColIndex() {
            return colIndex;
        }
    }

    public interface OnPatternListener {
        void onPatternStart();

        void onPatternDetected(String data);

        void onPatternCleared();
    }
}
