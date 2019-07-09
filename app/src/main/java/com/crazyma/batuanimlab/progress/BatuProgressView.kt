package com.crazyma.batuanimlab.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.crazyma.batuanimlab.R

class BatuProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val INDICATOR_WIDTH_DEFAULT = 60
        const val INDICATOR_HEIGHT_DEFAULT = 60
        const val INDICATOR_PADDING_DEFAULT = 30
        const val PROGRESS_LINE_WIDTH = 30
    }

    private var indicatorDrawable: Drawable? = null
    private var indicatorWidth = INDICATOR_WIDTH_DEFAULT
    private var indicatorHeight = INDICATOR_HEIGHT_DEFAULT
    private var indicatorPadding = INDICATOR_PADDING_DEFAULT
    private var progressLineWidth = PROGRESS_LINE_WIDTH
    private var progressY = 0f
    private var baseStartX = 0f
    private var baseEndX = 0f
    private var progressStartX = 0f
    private var progressEndX = 0f
    private var percentage = 0.5f
    private var indicatorRect = Rect()

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BatuProgressView,
            0, 0
        )

        try {
            // Resources$NotFoundException if vector image
            // badge = a.getDrawable(R.styleable.PreferencesBadge_badge)
            val drawableResId = a.getResourceId(R.styleable.BatuProgressView_indicator, -1)
            this.indicatorDrawable = AppCompatResources.getDrawable(context, drawableResId)
        } finally {
            a.recycle()
        }

        indicatorWidth = indicatorDrawable?.intrinsicWidth ?: INDICATOR_WIDTH_DEFAULT
        indicatorHeight = indicatorDrawable?.intrinsicHeight ?: INDICATOR_HEIGHT_DEFAULT
        indicatorPadding = INDICATOR_PADDING_DEFAULT
        progressLineWidth = PROGRESS_LINE_WIDTH
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.apply{
            strokeWidth = progressLineWidth.toFloat()
            color = Color.BLACK
        }
        canvas.drawLine(baseStartX, progressY, baseEndX , progressY, paint)

        paint.color = Color.BLUE
        canvas.drawLine(progressStartX, progressY, progressEndX , progressY, paint)

        indicatorDrawable?.run {
            setBounds((progressStartX - indicatorWidth/2f).toInt(), 0, (progressStartX + indicatorWidth/2f).toInt(), indicatorHeight)
            draw(canvas)
        }
    }

    /**
     * view 的高度: progress height + padding + indicator height
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        calculateBasePosition(widthSize)
        calculateProgressPosition(percentage)

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            else -> {
                indicatorHeight + indicatorPadding + progressLineWidth
            }
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY))
    }

    private fun calculateProgressPosition(percentage: Float){
        val distance = baseEndX - baseStartX
        progressStartX = distance * percentage + paddingStart.toFloat()
        progressEndX = baseEndX
    }

    private fun calculateBasePosition(viewWidth: Int){
        progressY = indicatorHeight + indicatorPadding + progressLineWidth * 0.5f
        baseStartX = paddingStart.toFloat()
        baseEndX = (viewWidth - paddingEnd).toFloat()
    }
}