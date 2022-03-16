package com.example.colorpalette


interface ColorChange {
    fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean)
}