import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.my_project.deliveringgoods.R
import timber.log.Timber
import java.util.*

internal class TopToastLayoutManager constructor(context: Context) : LinearLayout(context) {

    private val PROPERTY_CONTAINER_HEIGHT = UUID.randomUUID().toString()
    private val PROPERTY_TIMER_WIDTH = UUID.randomUUID().toString()
    private val RPOPERTY_ALPHA_APPEAR = UUID.randomUUID().toString()
    private val RPOPERTY_ALPHA_DISAPPEAR = UUID.randomUUID().toString()
    private val DEFAULT_RESOURCE_VALUE = 0
    private val TIMER_WIDTH = context.resources.getDimension(R.dimen.timer_width).toInt()
    private val ANIM_DURATION = 200L
    private val ALPHA_DURATION = ANIM_DURATION / 2
    private val openAnimator = ValueAnimator()
    private val closeAnimator = ValueAnimator()
    private val timerAnimator = ValueAnimator()
    private val alphaAppear = ValueAnimator()
    private val alphaDisappear = ValueAnimator()

    private lateinit var container: ViewGroup
    private lateinit var timerLayout: ViewGroup
    private lateinit var timerIndicator: ImageView
    private lateinit var textViewMessage: TextView

    private var containerHeight: Int = 0
    private var textSize: Float = 0F
    private var textColor: Int = DEFAULT_RESOURCE_VALUE

    private var timerDuration = 5000L

    internal fun show(view: View, message: String) {
        val parent = findParent(view) ?: throw RuntimeException("cannot find parent")
        if (isViewAlreadyInflated(parent)) {
            parent.removeView(parent.findViewById<ViewGroup>(R.id.container))
        }
        inflate(context, R.layout.top_toast_layout, parent)

        container = parent.findViewById(R.id.container)
        container.setOnClickListener { hide() }

        Timber.d("test %s", "show height ${container.measuredHeight}")

        val containerVTO = container.viewTreeObserver
        containerVTO.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Timber.d("test %s", "inside listener")
                containerVTO.removeOnGlobalLayoutListener(this)
                containerHeight = container.measuredHeight
                initAnimations()
                show()
            }
        })

        timerLayout = parent.findViewById(R.id.frameLayoutTimer)

        timerIndicator = parent.findViewById(R.id.imageViewTimerForeground)

        textViewMessage = parent.findViewById(R.id.textViewMessage)
        textViewMessage.text = message
        if (textSize != 0F) {
            textViewMessage.textSize = textSize
        }
        if (textColor != DEFAULT_RESOURCE_VALUE) {
            val color = ContextCompat.getColor(context, textColor)
            textViewMessage.setTextColor(color)
        }
    }

    internal fun initAnimations() {
        initOpenAnimator()
        initCloseAnimator()
        initTimerAnimation()
        initAlphaAnimations()
    }

    internal fun setTextSize(size: Float) {
        textSize = size
    }

    internal fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    internal fun hide() {
        if (container.height > 0) {
            alphaDisappear.start()
            closeAnimator.start()
        }
    }

    private fun isViewAlreadyInflated(viewGroup: ViewGroup): Boolean {
        return viewGroup.findViewById<ViewGroup>(R.id.container) != null
    }

    private fun initOpenAnimator() {
        Log.d("test", "open height $containerHeight")
        val propertyContainerHeight =
            PropertyValuesHolder.ofInt(PROPERTY_CONTAINER_HEIGHT, 0, containerHeight)
        openAnimator.setValues(propertyContainerHeight)
        openAnimator.interpolator = LinearInterpolator()
        openAnimator.duration = ANIM_DURATION
        openAnimator.addUpdateListener {
            val layoutParams = container.layoutParams
            val animatedValue = it.getAnimatedValue(PROPERTY_CONTAINER_HEIGHT) as Int
            layoutParams.height = animatedValue
            container.requestLayout()
        }
        openAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startTimer()
            }
        })
    }

    private fun initCloseAnimator() {
        Log.d("test", "close height $containerHeight")
        val propertyContainerHeight =
            PropertyValuesHolder.ofInt(PROPERTY_CONTAINER_HEIGHT, containerHeight, 0)
        closeAnimator.setValues(propertyContainerHeight)
        closeAnimator.interpolator = LinearInterpolator()
        closeAnimator.duration = ANIM_DURATION
        closeAnimator.addUpdateListener {
            val layoutParams = container.layoutParams
            val animatedValue = it.getAnimatedValue(PROPERTY_CONTAINER_HEIGHT) as Int
            layoutParams.height = animatedValue
            container.requestLayout()
        }
    }

    private fun initTimerAnimation() {
        val propertyTimerWidth = PropertyValuesHolder.ofInt(PROPERTY_TIMER_WIDTH, TIMER_WIDTH, 0)
        timerAnimator.setValues(propertyTimerWidth)
        timerAnimator.interpolator = LinearInterpolator()
        timerAnimator.duration = timerDuration
        timerAnimator.addUpdateListener {
            val animatedValue = it.getAnimatedValue(PROPERTY_TIMER_WIDTH) as Int
            val layoutParams = timerIndicator.layoutParams
            layoutParams.width = animatedValue
            timerIndicator.requestLayout()
        }
        timerAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                hide()
            }
        })
    }

    private fun initAlphaAnimations() {
        val propertyAlphaAppear = PropertyValuesHolder.ofFloat(RPOPERTY_ALPHA_APPEAR, 0f, 1f)
        alphaAppear.setValues(propertyAlphaAppear)
        alphaAppear.interpolator = LinearInterpolator()
        alphaAppear.duration = ALPHA_DURATION
        alphaAppear.addUpdateListener {
            container.alpha = it.getAnimatedValue(RPOPERTY_ALPHA_APPEAR) as Float
        }

        val propertyAlphaDisappear = PropertyValuesHolder.ofFloat(RPOPERTY_ALPHA_DISAPPEAR, 1f, 0f)
        alphaDisappear.setValues(propertyAlphaDisappear)
        alphaDisappear.interpolator = LinearInterpolator()
        alphaDisappear.duration = ALPHA_DURATION
        alphaDisappear.addUpdateListener {
            container.alpha = it.getAnimatedValue(RPOPERTY_ALPHA_DISAPPEAR) as Float
        }
        alphaDisappear.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                dropView()
            }
        })
    }

    private fun show() {
        alphaAppear.start()
        openAnimator.start()
    }

    private fun startTimer() {
        timerLayout.visibility = View.VISIBLE
        timerAnimator.start()
    }

    private fun dropView() {
        findParent(this)?.removeView(this)
    }

    private fun findParent(view: View): ViewGroup? {
        var fallback: ViewGroup? = null
        var tempView: View? = view

        do {
            if (tempView is CoordinatorLayout) return tempView

            if (tempView is FrameLayout) {
                if (tempView.id == R.id.content) return tempView
                fallback = tempView
            }

            if (tempView != null) {
                val parent = tempView.parent
                tempView = if (parent is View) parent else null
            }
        } while (tempView != null)

        return fallback
    }

}