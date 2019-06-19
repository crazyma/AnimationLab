package com.crazyma.batuanimlab.slot

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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

    //  second
    var duration = DURATION_DEFAULT
        set(value) {
            field = value
            generateOrder()
        }

    var drawableResIds: List<Int>? = null
        set(value) {
            field = value
            generateDrawables()
            generateOrder()
        }

    private var firstIconPositionY = 0
    private var secondIconPositionY = 0
    private var currentValue = 0
        set(value) {
            field = value
            calculatePosition()
            postInvalidate()
        }

    private var indexList: List<Int>? = null
    private val drawables = mutableListOf<Drawable>()
    private var firstVisibleDrawable: Drawable? = null
    private var secondVisibleDrawable: Drawable? = null

    private var maxAnimationValue = 0

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

        calculateMaxAnimationValue()

//        Log.i("badu", "-------------")
//        Log.d("badu", "heightSize: $heightSize")
//        Log.d("badu", "interval: $interval")
//        Log.d("badu", "iconHeight: $iconHeight")
//        Log.d("badu", "iconWidth: $iconWidth")
//        Log.d("badu", "startY: $startY")
//        Log.d("badu", "maxAnimationValue: $maxAnimationValue")
//        Log.d("badu", "currentValue: $currentValue")

        calculatePosition()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        firstVisibleDrawable?.run {
            setBounds(iconLeft, firstIconPositionY, iconRight, (firstIconPositionY + iconHeight))
            draw(canvas)
        }

        secondVisibleDrawable?.run {
            setBounds(iconLeft, secondIconPositionY, iconRight, (secondIconPositionY + iconHeight))
            draw(canvas)
        }
    }

    fun startRolling(delay: Long = 0L) {
        ValueAnimator.ofInt(0, maxAnimationValue).apply {
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

            firstVisibleDrawable = getVisibleDrawable(quotient)
            secondVisibleDrawable = getVisibleDrawable(quotient + 1)
        } else {                    //  odd, mean: second on top
            secondIconPositionY = startY - reminder
            firstIconPositionY = secondIconPositionY + iconHeight + interval

            secondVisibleDrawable = getVisibleDrawable(quotient)
            firstVisibleDrawable = getVisibleDrawable(quotient + 1)
        }
    }

    private fun generateDrawables() {
        drawables.clear()
        drawableResIds?.forEach { drawableResId ->
            drawables.add(resources.getDrawable(drawableResId, null))
        }
    }

    private fun generateOrder() {
        if (drawables.size > 0) {
            val totalIconsCount = (duration * 10 / drawables.size) * drawables.size
            val indexList = mutableListOf<Int>()
            for (i in 0 until totalIconsCount) {
                indexList.add(i % drawables.size)
            }

            this.indexList = indexList
            calculateMaxAnimationValue()
        }
    }

    private fun getVisibleDrawable(index: Int): Drawable? {
        val indexList = this.indexList

        return if (!indexList.isNullOrEmpty() && drawables.isNotEmpty() &&
            index >= 0 && index < indexList.size
        ) {
            drawables[indexList[index]]
        } else {
            null
        }
    }

    private fun calculateMaxAnimationValue() {
        maxAnimationValue = indexList?.run {
            if (isEmpty()) 0
            else (size - 1) * heightSize
        } ?: 0
    }
}