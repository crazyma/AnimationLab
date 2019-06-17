package com.crazyma.batuanimlab.sprinkle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.crazyma.batuanimlab.R

class SprinkleView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val sprinkleObjects = mutableListOf<SprinkleObject>()
    private var time= 0

    override fun onDraw(canvas: Canvas) {

        sprinkleObjects.forEach {

        }
    }

    fun runAnim() {

        val drawable = VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark01, null)!!.apply {
            setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

        //  起點可以超過視窗
        val birthX = (Math.random() * width).toFloat()
        val birthY = 0f
        val deathX = (Math.random() * width).toFloat()
        val deathY = height.toFloat()



        val sprinkleObject = SprinkleObject(
            drawable, birthX, birthY, deathX, deathY, 0 ,5000
        )

        sprinkleObjects.add(sprinkleObject)

        ValueAnimator.ofInt(0, 5000).apply {
            duration = 5000
            interpolator = LinearInterpolator()
            addUpdateListener {
                time = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    private data class SprinkleObject(
        val drawable: VectorDrawableCompat,
        val birthX: Float,
        val birthY: Float,
        val deathX: Float,
        val deathY: Float,
        val birthTime: Int,
        val deathTime: Int
    )
}