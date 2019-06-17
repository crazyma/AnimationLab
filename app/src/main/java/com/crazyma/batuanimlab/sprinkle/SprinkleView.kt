package com.crazyma.batuanimlab.sprinkle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat

class SprinkleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val points = mutableListOf<Point>()
    val timePoints = mutableListOf<Point>()
    var xx = 0f
    var yy = 0f

    private var firstPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.holo_green_light)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        createPoints(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        calculatePosition()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(xx, yy, 20f, firstPaint)
    }

    fun runAnim(){
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            addUpdateListener {
                val t = it.animatedValue as Float
                xx = thirdLevelBézier(points[0].x, points[1].x, points[2].x, points[3].x, t)
                yy = thirdLevelBézier(points[0].y, points[1].y, points[2].y, points[3].y, t)
                postInvalidate()
            }
        }.start()
    }

    private fun createPoints(width: Int, height: Int) {
        points.apply {
            add(Point(0, 0))
            add(Point(0,  height))
            add(Point(width, 0))
            add(Point(width, height))
        }
    }

    private fun calculatePosition() {
        timePoints.clear()
        var t = 0f
        while (t <= 1f) {
            val x = thirdLevelBézier(points[0].x, points[1].x, points[2].x, points[3].x, t)
            val y = thirdLevelBézier(points[0].y, points[1].y, points[2].y, points[3].y, t)
            timePoints.add(Point(x.toInt(), y.toInt()))
            t += 0.1f
        }
    }

    private fun thirdLevelBézier(p0: Int, p1: Int, p2: Int, p3: Int, t: Float) =
        p0 * (1 - t) * (1 - t) * (1 - t) +
                3 * p1 * t * (1 - t) * (1 - t) +
                3 * p2 * t * t * (1 - t) +
                p3 * t * t * t


}