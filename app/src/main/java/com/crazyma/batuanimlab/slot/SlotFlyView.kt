package com.crazyma.batuanimlab.slot

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

/**
 * 10 icon per second
 */
class SlotFlyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        const val DURATION_PROGRESS_ONE = 1
        const val DURATION_PROGRESS_TWO = 2
        const val DURATION_PROGRESS_THREE = 3
        const val DURATION_END = 2

        const val SLOT_INDEX_ONE = 0
        const val SLOT_INDEX_TWO = 1
        const val SLOT_INDEX_THREE = 2
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

    var slotIndex = SLOT_INDEX_ONE
        set(value) {
            field = value
            progressDuration = when (value) {
                SLOT_INDEX_TWO -> DURATION_PROGRESS_TWO
                SLOT_INDEX_THREE -> DURATION_PROGRESS_THREE
                else -> DURATION_PROGRESS_ONE
            }
            generateOrder()
        }

    var drawableResIds: List<Int>? = null
        set(value) {
            field = value
            generateDrawables()
            generateOrder()
        }

    var endDrawableIndex = 0
        set(value) {
            field = value
            generateOrder()
        }

    private var progressIconCount = 0
    private var endIconCount = 0
    private var progressDuration = DURATION_PROGRESS_ONE
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

    private var maxProgressAnimValue = 0
    private var maxEndAnimValue = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h

        interval = heightSize / 3
        iconHeight = 2 * interval
        iconWidth = iconHeight
        startY = interval / 2
        iconLeft = (widthSize - iconWidth) / 2
        iconRight = iconLeft + iconWidth

        calculateMaxAnimationValue()

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

    fun initPosition(){
        currentValue = 0
    }

    fun startRolling() {

        val animatorSet = AnimatorSet().apply {
            playSequentially(
                ValueAnimator.ofInt(0, maxProgressAnimValue).apply {
                    duration = progressDuration * 1000L
                    interpolator = LinearInterpolator()
                    addUpdateListener {
                        currentValue = it.animatedValue as Int
                    }
                },
                ValueAnimator.ofInt(maxProgressAnimValue, maxEndAnimValue + maxProgressAnimValue).apply {
                    duration = DURATION_END * 1000L
                    interpolator = OvershootInterpolator(0.3f)
                    addUpdateListener {
                        currentValue = it.animatedValue as Int
                    }
                }
            )
        }

        animatorSet.start()
    }

    private fun calculatePosition() {
        if (heightSize == 0) return

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
            val indexList = mutableListOf<Int>()
            generateProgressOrder(indexList)
            generateEndOrder(indexList)

            this.indexList = indexList
            calculateMaxAnimationValue()
        }
    }

    private fun generateProgressOrder(indexList: MutableList<Int>) {
        progressIconCount = (progressDuration * 25 / drawables.size) * drawables.size
        for (i in 0 until progressIconCount) {
            indexList.add(i % drawables.size)
        }
    }

    private fun generateEndOrder(indexList: MutableList<Int>) {

        if (endDrawableIndex < 0 || endDrawableIndex >= drawables.size)
            endDrawableIndex = 0

        endIconCount = (DURATION_END * 10 / drawables.size) * drawables.size
        val list = mutableListOf<Int>()
        for (i in 0 until endIconCount) {
            list.add((endDrawableIndex + i) % drawables.size)
        }
        list.reverse()

        indexList.addAll(list)
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
        maxProgressAnimValue = heightSize * when (progressIconCount) {
            0 -> 0
            else -> progressIconCount
        }
        maxEndAnimValue = heightSize * when (endIconCount) {
            0 -> 0
            else -> endIconCount - 1
        }
    }
}