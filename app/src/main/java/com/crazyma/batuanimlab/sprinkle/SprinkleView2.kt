package com.crazyma.batuanimlab.sprinkle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.crazyma.batuanimlab.R

class SprinkleView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        const val BIRTH_TIME_INTERVAL_FIRST = 5000
        const val BIRTH_TIME_INTERVAL_WHOLE = 10000
        const val ANIM_DURATION_INTERVAL = 5000
        const val ANIM_DURATION_MIN = 5000
    }

    private var animator: ValueAnimator? = null
    private var sprinkleObjects: List<SprinkleObject>? = null
    private var time = 0

    private var colorBlue = ContextCompat.getColor(context, R.color.colorBlue)
    private var colorPink = ContextCompat.getColor(context, R.color.colorPink)
    private var colorOrange = ContextCompat.getColor(context, R.color.colorOrange)

    override fun onDraw(canvas: Canvas) {

        sprinkleObjects?.forEach {
            if (it.checkAvailable(time)) {
                val sprinkX = bézier(it.birthX, it.p1X, it.deathX, it.getProgress(time))
                val sprinkY = bézier(it.birthY, it.p1Y, it.deathY, it.getProgress(time))
                val rotateDegree = it.rotateDegree * time

                canvas.withTranslation {
                    translate(sprinkX, sprinkY)
                    rotate(rotateDegree, it.drawable.intrinsicWidth / 2f, it.drawable.intrinsicHeight / 2f)
                    it.drawable.draw(this)
                }
            }
        }
    }

    fun runAnim() {
        var maxDeathTime = 0
        sprinkleObjects = generateObject().also {
            it.forEach { sprinkleObject ->
                maxDeathTime = Math.max(maxDeathTime, sprinkleObject.deathTime)
            }
        }

        animator?.cancel()
        animator = ValueAnimator.ofInt(0, maxDeathTime).apply {
            duration = maxDeathTime.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener {
                time = it.animatedValue as Int
                invalidate()
            }
            repeatCount = -1
            start()
        }
    }

    private fun generateObject(): List<SprinkleObject> {
        return mutableListOf<SprinkleObject>().apply {
            val count = 150
            for (i in 0..count) {

                val drawable = when (i % 3) {
                    0 -> {
                        VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark01, null)!!.apply {
                            setColorFilter(colorOrange, PorterDuff.Mode.SRC_IN)
                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                        }
                    }
                    1 -> {
                        VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark02, null)!!.apply {
                            setColorFilter(colorBlue, PorterDuff.Mode.SRC_IN)
                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                        }
                    }
                    else -> {
                        VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark03, null)!!.apply {
                            setColorFilter(colorPink, PorterDuff.Mode.SRC_IN)
                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                        }
                    }
                }

                val birthTimeInterval = when {
                    i < count / 2 -> BIRTH_TIME_INTERVAL_FIRST
                    else -> BIRTH_TIME_INTERVAL_WHOLE
                }
                val birthX = (Math.random() * width * 0.5 + width * 0.25).toFloat()
                val birthY = -100f
                val deathX = (Math.random() * width * 1.5 - width * 0.25).toFloat()
                val deathY = height.toFloat() + 100f
                val p1X = if (i % 2 == 0) 0f else width.toFloat()
                val p1Y = height / 2f
                val rotateDegree = (Math.random()).toFloat() * 0.5f
                val birthTime = (birthTimeInterval * Math.random()).toInt()
                val deathTime = birthTime + ANIM_DURATION_MIN + (ANIM_DURATION_INTERVAL * Math.random()).toInt()

                val sprinkleObject = SprinkleObject(
                    drawable, birthX, birthY, deathX, deathY, p1X, p1Y, rotateDegree, birthTime, deathTime
                )

                add(sprinkleObject)
            }
        }
    }

    private fun bézier(p0: Int, p1: Int, p2: Int, p3: Int, t: Float) =
        p0 * (1 - t) * (1 - t) * (1 - t) +
                3 * p1 * t * (1 - t) * (1 - t) +
                3 * p2 * t * t * (1 - t) +
                p3 * t * t * t

    private fun bézier(p0: Float, p1: Float, p2: Float, t: Float) =
        p0 * (1 - t) * (1 - t) +
                2 * p1 * t * (1 - t) +
                p2 * t * t


    private fun bézier(p0: Float, p1: Float, t: Float) = p0 * (1 - t) + p1 * t


    private data class SprinkleObject(
        val drawable: VectorDrawableCompat,
        val birthX: Float,
        val birthY: Float,
        val deathX: Float,
        val deathY: Float,
        val p1X: Float,
        val p1Y: Float,
        var rotateDegree: Float,
        val birthTime: Int,
        val deathTime: Int
    ) {
        fun checkAvailable(time: Int) = time in birthTime..deathTime

        fun getProgress(time: Int): Float {
            return when {
                time < birthTime -> 0f
                time > deathTime -> 1f
                else -> (time - birthTime).toFloat() / (deathTime - birthTime)
            }
        }
    }
}