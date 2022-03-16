package com.example.colorpalette

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable


class ColorWheelSelector @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val selectorPaint: Paint
    private val selectorBorder: Paint
    private var selectorRadiusPx: Float = 27f
    private var currentPoint = PointF()
    private var color : Int = 0

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.7f, selectorPaint)
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.65f, selectorBorder)
    }

    fun setSelectorRadiusPx(selectorRadiusPx: Float) {
        this.selectorRadiusPx = selectorRadiusPx
    }

    fun setCurrentPoint(currentPoint: PointF) {
        this.currentPoint = currentPoint
        invalidate()
    }

    fun setSelectorColor(color: Int)
    {
        selectorPaint.color = color
        invalidate()
    }

    init {
        selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        selectorPaint.setShadowLayer(25f, 0f, 0f, Color.DKGRAY)
        selectorPaint.color = color
        selectorPaint.style = Paint.Style.FILL
        selectorPaint.strokeWidth = 2f

        selectorBorder = Paint(Paint.ANTI_ALIAS_FLAG)
        selectorBorder.color = Color.WHITE
        selectorBorder.style = Paint.Style.STROKE
        selectorBorder.strokeWidth = 5f
    }
}