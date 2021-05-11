package com.crazyma.batuanimlab.barchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.crazyma.batuanimlab.R
import java.text.DecimalFormat

/**
 * @author Batu
 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var barDataList: List<BarData>? = null
        set(value) {
            field = value
            calculateYPosition()
            calculateBarXPositionsInfo()
            invalidate()
        }

    private var xFactorHeight = 0
    private var yPositionWidth = 0
    private var yPositionPaddingStart = 0f
    private var xFactorPaddingTop = 8 * density
    private var barWidth = 0f

    private val density
        get() = context.resources.displayMetrics.density

    private val barPaint = Paint()

    private val linePaint = Paint().apply {
        strokeWidth = 1 * density
    }

    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var yPositionValues = mutableListOf<Long>()
    private var barPosition: List<Float> = mutableListOf()
    private var linePositionY: List<Float> = mutableListOf()
    private var barPaddingStart = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var barPaddingEnd = 0f
        set(value) {
            field = value
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
            barPaint.color = a.getColor(R.styleable.BarChartView_barColor, 0x3397CF)
            linePaint.color = a.getColor(R.styleable.BarChartView_lineColor, 0x1F000000)
            textPaint.color = a.getColor(R.styleable.BarChartView_barTextColor, 0x8A00000)
            textPaint.textSize = a.getDimension(
                R.styleable.BarChartView_barTextSize,
                14 * context.resources.displayMetrics.scaledDensity
            )
        } finally {
            a.recycle()
        }

        calculateYPositionWidth()
        calculateXPositionHeight()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateBarXPositionsInfo()
        calculateLineYPositionInfo()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawXPositions(canvas)
        drawYPositions(canvas)
        drawLine(canvas)
        drawBars(canvas)
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

    private fun calculateBarXPositionsInfo() {
        val barCount = barDataList?.size ?: return

        val minusWidth = density * 4
        barWidth = density * 24
        var totalBarsWidth = barWidth * barCount
        val barDisplayAreaWidth =
            width - barPaddingStart - barPaddingEnd - yPositionPaddingStart - yPositionWidth - paddingStart - paddingEnd

        while (totalBarsWidth > barDisplayAreaWidth) {
            when {
                barWidth >= minusWidth * 2 -> barWidth -= minusWidth
                else -> return
            }
            totalBarsWidth = barWidth * barCount
        }
        val gapWidth = (barDisplayAreaWidth - totalBarsWidth) / (barCount - 1).toFloat()

        barPosition = mutableListOf<Float>().apply {
            for (i in 0 until barCount) {
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

    private fun calculateYPosition() {
        val maxValue = barDataList?.map { it.value }?.max()?.let {
            adjustMaxValue(it)
        } ?: return

        val middleValue = maxValue / 2

        yPositionValues.apply {
            clear()
            add(maxValue)
            add(middleValue)
            add(0L)
        }
    }

    private fun adjustMaxValue(value: Long): Long {
        return when {
            value < 10L -> {
                value
            }
            value < 1_000L -> {
                (value / 10 + 1) * 10
            }
            value < 1_000_000L -> {
                (value / 1000 + 1) * 1000
            }
            else -> {
                (value / 1000000 + 1) * 1000000
            }
        }
    }

    private fun getDisplayValueString(value: Long): String {
        return when {
            value < 10L -> {
                value.toString()
            }
            value < 1_000L -> {
                (value / 10 * 10).toString()
            }
            value < 1_000_000L -> {
                DecimalFormat("#.#").format(value / 1000f) + "K"
            }
            else -> {
                DecimalFormat("#.#").format(value / 1000000f) + "M"
            }
        }
    }

    private fun drawBars(canvas: Canvas) {
        if (barDataList.isNullOrEmpty() || yPositionValues.isEmpty() || barPosition.isEmpty()) return
        if (barDataList?.size != barPosition.size)
            throw java.lang.RuntimeException("The data count of `barDataList` & `barPosition` are NOT the same")

        val barPositionTop = linePositionY.first()
        val barPositionBottom = linePositionY.last()
        val totalBarHeight = barPositionBottom - barPositionTop
        val barHeightList = barDataList!!.map { it.value }.map { value ->
            totalBarHeight * value / yPositionValues[0].toFloat()
        }

        barPaint.strokeWidth = barWidth

        barPosition.forEachIndexed { index, x ->
            canvas.drawLine(
                x,
                barPositionBottom,
                x,
                barPositionBottom - barHeightList[index],
                barPaint
            )
        }

        barPaint.strokeWidth = 1 * density
        canvas.drawLine(
            barPosition.first() - barWidth * 0.5f,
            barPositionBottom,
            barPosition.last() + barWidth * 0.5f,
            barPositionBottom,
            barPaint
        )
    }

    private fun drawXPositions(canvas: Canvas) {
        barDataList?.map { it.text }?.forEachIndexed { index, text ->
            canvas.drawText(text, barPosition[index], height.toFloat(), textPaint)
        }
    }

    private fun drawYPositions(canvas: Canvas) {
        if (barDataList.isNullOrEmpty()) return

        val bounds = Rect()
        var textWidth = 0
        var textHeight = 0

        yPositionValues.map {
            getDisplayValueString(it)
        }.forEachIndexed { index, s ->
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