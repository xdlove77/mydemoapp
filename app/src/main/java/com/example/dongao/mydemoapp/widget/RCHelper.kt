package com.example.myproject.widget

import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.dongao.mydemoapp.R

class RCHelper(view: View, attributeSet: AttributeSet) {

    private var ltRadius = 0f
    private var rtRadius = 0f
    private var lbRadius = 0f
    private var rbRadius = 0f
    private var radius = 0f
    private var height = 0f
    private var width = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }
    private val path = Path()
    private val rectF = RectF()

    init {
        val typedArray = view.context.obtainStyledAttributes(attributeSet, R.styleable.RCConfig)
        ltRadius = typedArray.getDimension(R.styleable.RCConfig_ltRadius, 0f)
        rtRadius = typedArray.getDimension(R.styleable.RCConfig_rtRadius, 0f)
        lbRadius = typedArray.getDimension(R.styleable.RCConfig_lbRadius, 0f)
        rbRadius = typedArray.getDimension(R.styleable.RCConfig_rbRadius, 0f)
        radius = typedArray.getDimension(R.styleable.RCConfig_layoutRadius, 0f)
        typedArray.recycle()
    }

    fun clipRoundCorner(canvas: Canvas?) {
        height = canvas?.height?.toFloat() ?: 0f
        width = canvas?.width?.toFloat() ?: 0f
        drawLTCorner(canvas)
        drawRTCorner(canvas)
        drawLBCorner(canvas)
        drawRBCorner(canvas)
    }

    private fun drawRBCorner(canvas: Canvas?) {
        val r = if (rbRadius > 0) rbRadius else {
            if (radius > 0) radius else return
        }
        rectF.set(width - 2 * r, height - 2 * r, width, height)
        path.reset()
        path.moveTo(width, height)
        path.lineTo(width, height - r)
        path.arcTo(rectF, 0f, 90f)
        path.close()
        canvas?.drawPath(path, paint)
    }

    private fun drawLBCorner(canvas: Canvas?) {
        val r = if (lbRadius > 0) lbRadius else {
            if (radius > 0) radius else return
        }
        rectF.set(0f, height - 2 * r, 2 * r, height)
        path.reset()
        path.moveTo(0f, height)
        path.lineTo(r, height)
        path.arcTo(rectF, 90f, 90f)
        path.close()
        canvas?.drawPath(path, paint)
    }

    private fun drawRTCorner(canvas: Canvas?) {
        val r = if (rtRadius > 0) rtRadius else {
            if (radius > 0) radius else return
        }
        rectF.set(width - 2 * r, 0f, width, 2 * r)
        path.reset()
        path.moveTo(width, 0f)
        path.lineTo(width, r)
        path.arcTo(rectF, 0f, -90f)
        path.close()
        canvas?.drawPath(path, paint)
    }

    private fun drawLTCorner(canvas: Canvas?) {
        val r = if (ltRadius > 0) ltRadius else {
            if (radius > 0) radius else return
        }
        rectF.set(0f, 0f, r * 2, r * 2)
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, r)
        path.arcTo(rectF, 180f, 90f)
        path.close()
        canvas?.drawPath(path, paint)
    }
}