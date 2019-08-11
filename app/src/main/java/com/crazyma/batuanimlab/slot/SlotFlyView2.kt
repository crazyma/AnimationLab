package com.crazyma.batuanimlab.slot

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd

/**
 * support bitmap
 */
class SlotFlyView2 @JvmOverloads constructor(
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

    var bitmaps: List<Bitmap>? = null
        set(value) {
            field = value
            generateOrder()
        }

    var endBitmapIndex = 0
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
    private var firstVisibleBitmap: Bitmap? = null
    private var secondVisibleBitmap: Bitmap? = null

    private var maxProgressAnimValue = 0
    private var maxEndAnimValue = 0
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var firstRect = Rect()
    private var secondRect = Rect()

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

        firstVisibleBitmap?.run {
            canvas.drawBitmap(this, null, firstRect, paint)
        }

        secondVisibleBitmap?.run {
            canvas.drawBitmap(this, null, secondRect, paint)
        }
    }

    fun initPosition() {
        currentValue = 0
    }

    fun finalPosition() {
        post {
            currentValue = maxEndAnimValue + maxProgressAnimValue
        }
    }

    fun startRolling(finishedListener: (() -> Unit)? = null) {

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
            doOnEnd {
                finishedListener?.invoke()
            }
        }

        animatorSet.start()
    }

    fun isReady() = !bitmaps.isNullOrEmpty()

    private fun calculatePosition() {

        if (heightSize == 0) return

        quotient = currentValue / heightSize
        reminder = currentValue % heightSize

        if (quotient % 2 == 0) {    //  even, mean: first on top
            firstIconPositionY = startY - reminder
            secondIconPositionY = firstIconPositionY + iconHeight + interval

            firstVisibleBitmap = getVisibleBitmap(quotient)
            secondVisibleBitmap = getVisibleBitmap(quotient + 1)
        } else {                    //  odd, mean: second on top
            secondIconPositionY = startY - reminder
            firstIconPositionY = secondIconPositionY + iconHeight + interval

            secondVisibleBitmap = getVisibleBitmap(quotient)
            firstVisibleBitmap = getVisibleBitmap(quotient + 1)
        }

        firstRect = Rect(iconLeft, firstIconPositionY, iconRight, (firstIconPositionY + iconHeight))
        secondRect = Rect(iconLeft, secondIconPositionY, iconRight, (secondIconPositionY + iconHeight))
    }

    private fun generateOrder() {
        bitmaps?.run {
            if (size > 0) {
                val indexList = mutableListOf<Int>()
                generateProgressOrder(this, indexList)
                generateEndOrder(this, indexList)

                this@SlotFlyView2.indexList = indexList
                calculateMaxAnimationValue()
            }
        }
    }

    private fun generateProgressOrder(bitmaps: List<Bitmap>, indexList: MutableList<Int>) {
        if (bitmaps.isNotEmpty()) {
            progressIconCount = (progressDuration * 25 / bitmaps.size) * bitmaps.size
            for (i in 0 until progressIconCount) {
                indexList.add(i % bitmaps.size)
            }
        }
    }

    private fun generateEndOrder(bitmaps: List<Bitmap>, indexList: MutableList<Int>) {

        if (endBitmapIndex < 0 || endBitmapIndex >= bitmaps.size)
            endBitmapIndex = 0

        endIconCount = (DURATION_END * 10 / bitmaps.size) * bitmaps.size
        val list = mutableListOf<Int>()
        for (i in 0 until endIconCount) {
            list.add((endBitmapIndex + i) % bitmaps.size)
        }
        list.reverse()

        indexList.addAll(list)
    }

    private fun getVisibleBitmap(index: Int): Bitmap? {
        val indexList = this.indexList
        val bitmaps = this.bitmaps

        return if (!indexList.isNullOrEmpty() && !bitmaps.isNullOrEmpty() &&
            index >= 0 && index < indexList.size
        ) {
            bitmaps[indexList[index]]
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