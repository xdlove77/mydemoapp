package com.example.myproject.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class RCFrameLayout : FrameLayout{

    private val rcHelper: RCHelper

    constructor(context: Context,attributeSet: AttributeSet): super(context,attributeSet){
        rcHelper = RCHelper(this,attributeSet)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        rcHelper.clipRoundCorner(canvas)
        invalidate(Rect())
    }
}