package com.example.colorpalette.util

import android.graphics.Color.parseColor

class Constants{
    companion object{
        const val EVENT_MIN_INTERVAL = 1000 / 60 // 16ms
        const val SELECTOR_RADIUS_DP = 9
        val TEAL  = parseColor("#00c2a3")
        val GREEN= parseColor("#4ba54f")
        val ORANGE  = parseColor("#ff6100")
        val BG_COLOR = parseColor("#2c2c2c")
    }
}