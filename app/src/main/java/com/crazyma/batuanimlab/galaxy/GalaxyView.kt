package com.crazyma.batuanimlab.galaxy

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.withMatrix
import com.crazyma.batuanimlab.R
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Batu
 */
class GalaxyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIM_INTERVAL = 10 * 1000L
    }


    private lateinit var greenGalaxyDrawable: Drawable
    private lateinit var blueGalaxyDrawable: Drawable

    private var topOffset = 0f
    private var rotateAngle = 0.0
    private var count = 2
    private var galaxyDrawableWidth = 0

    private var animStartValue = 0f
    private var animEndValue = 0f

    private val animMatrices = mutableListOf<Matrix>()

    init {
        initGalaxyDrawable()
    }

    private fun initGalaxyDrawable() {
        blueGalaxyDrawable =
            resources.getDrawable(R.drawable.img_card_galaxy_blue, null).apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }

        greenGalaxyDrawable =
            resources.getDrawable(R.drawable.img_card_galaxy_green, null).apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }

        galaxyDrawableWidth = greenGalaxyDrawable.intrinsicWidth
        topOffset = greenGalaxyDrawable.intrinsicHeight * 0.5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val diagonalLength = sqrt(
            (w.toDouble().pow(2.toDouble()) + h.toDouble().pow(2.toDouble()))
        )
        while (count * galaxyDrawableWidth < diagonalLength) {
            count += 1
        }
        rotateAngle = atan(h.toDouble() / w.toDouble()) * 180 / Math.PI
        animEndValue = (count - 1) * galaxyDrawableWidth.toFloat()

        prepareMatrices(count)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        animMatrices.forEach {
            canvas.withMatrix(it) {
                blueGalaxyDrawable.draw(this)
                greenGalaxyDrawable.draw(this)
            }
        }
    }

    fun startAnim() {
        ValueAnimator.ofFloat(animStartValue, animEndValue).apply {
            duration = ANIM_INTERVAL
            repeatCount = -1
            interpolator = LinearInterpolator()
            addUpdateListener {

                val animOffset = it.animatedValue as Float
                animMatrices.forEachIndexed { index, matrix ->
                    setupMatrix(matrix, index, animOffset)
                }

                postInvalidate()
            }
        }.start()
    }

    private fun prepareMatrices(count: Int) {
        animMatrices.clear()
        for (index in 0 until count) {
            Matrix().apply {
                postTranslate(index * galaxyDrawableWidth.toFloat(), -topOffset)
                postRotate(rotateAngle.toFloat())
            }.let {
                animMatrices.add(it)
            }
        }
    }

    private fun setupMatrix(matrix: Matrix, index: Int, animOffset: Float) {
        matrix.apply {
            reset()
            postTranslate(index * galaxyDrawableWidth.toFloat() - animOffset, -topOffset)
            postRotate(rotateAngle.toFloat())
        }
    }

}