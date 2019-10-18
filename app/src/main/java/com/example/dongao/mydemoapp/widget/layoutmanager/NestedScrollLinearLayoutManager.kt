package com.example.dongao.mydemoapp.widget.layoutmanager

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.dongao.mydemoapp.utils.isAtBottom
import com.example.dongao.mydemoapp.utils.isAtTop

class NestedScrollLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var screenH = 0

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return screenH
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (dy > 0) {
            /**
             * 内容往上滑动，父 RV 优先滑动；父 RV 到底，子 RV 继续滑动。
             * 子 RV 若到底，则不滑动，保证父 RV 的越界动画。
             */
            var scrolledDy = super.scrollVerticallyBy(dy, recycler, state)
            if (dy != scrolledDy) {
                for (i in 0 until childCount) {
                    val nest = getChildAt(i).findVerticalScrollRecyclerView()
                    nest?.apply {
                        if (!layoutManager.isAtBottom()) {
                            scrollBy(0, dy - scrolledDy)
                            scrolledDy = dy
                        }
                    }
                }
            }
            return scrolledDy
        } else {
            /**
             * 内容往下滑动，子 RV 优先滑动；子 RV 到顶，父 RV 继续滑动。
             */
            for (i in 0 until childCount) {
                val nest = getChildAt(i).findVerticalScrollRecyclerView()
                nest?.apply {
                    if (!layoutManager.isAtTop()) {
                        scrollBy(0, dy)
                        return dy
                    }
                }
            }
            return super.scrollVerticallyBy(dy, recycler, state)
        }
    }
}

private fun View.findVerticalScrollRecyclerView(): RecyclerView? {
    if (this !is ViewGroup) {
        if (this is RecyclerView) {
            return this
        } else {
            return null
        }
    } else {
        val nest = children.find { it.findVerticalScrollRecyclerView() != null }?.findVerticalScrollRecyclerView()
        if (nest != null) {
            return nest
        } else {
            if (isTargetNestScrollView()) {
                return this as RecyclerView
            } else {
                return null
            }
        }
    }
}

private fun View.isTargetNestScrollView(): Boolean {
    return (this is RecyclerView && this.canScrollVertically(1) or this.canScrollVertically(-1))
}

private val ViewGroup.children: List<View>
    get() {
        val views = mutableListOf<View>()

//        if (this is ViewPager) {
//            ViewPagerUtils.getCurrentView(this)?.apply {
//                views.add(this)
//            }
//            return views
//        }

        for (i in 0 until childCount) {
            views.add(getChildAt(i))
        }
        return views
    }