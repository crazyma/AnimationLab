package com.crazyma.batuanimlab.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import android.os.Parcel
import android.os.Parcelable
import com.crazyma.batuanimlab.R


class BatuProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val INDICATOR_PADDING_DEFAULT = 30
        const val PROGRESS_LINE_WIDTH = 30
    }

    var indicatorWidth: Int = -1
        set(value) {
            field = value
            calculateIndicatorSize()
            requestLayout()
        }

    var indicatorDrawable: Drawable? = null
        set(value) {
            field = value
            calculateIndicatorSize()
            requestLayout()
        }

    var indicatorPadding = INDICATOR_PADDING_DEFAULT
        set(value) {
            field = value
            requestLayout()
        }

    var progressLineWidth = PROGRESS_LINE_WIDTH
        set(value) {
            field = value
            requestLayout()
        }

    var baseColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    var progressColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    var percentage = 0.5f
        set(value) {
            field = value
            calculateProgressPosition(value)
            invalidate()
        }

    private var finalIndicatorWidth = 0
    private var finalIndicatorHeight = 0
    private var progressY = 0f
    private var baseStartX = 0f
    private var baseEndX = 0f
    private var progressStartX = 0f
    private var progressEndX = 0f

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BatuProgressView,
            0, 0
        )

        try {
            baseColor = a.getColor(R.styleable.BatuProgressView_baseColor, Color.BLACK)
            progressColor = a.getColor(R.styleable.BatuProgressView_progressColor, Color.RED)
            indicatorPadding =
                a.getDimensionPixelSize(R.styleable.BatuProgressView_indicatorPadding, INDICATOR_PADDING_DEFAULT)
            progressLineWidth =
                a.getDimensionPixelSize(R.styleable.BatuProgressView_progressLineWidth, PROGRESS_LINE_WIDTH)

            val drawableResId = a.getResourceId(R.styleable.BatuProgressView_indicator, -1)
            if (drawableResId != -1)
                indicatorDrawable = AppCompatResources.getDrawable(context, drawableResId)

            indicatorWidth = a.getDimensionPixelSize(R.styleable.BatuProgressView_indicatorWidth, -1)

        } finally {
            a.recycle()
        }

        calculateIndicatorSize()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.apply {
            strokeWidth = progressLineWidth.toFloat()
            color = baseColor
        }
        canvas.drawLine(baseStartX, progressY, baseEndX, progressY, paint)

        paint.color = progressColor
        canvas.drawLine(progressStartX, progressY, progressEndX, progressY, paint)

        indicatorDrawable?.run {
            setBounds(
                (progressStartX - finalIndicatorWidth / 2f).toInt(),
                0,
                (progressStartX + finalIndicatorWidth / 2f).toInt(),
                finalIndicatorHeight
            )
            draw(canvas)
        }
    }

    /**
     * view 的高度: progress height + padding + indicator height
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            else -> {
                finalIndicatorHeight + indicatorPadding + progressLineWidth
            }
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateBasePosition(w)
        calculateProgressPosition(percentage)
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState()!!)
        savedState.apply {
            baseColor = this@BatuProgressView.baseColor
            progressColor = this@BatuProgressView.progressColor
            indicatorPadding = this@BatuProgressView.indicatorPadding
            progressLineWidth = this@BatuProgressView.progressLineWidth
            percentage = this@BatuProgressView.percentage
        }
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            state.run {
                this@BatuProgressView.baseColor = baseColor
                this@BatuProgressView.progressColor = progressColor
                this@BatuProgressView.indicatorPadding = indicatorPadding
                this@BatuProgressView.progressLineWidth = progressLineWidth
                this@BatuProgressView.percentage = percentage
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun calculateProgressPosition(percentage: Float) {
        val distance = baseEndX - baseStartX
        progressStartX = distance * percentage + paddingStart.toFloat()
        progressEndX = baseEndX
    }

    private fun calculateBasePosition(viewWidth: Int) {
        progressY = finalIndicatorHeight + indicatorPadding + progressLineWidth * 0.5f
        baseStartX = paddingStart.toFloat()
        baseEndX = (viewWidth - paddingEnd).toFloat()
    }

    private fun calculateIndicatorSize() {
        val drawable = indicatorDrawable
        if (drawable != null) {
            if (indicatorWidth > 0) {
                finalIndicatorWidth = indicatorWidth
                finalIndicatorHeight = indicatorWidth * drawable.intrinsicHeight / drawable.intrinsicWidth
            } else {
                finalIndicatorWidth = drawable.intrinsicWidth
                finalIndicatorHeight = drawable.intrinsicHeight
            }
        } else {
            finalIndicatorWidth = 0
            finalIndicatorHeight = 0
        }
    }

    internal class SavedState : BaseSavedState {
        //        var value: Int = 0 //this will store the current value from ValueBar
        var baseColor = 0
        var progressColor = 0
        var indicatorPadding = 0
        var progressLineWidth = 0
        var percentage = 0f

        constructor(source: Parcel) : super(source) {
            baseColor = source.readInt()
            progressColor = source.readInt()
            indicatorPadding = source.readInt()
            progressLineWidth = source.readInt()
            percentage = source.readFloat()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(baseColor)
            out.writeInt(progressColor)
            out.writeInt(indicatorPadding)
            out.writeInt(progressLineWidth)
            out.writeFloat(percentage)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}