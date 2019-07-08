package com.crazyma.batuanimlab.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BatuProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 40f
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(30f,60f,width.toFloat() - 30f,60f, paint)
    }

}