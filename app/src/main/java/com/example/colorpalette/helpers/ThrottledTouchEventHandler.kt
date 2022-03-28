package com.example.colorpalette.helpers

import com.example.colorpalette.util.Constants
import android.view.MotionEvent
import top.defaults.colorpicker.Updatable

class ThrottledTouchEventHandler {

    private var minInterval = Constants.EVENT_MIN_INTERVAL
    private var updatable: Updatable? = null
    private var lastPassedEventTime: Long = 0

    constructor (minInterval: Int, updatable: Updatable) {
        this.minInterval = minInterval
        this.updatable = updatable
    }

    constructor (updatable: Updatable?) : this(Constants.EVENT_MIN_INTERVAL, updatable!!)

    fun onTouchEvent(event: MotionEvent?) {
        if (updatable == null) return
        val current = System.currentTimeMillis()
        if (current - lastPassedEventTime <= minInterval) {
            return
        }
        lastPassedEventTime = current
        updatable!!.update(event)
    }
}