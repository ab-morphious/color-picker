package com.example.colorpalette

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class ThreeWaySegmentItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentPoint = PointF()
    var color = 0
    var selectorPaint : Paint

    init {
        selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        selectorPaint.color = color
        selectorPaint.style = Paint.Style.FILL
        selectorPaint.strokeWidth = 2f

    }

    override fun onDraw(canvas: Canvas) {
        canvas!!.drawCircle(width * 1f / 2, height * 1f / 2, 20f, selectorPaint)
    }


    fun setFillColor(color : Int)
    {
        this.color  = color
        selectorPaint.setColor(color)
        invalidate()
    }
}