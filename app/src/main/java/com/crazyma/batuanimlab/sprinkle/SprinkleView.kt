package com.crazyma.batuanimlab.sprinkle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Build
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.crazyma.batuanimlab.R


class SprinkleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val points = mutableListOf<Point>()
    val timePoints = mutableListOf<Point>()
    var xx = 0f
    var yy = 0f

    var vectorDrawable2: VectorDrawableCompat =
        VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark02, null)!!.apply {
            setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
    var vectorDrawable3: VectorDrawableCompat =
        VectorDrawableCompat.create(context.resources, R.drawable.ic_img_spark03, null)!!.apply {
            setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

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
        canvas.drawBitmap(getBitmapFromVectorDrawable(R.drawable.ic_img_spark01), 100f, 100f, firstPaint)

        canvas.withTranslation {
            translate(200f, 200f)
            rotate(45f, 0f, 0f)
            vectorDrawable2.draw(this)
        }

        canvas.withTranslation {
            translate(300f, 300f)
            vectorDrawable3.draw(this)
        }

    }

    fun runAnim() {
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
            add(Point(0, height))
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

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun thirdLevelBézier(p0: Int, p1: Int, p2: Int, p3: Int, t: Float) =
        p0 * (1 - t) * (1 - t) * (1 - t) +
                3 * p1 * t * (1 - t) * (1 - t) +
                3 * p2 * t * t * (1 - t) +
                p3 * t * t * t


}