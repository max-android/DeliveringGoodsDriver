

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.content.ContextCompat
import com.my_project.deliveringgoods.R


class ColumnView (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var backgroundPaint: Paint
    private lateinit var linePaint: Paint
    private lateinit var numberPaint: TextPaint
    private lateinit var backgroundRect: RectF
    private var topText: String = ""
    private var data: Int = 0
    private var baselineWidth: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColumnView, 0, 0)
        initBackgroundPaint(context, typedArray)
        initNumberPaint(context, typedArray)
        backgroundRect = RectF()
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = Math.round(paddingLeft.toFloat() + paddingRight.toFloat())
        val desiredHeight = Math.round(paddingTop.toFloat() + paddingBottom.toFloat())
        val measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height
        val paramY = canvasHeight.toFloat() - (canvasHeight * data/100).toFloat()
        drawText(canvas,canvasWidth,paramY)
        drawColumn(canvas,canvasWidth,canvasHeight,paramY)
    }

    private fun initBackgroundPaint(context: Context, typedArray: TypedArray) {
        backgroundPaint = Paint(Paint.SUBPIXEL_TEXT_FLAG)
        val backgroundColor = typedArray.getColor(
            R.styleable.ColumnView_columnBackgroundViewColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        backgroundPaint.color = backgroundColor
    }

    private fun initLinePaint(context: Context, typedArray: TypedArray) {
        linePaint = Paint(Paint.SUBPIXEL_TEXT_FLAG)

        linePaint.apply {
            style = Paint.Style.STROKE
            color = typedArray.getColor(
                R.styleable.ColumnView_columnBaselineWidth,
                ContextCompat.getColor(context, R.color.colorAccent)
            )
            baselineWidth = typedArray.getDimensionPixelSize(R.styleable.RequestView_baselineWidth, 1)
            strokeWidth = baselineWidth.toFloat()
        }

    }

    private fun initNumberPaint(context: Context, typedArray: TypedArray) {
        numberPaint = TextPaint(Paint.SUBPIXEL_TEXT_FLAG)
        numberPaint.apply {
            color = typedArray.getColor(
                R.styleable.RequestView_android_textColor,
                ContextCompat.getColor(context, android.R.color.white)
            )
            textSize = Math.round(
                typedArray.getDimensionPixelSize(
                    R.styleable.RequestView_android_textSize,
                    Math.round(64f * resources.displayMetrics.scaledDensity)
                ).toFloat()
            ).toFloat()
            typeface = Typeface.MONOSPACE
        }
    }

    private fun reconcileSize(contentSize: Int, measureSpec: Int):Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        return  when(mode){
            View.MeasureSpec.EXACTLY -> specSize
            View.MeasureSpec.AT_MOST ->  if (contentSize < specSize) contentSize else  specSize
            View.MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    private fun drawText(canvas: Canvas, canvasWidth:Int, height:Float){
        val centerX = canvasWidth.toFloat()/2
        if(data < 91){
            //проценты
            val text = "$data" + topText
            val textWidth = numberPaint.measureText(text)
            val textX = Math.round(centerX - textWidth * 0.5f).toFloat()
            val textHeight = numberPaint.textSize
            val textY = Math.round(height - textHeight/4).toFloat()
            canvas.drawText(text, textX, textY, numberPaint)
        }
    }

    private fun drawColumn(canvas: Canvas, canvasWidth:Int, canvasHeight:Int, height:Float){
        backgroundRect.set(0f, height, canvasWidth.toFloat(),canvasHeight.toFloat())
        canvas.drawRoundRect(backgroundRect, 0f, 0f, backgroundPaint)
        backgroundRect.set(0f, height, canvasWidth.toFloat(),canvasHeight.toFloat())
        val p = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
        }
        val rect = RectF()
        rect.set(0f, 0f, canvasWidth.toFloat(),canvasHeight.toFloat())
        canvas.drawRoundRect( rect, 0f, 0f, p)
    }

    fun setData(topText: String, data: Int) {
        this.data = data
        this.topText = topText
        setAnimation()
        invalidate()
    }

    private fun setAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.apply {
            duration = 2000
            interpolator = BounceInterpolator()
            addUpdateListener { valueanimator ->
                val value = animator.animatedValue as Float
                scaleX = value
                scaleY = value
            }
        }.start()
    }

}