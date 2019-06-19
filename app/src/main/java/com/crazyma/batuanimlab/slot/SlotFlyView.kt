package com.crazyma.batuanimlab.slot

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator

/**
 * 10 icon per second
 *
 *
 */
class SlotFlyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val DURATION_DEFAULT = 2
    }

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
            postInvalidate()
        }

    //  second
    var duration = DURATION_DEFAULT
        set(value) {
            field = value
            generateOrder()
        }

    private var firstIconPositionY = 0
    private var secondIconPositionY = 0

    private var indexList = listOf(
        0, 1, 2, 3,
        0, 1, 2, 3,
        0, 1, 2, 3,
        0, 1, 2, 3,
        0, 1, 2, 3
    )

    private var iconList = listOf(
        android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple
    )

    var drawableResIds: List<Int>? = null
        set(value) {
            field = value
            generateDrawables()
            generateOrder()
        }

    private val drawables = mutableListOf<Drawable>()
    private var appearingDrawable1: Drawable? = null
    private var appearingDrawable2: Drawable? = null

    private var maxValue = 0

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

        maxValue = (indexList.size - 1) * heightSize

//        Log.i("badu", "-------------")
//        Log.d("badu", "heightSize: $heightSize")
//        Log.d("badu", "interval: $interval")
//        Log.d("badu", "iconHeight: $iconHeight")
//        Log.d("badu", "iconWidth: $iconWidth")
//        Log.d("badu", "startY: $startY")
//        Log.d("badu", "maxValue: $maxValue")
//        Log.d("badu", "currentValue: $currentValue")

        calculatePosition()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        appearingDrawable1?.run {
            setBounds(iconLeft, firstIconPositionY, iconRight, (firstIconPositionY + iconHeight))
            draw(canvas)
        }

        appearingDrawable2?.run {
            setBounds(iconLeft, secondIconPositionY, iconRight, (secondIconPositionY + iconHeight))
            draw(canvas)
        }
    }

    fun startRolling(delay: Long = 0L) {
        ValueAnimator.ofInt(0, maxValue).apply {
            duration = (this@SlotFlyView.duration + delay) * 1000
            interpolator = OvershootInterpolator(0.3f)
            addUpdateListener {
                currentValue = it.animatedValue as Int
            }
        }.start()
    }

    private fun calculatePosition() {
        quotient = currentValue / heightSize
        reminder = currentValue % heightSize

        if (quotient % 2 == 0) {    //  even, mean: first on top
            firstIconPositionY = startY - reminder
            secondIconPositionY = firstIconPositionY + iconHeight + interval

            appearingDrawable1 = getAppearingDrawable(quotient)
            appearingDrawable2 = getAppearingDrawable(quotient + 1)
        } else {                    //  odd, mean: second on top
            secondIconPositionY = startY - reminder
            firstIconPositionY = secondIconPositionY + iconHeight + interval

            appearingDrawable2 = getAppearingDrawable(quotient)
            appearingDrawable1 = getAppearingDrawable(quotient + 1)
        }
    }

    private fun getAppearingDrawable(index: Int) =
        if (drawables.isNotEmpty() && index >= 0 && index < indexList.size) drawables[indexList[index]]
        else null

    private fun generateDrawables() {
        drawables.clear()
        drawableResIds?.forEach { drawableResId ->
            drawables.add(resources.getDrawable(drawableResId, null))
        }
    }

    private fun generateOrder() {
        if (drawables.size > 0) {
            val totalIconsCount = (duration * 5 / drawables.size) * drawables.size
            val indexList = mutableListOf<Int>()
            for (i in 0 until totalIconsCount) {
                indexList.add(i % drawables.size)
            }
            this.indexList = indexList
        }
    }
}