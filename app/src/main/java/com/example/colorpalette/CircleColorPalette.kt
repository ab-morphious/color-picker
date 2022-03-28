package com.example.colorpalette

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

class CircleColorPalette : View {

    private var huePaint: Paint? = null
    private var saturationPaint: Paint? = null
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        huePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        saturationPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        var widthT = w - paddingLeft - paddingRight
        var heightT = h - paddingTop - paddingBottom

        radius = Math.min(widthT, heightT) * 0.5f
        centerX = w * 0.5f
        centerY = h * 0.5f

        val hueShaderS: Shader = SweepGradient(
            centerX,
            centerY,
            intArrayOf(
                Color.RED,
                Color.MAGENTA,
                Color.BLUE,
                Color.CYAN,
                Color.GREEN,
                Color.YELLOW,
                Color.RED
            ),
            null
        )

        huePaint!!.shader = hueShaderS

        val saturationShader: Shader = RadialGradient(
            centerX, centerY, radius,
            Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP
        )
        saturationPaint!!.shader = saturationShader
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!.drawCircle(centerX, centerY, radius, huePaint!!)
        canvas.drawCircle(centerX, centerY, radius, saturationPaint!!)
    }
}