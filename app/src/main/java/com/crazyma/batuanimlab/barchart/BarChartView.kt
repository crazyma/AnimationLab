package com.crazyma.batuanimlab.barchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.crazyma.batuanimlab.R

/**
 * @author Batu
 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val BAR_COUNT = 7
    }

    private val density
        get() = context.resources.displayMetrics.density

    private val barPaint = Paint().apply {
        color = Color.RED
    }

    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1 * density
    }

    private var xFactorHeight = 0
    private var yPositionWidth = 0
    private var yPositionPaddingStart = 0f
    private var xFactorPaddingTop = 8 * density
    private var barWidth = 0f

    private val textPaint = Paint().apply {
        color = Color.BLACK // This is treated as an avatar, not text label.
        textAlign = Paint.Align.CENTER
        textSize = 14 * context.resources.displayMetrics.density
        isAntiAlias = true
    }

    private var testDateStrings = listOf(
        "9/29", "9/30", "10/1", "10/2", "10/3", "10/4", "10/5"
    )
    private var dates: MutableList<Pair<String, Int>> = mutableListOf()
    private var barPosition: List<Float> = mutableListOf()
    private var linePositionY: List<Float> = mutableListOf()
    private var barPaddingStart = 0f
        set(value) {
            field = value
            calculateXPositionWidthList()
            invalidate()
        }

    private var barPaddingEnd = 0f
        set(value) {
            field = value
            calculateXPositionWidthList()
            invalidate()
        }

    private var linePaddingTop = 0f
        set(value) {
            field = value
            calculateLineYPositionInfo()
            invalidate()
        }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BarChartView,
            0, 0
        )

        try {
            barPaddingStart = a.getDimension(R.styleable.BarChartView_barPaddingStart, 0f)
            barPaddingEnd = a.getDimension(R.styleable.BarChartView_barPaddingEnd, 0f)
            linePaddingTop = a.getDimension(R.styleable.BarChartView_linePaddingTop, 0f)
            yPositionPaddingStart =
                a.getDimension(R.styleable.BarChartView_yPositionPaddingStart, 0f)
        } finally {
            a.recycle()
        }

        calculateYPositionWidth()
        calculateXPositionHeight()
        calculateXPositionWidthList()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateBarXPositionsInfo()
        calculateLineYPositionInfo()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBars(canvas)
        drawXFactor(canvas)
        drawYPositions(canvas)
        drawLine(canvas)
    }

    private fun calculateYPositionWidth() {
        val bounds = Rect()
        val string = "00000"
        textPaint.getTextBounds(string, 0, string.length, bounds)
        yPositionWidth = bounds.width()
    }

    private fun calculateXPositionHeight() {
        val bounds = Rect()
        val string = "12/31"
        textPaint.getTextBounds(string, 0, string.length, bounds)
        xFactorHeight = bounds.height()
    }

    private fun calculateXPositionWidthList() {
        dates.clear()
        val bounds = Rect()
        testDateStrings.forEach {
            textPaint.getTextBounds(it, 0, it.length, bounds)
            dates.add(Pair(it, bounds.width()))
        }
    }

    private fun calculateBarXPositionsInfo() {
        val minusWidth = density * 4
        barWidth = density * 24
        var gapWidth = 0f
        var totalBarsWidth = barWidth * BAR_COUNT
        val barDisplayAreaWidth =
            width - barPaddingStart - barPaddingEnd - yPositionPaddingStart - yPositionWidth - paddingStart - paddingEnd

        while (totalBarsWidth > barDisplayAreaWidth) {
            when {
                barWidth >= minusWidth * 2 -> barWidth -= minusWidth
                else -> throw RuntimeException("Too small") //  TODO by Batu: fix it
            }
            totalBarsWidth = barWidth * BAR_COUNT
        }
        gapWidth = (barDisplayAreaWidth - totalBarsWidth) / (BAR_COUNT - 1).toFloat()

        barPosition = mutableListOf<Float>().apply {
            for (i in 0 until 7) {
                add(paddingStart + barPaddingStart + barWidth * 0.5f + i * (barWidth + gapWidth))
            }
        }
    }

    private fun calculateLineYPositionInfo() {
        val bottomLinePositionY = height.toFloat() - xFactorHeight - xFactorPaddingTop
        val topLinePositionY = linePaddingTop
        val middleLinePositionY = (bottomLinePositionY + topLinePositionY) * 0.5f
        linePositionY = listOf(topLinePositionY, middleLinePositionY, bottomLinePositionY)
    }

    private fun drawBars(canvas: Canvas) {
        val barHeight = density * 60
        val barPositionBottom = height.toFloat() - xFactorHeight - xFactorPaddingTop
        barPaint.strokeWidth = barWidth
        barPosition.forEach { position ->
            canvas.drawLine(
                position,
                barPositionBottom,
                position,
                barPositionBottom - barHeight,
                barPaint
            )
        }
    }

    private fun drawXFactor(canvas: Canvas) {
        dates.forEachIndexed { index, (text, width) ->
            canvas.drawText(text, barPosition[index], height.toFloat(), textPaint)
        }
    }

    private fun drawYPositions(canvas: Canvas) {

        val texts = listOf<String>("1000", "111", "0k")
        val bounds = Rect()
        var textWidth = 0
        var textHeight = 0

        texts.forEachIndexed { index, s ->
            textPaint.getTextBounds(s, 0, s.length, bounds)
            textWidth = bounds.width()
            textHeight = bounds.height()
            canvas.drawText(
                s,
                width - textWidth * .5f - paddingEnd,
                linePositionY[index] + textHeight * 0.5f,
                textPaint
            )
        }
    }

    private fun drawLine(canvas: Canvas) {
        val lineStartPositionX = paddingStart.toFloat()
        val lineEndPositionX = width - (paddingEnd + yPositionWidth + yPositionPaddingStart)
        linePositionY.forEach { positionY ->
            canvas.drawLine(
                lineStartPositionX,
                positionY,
                lineEndPositionX,
                positionY,
                linePaint
            )
        }
    }

}