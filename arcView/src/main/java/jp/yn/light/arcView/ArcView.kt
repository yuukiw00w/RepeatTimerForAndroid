package jp.yn.light.arcView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * onDrawのタイミングでdrawArcを使って円弧を描くためのView
 */
class ArcView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var parameter: ArcDrawParameter? = null
    private val rectF = RectF()
    private var halfStrokeWidth = 0f

    fun draw(arcDrawParameter: ArcDrawParameter?) {
        parameter = arcDrawParameter ?: return
        invalidate()
    }

    private fun initialize(arcInitData: ArcInitData) {
        paint.color = arcInitData.color

        paint.strokeWidth = arcInitData.strokeWidth
        paint.isAntiAlias = arcInitData.isAntiAlias
        paint.style = arcInitData.style
        paint.strokeCap = arcInitData.strokeCap

        halfStrokeWidth = arcInitData.strokeWidth / 2f

        rectF.left = halfStrokeWidth
        rectF.top = halfStrokeWidth
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView, defStyleAttr, 0)
        val initData = ArcInitData(
            typedArray.getColor(
                R.styleable.ArcView_arcColor,
                ContextCompat.getColor(context, R.color.arc)
            ),
            typedArray.getDimension(
                R.styleable.ArcView_arcStrokeWidth,
                context.resources.getDimensionPixelSize(R.dimen.arc_stroke_width).toFloat()
            ),
            typedArray.getBoolean(R.styleable.ArcView_arcIsAntiAlias, true),
            when (typedArray.getInteger(R.styleable.ArcView_arcStyle, 0)) {
                0 -> Paint.Style.FILL
                1 -> Paint.Style.STROKE
                else -> Paint.Style.FILL_AND_STROKE
            },
            when (typedArray.getInteger(R.styleable.ArcView_arcStrokeCap, 0)) {
                0 -> Paint.Cap.BUTT
                1 -> Paint.Cap.ROUND
                else -> Paint.Cap.SQUARE
            }
        )
        initialize(initData)
        val arcDrawParameter = ArcDrawParameter(
            typedArray.getFloat(R.styleable.ArcView_arcStartAngle, 0f),
            typedArray.getFloat(R.styleable.ArcView_arcSweepAngle, 0f),
            typedArray.getBoolean(R.styleable.ArcView_arcUseCenter, true)
        )
        draw(arcDrawParameter)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val parameter = parameter ?: return
        canvas ?: return
        // weightとheightがonDrawのタイミングでないと定まらないので、その部分だけここでsetする
        rectF.right = width.toFloat() - halfStrokeWidth
        rectF.bottom = height.toFloat() - halfStrokeWidth
        canvas.drawArc(
            rectF,
            parameter.startAngle,
            parameter.sweepAngle,
            parameter.useCenter,
            paint
        )
    }
}