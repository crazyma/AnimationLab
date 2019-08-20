package com.crazyma.batuanimlab.shinymask

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.crazyma.batuanimlab.R

/**
 * Using [ProterDuff.Mode.CLEAR] to achieve the crop effect. To do so, you have to disable hardware acceleration.
 * (Read more about hardware acceleration: https://developer.android.com/guide/topics/graphics/hardware-accel)
 *
 * The other way to achieve this effect, I think, is to create a bitmap, like [createBitmap],
 * then draw on this view
 */
class ShinyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val snorlaxBitmap = BitmapFactory.decodeResource(resources, R.drawable.snolax)
    private lateinit var shaderPaint: Paint

    init {
        //  stop using hardware acceleration
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shaderPaint = createGradientPaint(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(snorlaxBitmap, 0f, 0f, null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), shaderPaint)
        canvas.drawRect(0f, 0f, snorlaxBitmap.width * 0.5f, snorlaxBitmap.height * 0.5f, paint)
    }

    private fun createGradientPaint(width: Int, height: Int): Paint {
        val shaderPaint = Paint()

        val centerX = width * 0.5f
        val centerY = height * 0.5f
        val radius = width * 0.25f

        val colorArray = intArrayOf(Color.WHITE, Color.WHITE, Color.TRANSPARENT)
        val positionArray = floatArrayOf(0f, 0.25f, 1f)

        val radialGradient = RadialGradient(centerX, centerY, radius, colorArray, positionArray, Shader.TileMode.CLAMP)
        shaderPaint.shader = radialGradient
        return shaderPaint
    }

    private fun createBitmap(): Bitmap {
        val originBitmap = BitmapFactory.decodeResource(resources, R.drawable.snolax)

        val finalBitmap = Bitmap.createBitmap(originBitmap.width, originBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        canvas.drawBitmap(originBitmap, 0f, 0f, null)
        val shaderPaint = Paint()

        val centerX = originBitmap.width * 0.5f
        val centerY = originBitmap.height * 0.5f
        val radius = originBitmap.width * 0.25f

        val colorArray = intArrayOf(Color.WHITE,Color.WHITE, Color.TRANSPARENT )
        val positionArray = floatArrayOf(0f, 0.25f, 1f)

        val radialGradient = RadialGradient(centerX, centerY, radius, colorArray, positionArray, Shader.TileMode.CLAMP)

        shaderPaint.shader = radialGradient
        canvas.drawRect(0f, 0f, originBitmap.width.toFloat(), originBitmap.height.toFloat(), shaderPaint)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        canvas.drawRect(0f,0f,centerX,centerY,paint)

        return finalBitmap
    }

}