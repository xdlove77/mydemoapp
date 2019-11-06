package com.example.dongao.mydemoapp.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

/**
 * 1、遍历找到布局文件中的tag，对标记tag的view进行替换（没问题 已解决）
 * 2、错误页面如何绑定到manager中？ （通过tag去绑定到manager中， 使用者使用tag去区分页面？）
 *      例如多种自定义的错误页面
 * 3、事件如何回调？（显示出来之后通过回调返回view？）
 *      例如多种自定义错误页面的事件如何回调
 */
class PageStateManager private constructor() {

    private var injected = false
    private lateinit var layout: FrameLayout
    private lateinit var contentView: View
    private val pageViewMap = HashMap<String, PageInfo>()
    private lateinit var context: Context

    companion object {
        private const val REPLACE_TAG = "replaceTag"
        private const val CONTENT = "content"

        @JvmStatic
        fun of() = PageStateManager()
    }

    fun injectLayoutByTag(container: ViewGroup): PageStateManager {
        if (injected)
            throw RuntimeException("只能注入一次")
        val contentView =
                findViewByTag(container) ?: throw RuntimeException("需要在被替换的layout的属性中添加 android:tag='replaceTag'")
        replaceViewWithSelf(contentView)
        context = container.context
        this.contentView = contentView
        injected = true
        return this
    }

    fun injectLayoutByView(contentView: View): PageStateManager {
        if (injected)
            throw RuntimeException("只能注入一次")
        replaceViewWithSelf(contentView)
        context = contentView.context
        this.contentView = contentView
        injected = true
        return this
    }

    private fun findViewByTag(container: ViewGroup): View? {
        if (REPLACE_TAG == container.tag){
            container.tag = null
            return container
        }

        loop@ for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is ViewGroup) {
                findViewByTag(view)
                continue@loop
            }
            if (REPLACE_TAG == view.tag) {
                view.tag = null
                return view
            }
        }
        return null
    }

    private fun replaceViewWithSelf(view: View) {
        layout = FrameLayout(view.context)

        val parent = view.parent as ViewGroup
        val index = parent.indexOfChild(view)
        val layoutParams = view.layoutParams
        layout.id = view.id
        view.id = View.NO_ID
        parent.removeViewInLayout(view)
        parent.addView(layout, index, layoutParams)
        layout.addView(
                view,
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
    }


    fun addPage(tag: String, layoutId: Int, visibilityListener: PageVisibilityListener? = null) = this.apply {
        checkInjected()
        if (tag == CONTENT)
            throw IllegalArgumentException("tag参数不能使用 $tag 作为标识")
        pageViewMap[tag]?.apply {
            layout.removeView(page)
        }
        val viewStub = ViewStub(context, layoutId)
        layout.addView(
                viewStub,
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        pageViewMap[tag] = PageInfo(viewStub, visibilityListener)
    }

    fun showPage(tag: String) {
        checkInjected()
        innerShowPage(tag)
    }

    fun showContentPage(){
        checkInjected()
        innerShowPage(CONTENT)
    }

    private fun innerShowPage(tag: String) {
        if (tag != CONTENT && !pageViewMap.containsKey(tag)){
            throw RuntimeException("需要先添加tag为${tag}的状态页")
        }
        pageViewMap.forEach {
            setPageVisibility(it.value, if (tag == it.key) View.VISIBLE else View.GONE )
        }
        if (tag == CONTENT){
            contentView.visibility = View.VISIBLE
        } else {
            contentView.visibility = View.GONE
        }
    }

    private fun setPageVisibility(pageInfo: PageInfo, visibility: Int) {
        val page = pageInfo.page
        if (visibility == View.VISIBLE) {
            var view: View = page
            if (view is ViewStub) {
                view = view.inflate()
                pageInfo.page = view
            }
            if (view.visibility == View.VISIBLE)
                return
            view.visibility = visibility
            pageInfo.visibilityListener?.onVisible(view)
        } else if (page !is ViewStub && page.visibility == View.VISIBLE) {
            page.visibility = visibility
            pageInfo.visibilityListener?.onInvisible(page)
        }
    }

    private fun checkInjected() {
        if (!injected)
            throw RuntimeException("需要先调用injectLayout方法")
    }

    interface PageVisibilityListener {
        fun onVisible(view: View)
        fun onInvisible(view: View)
    }

    open class SimplePageVisibilityListener : PageVisibilityListener {
        override fun onVisible(view: View) {
        }

        override fun onInvisible(view: View) {
        }
    }

    private class PageInfo(var page: View, val visibilityListener: PageVisibilityListener?)
}