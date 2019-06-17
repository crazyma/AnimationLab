package com.crazyma.batuanimlab.slot

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat

class SlotFlyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var interval = 0
    private var iconHeight = 0
    private var iconWidth = 0
    private var iconLeft = 0
    private var iconRight = 0
    private var startY = 0

    private var quotient = 0
    private var reminder = 0

    var currentValue = 0
        set(value) {
            field = value
            calculatePosition()
            invalidate()
        }

    private var firstIconPositionY = 0
    private var secondIconPositionY = 0

    private var colorList = listOf(
        android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple,
        android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple,
        android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple,
        android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple
    )

    private var maxValue = 0

    private var firstPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.holo_green_light)
    }

    private var secondPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.holo_blue_bright)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec)
        heightSize = MeasureSpec.getSize(heightMeasureSpec)

        interval = heightSize / 3
        iconHeight = 2 * interval
        iconWidth = iconHeight
        startY = interval / 2
        iconLeft = (widthSize - iconWidth) / 2
        iconRight = iconLeft + iconWidth

        maxValue = (colorList.size - 1) * heightSize

        Log.i("badu", "-------------")
        Log.d("badu", "heightSize: $heightSize")
        Log.d("badu", "interval: $interval")
        Log.d("badu", "iconHeight: $iconHeight")
        Log.d("badu", "iconWidth: $iconWidth")
        Log.d("badu", "startY: $startY")
        Log.d("badu", "maxValue: $maxValue")
        Log.d("badu", "currentValue: $currentValue")

        calculatePosition()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(
            iconLeft.toFloat(),
            firstIconPositionY.toFloat(),
            iconRight.toFloat(),
            (firstIconPositionY + iconHeight).toFloat(),
            firstPaint
        )

        canvas.drawRect(
            iconLeft.toFloat(),
            secondIconPositionY.toFloat(),
            iconRight.toFloat(),
            (secondIconPositionY + iconHeight).toFloat(),
            secondPaint
        )
    }

    fun startRolling() {
        ValueAnimator.ofInt(0, maxValue).apply {
            duration = 2000
            interpolator = OvershootInterpolator(0.3f)
            addUpdateListener {
                currentValue = it.animatedValue as Int
                calculatePosition()
                postInvalidate()
            }
        }.start()
    }


    private fun calculatePosition() {
        quotient = currentValue / heightSize
        reminder = currentValue % heightSize

        if (quotient % 2 == 0) {    //  even, mean: first on top
            firstIconPositionY = startY - reminder
            secondIconPositionY = firstIconPositionY + iconHeight + interval

            firstPaint.color = ContextCompat.getColor(context, getColor(quotient))
            secondPaint.color = ContextCompat.getColor(context, getColor(quotient + 1))
        } else {                    //  odd, mean: second on top
            secondIconPositionY = startY - reminder
            firstIconPositionY = secondIconPositionY + iconHeight + interval

            secondPaint.color = ContextCompat.getColor(context, getColor(quotient))
            firstPaint.color = ContextCompat.getColor(context, getColor(quotient + 1))
        }
    }

    private fun getColor(index: Int) =
        if (index >= 0 && index < colorList.size) colorList[index]
        else android.R.color.transparent
}