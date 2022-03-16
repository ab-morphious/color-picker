import com.example.colorpalette.ColorChange

interface ColorObservable {
    fun subscribe(observer: ColorChange?)
    fun unsubscribe(observer: ColorChange?)
    fun getColor(): Int
}
