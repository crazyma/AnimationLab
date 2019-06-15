package com.crazyma.batuanimlab.slot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
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

    private var currentValue = 525 + 525 + 525
    private var firstIconPositionY = 0
    private var secondIconPositionY = 0

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

        Log.i("badu","-------------")
        Log.d("badu", "heightSize: $heightSize")
        Log.d("badu", "interval: $interval")
        Log.d("badu", "iconHeight: $iconHeight")
        Log.d("badu", "iconWidth: $iconWidth")
        Log.d("badu", "startY: $startY")

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


    private fun calculatePosition() {

        quotient = currentValue / heightSize
        reminder = currentValue % heightSize

        if (quotient % 2 == 0) {    //  even, mean: first on top
            Log.v("badu","first on top")
            firstIconPositionY = startY - reminder
            secondIconPositionY = firstIconPositionY + iconHeight + interval
        } else {                    //  odd, mean: second on top
            Log.v("badu","second on top")
            secondIconPositionY = startY - reminder
            firstIconPositionY = secondIconPositionY + iconHeight + interval
        }

    }
}