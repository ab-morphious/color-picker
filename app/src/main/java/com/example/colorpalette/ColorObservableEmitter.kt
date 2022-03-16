package top.defaults.colorpicker

import ColorObservable
import com.example.colorpalette.ColorChange

internal class ColorObservableEmitter : ColorObservable {
    private val observers: MutableList<ColorChange> = ArrayList<ColorChange>()
    private var color = 0
    override fun subscribe(observer: ColorChange?) {
        if (observer == null) return
        observers.add(observer)
    }

    override fun unsubscribe(observer: ColorChange?) {
        if (observer == null) return
        observers.remove(observer)
    }

    override fun getColor(): Int {
        return this.color
    }

    fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean) {
        this.color = color
        for (observer in observers) {
            observer.onColor(color, fromUser, shouldPropagate)
        }
    }
}