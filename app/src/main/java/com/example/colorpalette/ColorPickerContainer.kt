package top.defaults.colorpicker

import ColorObservable
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.core.view.*
import com.example.colorpalette.*
import com.example.colorpalette.helpers.ThrottledTouchEventHandler

class ColorPickerContainer @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context!!, attrs, defStyleAttr), ColorObservable, Updatable {
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var selectorRadiusPx: Float = 27F
    private val currentPoint = PointF()
    private var currentColor = Color.RED
    private var onlyUpdateOnTouchEventUp = false
    private var selector: ColorWheelSelector? = null
    private val emitter: ColorObservableEmitter = ColorObservableEmitter()
    private val handler: ThrottledTouchEventHandler = ThrottledTouchEventHandler(this)

    private var colorSelect1: ThreeWaySegmentItem
    private var colorSelect2: ThreeWaySegmentItem
    private var colorSelect3: ThreeWaySegmentItem
    private var selectedColorSegment = -1
    private val TEAL = parseColor("#00c2a3")
    private val GREEN = parseColor("#4ba54f")
    private val ORANGE = parseColor("#ff6100")
    private var segmentColors : MutableList<Int> = mutableListOf<Int>(TEAL, GREEN,
        ORANGE)
    private val DEFAULT_COLOR = "#2c2c2c"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int
        height = Math.min(maxWidth, maxHeight)
        width = height
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val netWidth = w - paddingLeft - paddingRight - marginLeft - marginRight
        val netHeight = h - paddingTop - paddingBottom - marginTop - marginBottom
        radius = (Math.min(netWidth, netHeight) * 0.5f - selectorRadiusPx) - 45
        if (radius < 0) return
        centerX = (netWidth) * 0.5f
        centerY = (netHeight) * 0.5f
        setColor(currentColor, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                handler.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                update(event)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun update(event: MotionEvent?) {
        val x = event!!.x
        val y = event.y
        val isTouchUpEvent = event.actionMasked == MotionEvent.ACTION_UP
        if (!onlyUpdateOnTouchEventUp || isTouchUpEvent) {
            emitter.onColor(getColorAtPoint(x, y), true, isTouchUpEvent)
           if(selectedColorSegment != -1) handleColorSegments(x,y)
            invalidate()
        }
        updateSelector(x, y)
    }

    private fun handleColorSegments( x: Float,  y: Float)
    {
        var sColor : Int = getColorAtPoint(x, y);

        when(selectedColorSegment)
        {
            0 -> {
                colorSelect1.setFillColor(sColor)
            }
            1 -> {
                colorSelect2.setFillColor(sColor)
            }
            2 -> {
                colorSelect3.setFillColor(sColor)
            }
            else -> {
                colorSelect1.setFillColor(parseColor(DEFAULT_COLOR))
                colorSelect2.setFillColor(parseColor(DEFAULT_COLOR))
                colorSelect3.setFillColor(parseColor(DEFAULT_COLOR))
            }
        }
        segmentColors[selectedColorSegment] = sColor
    }
    private fun getColorAtPoint(eventX: Float, eventY: Float): Int {
        val x = eventX - centerX
        val y = eventY - centerY
        val r = Math.sqrt((x * x + y * y).toDouble())
        val hsv = floatArrayOf(0f, 0f, 1f)
        hsv[0] = (Math.atan2(y.toDouble(), -x.toDouble()) / Math.PI * 180f).toFloat() + 180
        hsv[1] = Math.max(0f, Math.min(1f, (r / radius).toFloat()))
        return Color.HSVToColor(hsv)
    }

    fun setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp: Boolean) {
        this.onlyUpdateOnTouchEventUp = onlyUpdateOnTouchEventUp
    }

    fun setColor(color: Int, shouldPropagate: Boolean) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val r = hsv[1] * radius
        val radian = (hsv[0] / 180f * Math.PI).toFloat()
        updateSelector(
            (r * Math.cos(radian.toDouble()) + centerX).toFloat(),
            (-r * Math.sin(radian.toDouble()) + centerY).toFloat()
        )
        selector?.setSelectorColor(color)
        currentColor = color
        invalidate()

        if (!onlyUpdateOnTouchEventUp) {
            emitter.onColor(color, false, shouldPropagate)
        }
    }

    private fun updateSelector(eventX: Float, eventY: Float) {
        var x = eventX - centerX
        var y = eventY - centerY
        val r = Math.sqrt((x * x + y * y).toDouble())
        if (r > radius) {
            x *= (radius / r).toFloat()
            y *= (radius / r).toFloat()
        }
        currentPoint.x = x + centerX
        currentPoint.y = y + centerY
        selector?.setCurrentPoint(currentPoint)
        selector?.setSelectorColor(getColor())
    }

    override fun subscribe(observer: ColorChange?) {
        emitter.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorChange?) {
        emitter.unsubscribe(observer)
    }

    override fun getColor(): Int {
        return emitter.getColor()
    }

    fun parseColor(color : String) : Int{
        return Color.parseColor(color);
    }
    init {

        selectorRadiusPx = 27f * resources.displayMetrics.density
        val padding = 80

        val layoutParams0 = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams0.setMargins(
            50
        )
        val palette0 = CircleColorPalette(context!!, null)
        addView(palette0, layoutParams0)

        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        selector = ColorWheelSelector(context)
        selector!!.setSelectorRadiusPx(selectorRadiusPx)

        addView(selector, layoutParams)

        val frameLayoutParams = LayoutParams(
            300,
            60
        )

        var frameLayout = FrameLayout(context)
        frameLayout.setBackgroundColor(Color.parseColor("#2c2c2c"))

        val layoutParamColorSelector = LayoutParams(
            50,
            50
        )
        layoutParamColorSelector.setMargins(50, 0, 0, 0)
        colorSelect1 = ThreeWaySegmentItem(context)
        colorSelect1.setFillColor(Color.parseColor("#00c2a3"))
        frameLayout.addView(colorSelect1, layoutParamColorSelector)

        val layoutParamColorSelector2 = LayoutParams(
            50,
            50
        )
        layoutParamColorSelector2.setMargins(50 + 50 + 25, 0, 0, 0)

        colorSelect2 = ThreeWaySegmentItem(context)
        colorSelect2.setFillColor(Color.parseColor("#4ba54f"))


        frameLayout.addView(colorSelect2, layoutParamColorSelector2)

        val layoutParamColorSelector3 = LayoutParams(
            50,
            50
        )
        layoutParamColorSelector3.setMargins(50 + 50 + 25 + 50 + 25, 0, 0, 0)

        colorSelect3 = ThreeWaySegmentItem(context)
        colorSelect3.setFillColor(Color.parseColor("#ff6100"))

        frameLayout.addView(colorSelect3, layoutParamColorSelector3)

        addView(frameLayout, frameLayoutParams)

        colorSelect1.setOnClickListener(OnClickListener {
            selectedColorSegment = 0;
            setColor(segmentColors[selectedColorSegment], false)
            invalidate()
        })


        colorSelect2.setOnClickListener(OnClickListener {
            selectedColorSegment = 1;
            setColor(segmentColors[selectedColorSegment], false)
            invalidate()
        })

        colorSelect3.setOnClickListener(OnClickListener {
            selectedColorSegment = 2;
            setColor(segmentColors[selectedColorSegment], false)
            invalidate()
        })
    }
}