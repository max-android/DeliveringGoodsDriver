import android.view.View
import androidx.core.content.ContextCompat


class TopToast constructor(
    private val view: View,
    private val message: String
) {
    private var topToastLayoutManager = TopToastLayoutManager(view.context)

    fun setTextSize(size: Float) = topToastLayoutManager.setTextSize(size)

    fun setTextColor(colorId: Int) = topToastLayoutManager.setTextColor(colorId)

    fun setBackgroundColor(colorId: Int) {
        val color = ContextCompat.getColor(view.context, colorId)
        topToastLayoutManager.setBackgroundColor(color)
    }

    fun show() = topToastLayoutManager.show(view, message)

    fun hide() = topToastLayoutManager.hide()
}
