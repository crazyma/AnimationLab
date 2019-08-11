package com.crazyma.batuanimlab.inner_trans

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.crazyma.batuanimlab.R
import java.lang.RuntimeException

class ClipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        const val DURATION_OFFSET_ANIM = 500L
        const val OFFSET = 30
    }

    var isPatternShowed = true
        set(value) {
            field = value
            postInvalidate()
        }

    private var patternDrawable: Drawable =
        ContextCompat.getDrawable(context, R.drawable.img_bg_pattern_blue)
            ?: throw RuntimeException("Load patternDrawable resource failed")

    private var foregroundDrawable: Drawable =
        ContextCompat.getDrawable(context, R.drawable.img_bg_pattern_yellow)
            ?: throw RuntimeException("Load foregroundDrawable resource failed")

    private var foregroundWidth = 0f
    private var foregroundHeight = 0f
    private var patternXRepeatCount = 0
    private var patternYRepeatCount = 0
    private var offsetY = (context.resources.displayMetrics.density * OFFSET).toInt()
    private var currentOffset = offsetY
        set(value) {
            field = value
            postInvalidate()
        }
    private var animator: ValueAnimator? = null

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClipView,
            0, 0
        )

        val isClipped: Boolean
        try {
            isClipped = a.getBoolean(R.styleable.ClipView_isClipped, true)
            isPatternShowed = a.getBoolean(R.styleable.ClipView_isPatternShowed, true)
        } finally {
            a.recycle()
        }

        currentOffset = if (isClipped) {
            (context.resources.displayMetrics.density * OFFSET).toInt()
        } else {
            0
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        foregroundHeight = foregroundDrawable.intrinsicHeight * w / foregroundDrawable.intrinsicWidth.toFloat()
        foregroundWidth = w.toFloat()

        patternXRepeatCount = (w / patternDrawable.intrinsicWidth) + 1
        patternYRepeatCount = (h / patternDrawable.intrinsicHeight) + 1
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isPatternShowed) {
            for (i in 0 until patternXRepeatCount) {
                for (j in 0 until patternYRepeatCount) {
                    patternDrawable.run {
                        setBounds(
                            patternDrawable.intrinsicWidth * i,
                            patternDrawable.intrinsicHeight * j - currentOffset,
                            patternDrawable.intrinsicWidth * (i + 1),
                            patternDrawable.intrinsicHeight * (j + 1) - currentOffset
                        )
                        draw(canvas)
                    }
                }
            }
        }

        foregroundDrawable.run {
            setBounds(
                0,
                0 - currentOffset,
                foregroundWidth.toInt(),
                foregroundHeight.toInt() - currentOffset
            )
            draw(canvas)
        }
    }

    fun moveToClipPosition(duration: Long? = null) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(currentOffset, offsetY).apply {
            this.duration = duration ?: DURATION_OFFSET_ANIM
            addUpdateListener {
                currentOffset = it.animatedValue as Int
            }
            start()
        }
    }

    fun moveToTopPosition(duration: Long? = null) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(currentOffset, 0).apply {
            this.duration = duration ?: DURATION_OFFSET_ANIM
            addUpdateListener {
                currentOffset = (it.animatedValue as Int)
            }
            start()
        }
    }
}