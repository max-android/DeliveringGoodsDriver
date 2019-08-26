import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.my_project.deliveringgoods.R


class CircleDiagramView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var backgroundPaint_1: Paint
    private lateinit var backgroundPaint_2: Paint
    private lateinit var backgroundPaint_3: Paint
    private lateinit var numberPaint: TextPaint
    private var data_1: Int = 0
    private var data_2: Int = 0
    private var data_3: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleDiagramView, 0, 0)
        initBackgroundPaint(context, typedArray)
        initNumberPaint(context, typedArray)
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
        drawArc(canvas)
    }

    private fun initBackgroundPaint(context: Context, typedArray: TypedArray) {
        backgroundPaint_1 = Paint(Paint.SUBPIXEL_TEXT_FLAG)
        val backgroundColor = typedArray.getColor(
            R.styleable.CircleDiagramView_circle1BackgroundColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        backgroundPaint_1.color = backgroundColor
        backgroundPaint_2 = Paint(Paint.SUBPIXEL_TEXT_FLAG)
        val backgroundColor_2 = typedArray.getColor(
            R.styleable.CircleDiagramView_circle2BackgroundColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        backgroundPaint_2.color = backgroundColor_2
        backgroundPaint_3 = Paint(Paint.SUBPIXEL_TEXT_FLAG)
        val backgroundColor_3 = typedArray.getColor(
            R.styleable.CircleDiagramView_circle3BackgroundColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        backgroundPaint_3.color = backgroundColor_3
    }

    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        return when (mode) {
            View.MeasureSpec.EXACTLY -> specSize
            View.MeasureSpec.AT_MOST -> if (contentSize < specSize) contentSize else specSize
            View.MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    private fun initNumberPaint(context: Context, typedArray: TypedArray) {
        numberPaint = TextPaint(Paint.SUBPIXEL_TEXT_FLAG)
        numberPaint.apply {
            color = typedArray.getColor(
                R.styleable.CircleDiagramView_android_textColor,
                ContextCompat.getColor(context, android.R.color.white)
            )

            textSize = Math.round(
                typedArray.getDimensionPixelSize(
                    R.styleable.CircleDiagramView_android_textSize,
                    Math.round(64f * resources.displayMetrics.scaledDensity)
                ).toFloat()
            ).toFloat()
            typeface = Typeface.MONOSPACE
        }
    }


    private fun drawArc(canvas: Canvas) {

        val oval = RectF()
        oval.set(
            0f, 0f,
            (canvas.width).toFloat(),
            (canvas.height).toFloat()
        )

        val contur = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
        }

        canvas.drawCircle(canvas.width.toFloat() / 2, canvas.height.toFloat() / 2, canvas.height.toFloat() / 2, contur)

        val sweepAngle_1 = data_1.toFloat() / 100f * 360
        val sweepAngle_2 = data_2.toFloat() / 100f * 360
        val sweepAngle_3 = data_3.toFloat() / 100f * 360

        canvas.drawArc(oval, 270f, sweepAngle_1, true, backgroundPaint_1)
        canvas.drawArc(oval, 270f + sweepAngle_1, sweepAngle_2, true, backgroundPaint_2)
        canvas.drawArc(oval, 270f + sweepAngle_1 + sweepAngle_2, sweepAngle_3, true, backgroundPaint_3)

        drawText(canvas, sweepAngle_1, sweepAngle_2, sweepAngle_3)

    }

    private fun drawText(canvas: Canvas, sweepAngle_1: Float, sweepAngle_2: Float, sweepAngle_3: Float) {
        val cX_1 =
            canvas.width.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.cos((270f + sweepAngle_1 / 2) * Math.PI / 180)
        val cY_1 =
            canvas.height.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.sin((270f + sweepAngle_1 / 2) * Math.PI / 180)

        val textX_1 = (cX_1 + canvas.width.toFloat() / 2) / 2
        val textY_1 = (cY_1 + canvas.height.toFloat() / 2) / 2

        val textWidth_1 = numberPaint.measureText("$data_1%")
        val coordX_1 = Math.round(textX_1 - textWidth_1 * 0.5f).toFloat()

        val textHeight_1 = numberPaint.textSize / 2
        val coordY_1 = Math.round(textY_1 + textHeight_1 * 0.5f).toFloat()

        canvas.drawText("$data_1%", coordX_1, coordY_1, numberPaint)

        val cX_2 =
            canvas.width.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.cos((270f + sweepAngle_1 + sweepAngle_2 / 2) * Math.PI / 180)
        val cY_2 =
            canvas.height.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.sin((270f + sweepAngle_1 + sweepAngle_2 / 2) * Math.PI / 180)

        val textX_2 = (cX_2 + canvas.width.toFloat() / 2) / 2
        val textY_2 = (cY_2 + canvas.height.toFloat() / 2) / 2

        val textWidth_2 = numberPaint.measureText("$data_2%")
        val coordX_2 = Math.round(textX_2 - textWidth_2 * 0.5f).toFloat()

        val textHeight_2 = numberPaint.textSize / 2
        val coordY_2 = Math.round(textY_2 + textHeight_2 * 0.5f).toFloat()

        canvas.drawText("$data_2%", coordX_2, coordY_2, numberPaint)

        val cX_3 =
            canvas.width.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.cos((270f + sweepAngle_1 + sweepAngle_2 + sweepAngle_3 / 2) * Math.PI / 180)
        val cY_3 =
            canvas.height.toFloat() / 2 + canvas.height.toFloat() / 2 * Math.sin((270f + sweepAngle_1 + sweepAngle_2 + sweepAngle_3 / 2) * Math.PI / 180)

        val textX_3 = (cX_3 + canvas.width.toFloat() / 2) / 2
        val textY_3 = (cY_3 + canvas.height.toFloat() / 2) / 2

        val textWidth_3 = numberPaint.measureText("$data_3%")
        val coordX_3 = Math.round(textX_3 - textWidth_3 * 0.5f).toFloat()

        val textHeight_3 = numberPaint.textSize / 2
        val coordY_3 = Math.round(textY_3 + textHeight_3 * 0.5f).toFloat()

        canvas.drawText("$data_3%", coordX_3, coordY_3, numberPaint)
    }

    fun setData(data_1: Int, data_2: Int, data_3: Int) {
        this.data_1 = data_1
        this.data_2 = data_2
        this.data_3 = data_3
        invalidate()
    }

}