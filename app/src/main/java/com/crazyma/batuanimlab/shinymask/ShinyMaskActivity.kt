package com.crazyma.batuanimlab.shinymask

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_shiny_mask.*

class ShinyMaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shiny_mask)
        Handler().postDelayed({
            snorlaxImageView.setImageBitmap(createBitmap())
        }, 500)
    }

    private fun createBitmap(): Bitmap {
        val originBitmap = BitmapFactory.decodeResource(resources, R.drawable.snolax)

        val finalBitmap = Bitmap.createBitmap(originBitmap.width, originBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        canvas.drawBitmap(originBitmap, 0f, 0f, null)
        val shaderPaint = Paint()
        val linearGradient =
            LinearGradient(0f, 0f, 0f, originBitmap.height + 1f, 0x7fffffff, 0x00ffffff, Shader.TileMode.MIRROR)

        val centerX = originBitmap.width * 0.5f
        val centerY = originBitmap.height * 0.5f
        val radius = originBitmap.width * 0.25f

        val colorArray = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE)
        val positionArray = floatArrayOf(0f, 0.25f, 1f)

        val radialGradient = RadialGradient(centerX, centerY, radius, colorArray, positionArray, Shader.TileMode.CLAMP)

        shaderPaint.shader = radialGradient
        shaderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawRect(0f, 0f, originBitmap.width.toFloat(), originBitmap.height.toFloat(), shaderPaint)
        return finalBitmap
    }

    private fun createBitmap1(): Bitmap {
        val originBitmap = BitmapFactory.decodeResource(resources, R.drawable.snolax)

        val testBitmap =  Bitmap.createBitmap(originBitmap.width, originBitmap.height, Bitmap.Config.ARGB_8888)
        val testCanvas = Canvas (testBitmap)
        val testPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
            color = Color.RED
        }
        testCanvas.drawRect(0f,0f,originBitmap.width * .5f, originBitmap.height * .5f,testPaint)

        val finalBitmap = Bitmap.createBitmap(originBitmap.width, originBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        canvas.drawBitmap(testBitmap, 0f, 0f, null)
        val shaderPaint = Paint()
        val linearGradient =
            LinearGradient(0f, 0f, 0f, originBitmap.height + 1f, 0x7fffffff, 0x00ffffff, Shader.TileMode.MIRROR)

        val centerX = originBitmap.width * 0.5f
        val centerY = originBitmap.height * 0.5f
        val radius = originBitmap.width * 0.25f

        val colorArray = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE)
        val positionArray = floatArrayOf(0f, 0.25f, 1f)

        val radialGradient = RadialGradient(centerX, centerY, radius, colorArray, positionArray, Shader.TileMode.CLAMP)

        shaderPaint.shader = radialGradient
        shaderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawRect(0f, 0f, originBitmap.width.toFloat(), originBitmap.height.toFloat(), shaderPaint)
        return finalBitmap
    }


}