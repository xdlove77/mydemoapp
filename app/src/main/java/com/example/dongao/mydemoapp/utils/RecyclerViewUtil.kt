package com.example.dongao.mydemoapp.utils

import android.support.v7.widget.*
import android.util.DisplayMetrics
import android.view.View


/**
 * 只适用于 LinearLayoutManager，GridLayoutManager 或 StaggeredGridLayoutManager
 */
fun RecyclerView.LayoutManager.isAtBottom(): Boolean {
    return findLastCompletelyVisiblePosition() == itemCount - 1
}

fun RecyclerView.LayoutManager.isAtTop(): Boolean {
    return tryFindFirstCompletelyVisiblePosition() == 0
}

fun RecyclerView.LayoutManager.findLastVisiblePosition(): Int {
    return when (this) {
        is LinearLayoutManager ->
            findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val poses = IntArray(spanCount)
            findLastVisibleItemPositions(poses).max() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

fun RecyclerView.LayoutManager.findLastCompletelyVisiblePosition(): Int {
    return when (this) {
        is LinearLayoutManager ->
            findLastCompletelyVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val poses = IntArray(spanCount)
            findLastCompletelyVisibleItemPositions(poses).max() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

fun RecyclerView.LayoutManager.tryFindFirstVisiblePosition(): Int {
    return when (this) {
        is LinearLayoutManager -> {
            findFirstVisibleItemPosition()
        }
        is StaggeredGridLayoutManager -> {
            val poses = IntArray(spanCount)
            findFirstVisibleItemPositions(poses).min() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

fun RecyclerView.LayoutManager.tryFindLastVisiblePosition(): Int {
    return when (this) {
        is LinearLayoutManager -> {
            findLastVisibleItemPosition()
        }
        is StaggeredGridLayoutManager -> {
            val poses = IntArray(spanCount)
            findLastVisibleItemPositions(poses).max() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

fun RecyclerView.LayoutManager.tryFindFirstCompletelyVisiblePosition(): Int {
    return when (this) {
        is LinearLayoutManager -> {
            findFirstCompletelyVisibleItemPosition()
        }
        is StaggeredGridLayoutManager -> {
            val poses = IntArray(spanCount)
            findFirstCompletelyVisibleItemPositions(poses).min() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

fun RecyclerView.smoothScrollToPositionTop(pos: Int) {
    if (pos == RecyclerView.NO_POSITION) return
    val lm = layoutManager
    if (lm is LinearLayoutManager) {
        val vPos = lm.findFirstVisibleItemPosition()
        val cPos = lm.findFirstCompletelyVisibleItemPosition()
        if (vPos == cPos && cPos == pos) return
    }
    val scroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }
    scroller.targetPosition = pos

    layoutManager.startSmoothScroll(scroller)
}

fun RecyclerView.smoothScrollToBottom() {
    if (adapter.itemCount > 1)
        smoothScrollToPositionTop(adapter.itemCount - 1)
}

fun RecyclerView.findCenterView(): View? {
    return layoutManager?.findCenterView()
}

fun RecyclerView.LayoutManager.findCenterView(): View? {

    val helper: OrientationHelper = if (canScrollVertically()) {
        OrientationHelper.createOrientationHelper(this, OrientationHelper.VERTICAL)
    } else {
        OrientationHelper.createOrientationHelper(this, OrientationHelper.HORIZONTAL)
    }

    val childCount = childCount
    if (childCount == 0) {
        return null
    }

    var closestChild: View? = null
    val center: Int
    if (clipToPadding) {
        center = helper.startAfterPadding + helper.totalSpace / 2
    } else {
        center = helper.end / 2
    }
    var absClosest = Integer.MAX_VALUE

    for (i in 0 until childCount) {
        val child = getChildAt(i)
        val childCenter = helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2
        val absDistance = Math.abs(childCenter - center)

        /** if child center is closer than previous closest, set it as closest   */
        if (absDistance < absClosest) {
            absClosest = absDistance
            closestChild = child
        }
    }
    return closestChild
}

//fun LinearLayoutManager.smoothScrollToCenter(pos: Int) {
//    val visible = (findFirstVisibleItemPosition() <= pos && findLastVisibleItemPosition() >= pos)
//    if (pos < 0) return
//    val scroller = if (visible) SlowCenterScroller() else QuickCenterScroller()
//    scroller.targetPosition = pos
//    startSmoothScroll(scroller)
//}

//open class CenterSmoothScroller : LinearSmoothScroller(MfwTinkerApplication.getInstance()) {
//    override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
//        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
//    }
//}
//
//private class SlowCenterScroller : CenterSmoothScroller() {
//    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
//        return 5 * super.calculateSpeedPerPixel(displayMetrics)
//    }
//}
//
//private class QuickCenterScroller : CenterSmoothScroller() {
//    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
//        return 2 * super.calculateSpeedPerPixel(displayMetrics)
//    }
//}

fun View.topMostRv(): RecyclerView? {
    var rv: RecyclerView? = null

    var p: View? = this
    while (p != null) {
        if (p is RecyclerView) {
            rv = p
        }
        p = p.parent as? View
    }
    return rv
}