package com.crazyma.batuanimlab.barchart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.databinding.LayoutThumbViewBinding
import java.text.DecimalFormat
import kotlin.math.absoluteValue
import kotlin.math.max


/**
 * @author Batu
 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var barDataList: List<BarData>? = null
        set(value) {
            field = value
            calculateYTextValues()
            calculateBarXPositionsInfo()
            invalidate()
        }

    private var xTextHeight = 0
    private var yTextWidth = 0
    private var yTextPaddingStart = 0f
    private var xTextPaddingTop = 8 * density
    private var barWidth = 0f
    private var closestBarXIndex: Int? = null

    private val barDisplayAreaWidth: Float
        get() = width - barPaddingStart - barPaddingEnd - yTextPaddingStart - yTextWidth - paddingStart - paddingEnd

    private val density
        get() = context.resources.displayMetrics.density

    private val barPaint = Paint()

    private val pivotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2 * density
        pathEffect = DashPathEffect(floatArrayOf(15f, 5f), 0f)
    }

    private val linePaint = Paint().apply {
        strokeWidth = 1 * density
    }

    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var yTextValues = mutableListOf<Long>()
    private var barPositionX: List<Float> = mutableListOf()
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

    private lateinit var thumbBinding: LayoutThumbViewBinding

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
            yTextPaddingStart =
                a.getDimension(R.styleable.BarChartView_yTextPaddingStart, 0f)
            barPaint.color = a.getColor(R.styleable.BarChartView_barColor, 0x3397CF)
            pivotPaint.color = a.getColor(R.styleable.BarChartView_barColor, 0x3397CF)
            linePaint.color = a.getColor(R.styleable.BarChartView_lineColor, 0x1F000000)
            textPaint.color = a.getColor(R.styleable.BarChartView_barTextColor, 0x8A00000)
            textPaint.textSize = a.getDimension(
                R.styleable.BarChartView_barTextSize,
                14 * context.resources.displayMetrics.scaledDensity
            )
        } finally {
            a.recycle()
        }

        setWillNotDraw(false)
        calculateYTextWidth()
        calculateXTextHeight()
        setupThumbView()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateBarXPositionsInfo()
        calculateLineYPositionInfo()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawXTexts(canvas)
        drawYTexts(canvas)
        drawLine(canvas)
        drawBars(canvas)
        drawPivotLine(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {

            MotionEvent.ACTION_MOVE -> {
                handleTouch(event)
                populateThumbInfo()
                moveThumb()
                true
            }

            MotionEvent.ACTION_DOWN -> {
                handleTouch(event)
                populateThumbInfo()
                moveThumb()
                true
            }

            MotionEvent.ACTION_UP -> {
                updatePivotLineInfo(null)
                hideThumb()
                true
            }

            MotionEvent.ACTION_CANCEL -> {
                updatePivotLineInfo(null)
                invalidate()
                hideThumb()
                true
            }

            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    private fun setupThumbView() {
        thumbBinding = LayoutThumbViewBinding.inflate(LayoutInflater.from(context), this, false)
        this.addView(thumbBinding?.root)
        thumbBinding?.root?.isInvisible = true
    }

    private fun handleTouch(event: MotionEvent) {
        val x = event.x
        val y = event.y

        var distance = Float.MAX_VALUE
        barPositionX.forEachIndexed { index, barX ->
            (barX - x).absoluteValue.let {
                if (it < distance) {
                    distance = it
                    updatePivotLineInfo(index)
                }
            }
        }
    }

    private fun updatePivotLineInfo(index: Int?) {
        closestBarXIndex = index
        invalidate()
    }

    private fun calculateYTextWidth() {
        val bounds = Rect()
        val string = "00000"
        textPaint.getTextBounds(string, 0, string.length, bounds)
        yTextWidth = bounds.width()
    }

    private fun calculateXTextHeight() {
        val bounds = Rect()
        val string = "12/31"
        textPaint.getTextBounds(string, 0, string.length, bounds)
        xTextHeight = bounds.height()
    }

    private fun calculateBarXPositionsInfo() {
        val barCount = barDataList?.size ?: return

        val minusWidth = density * 4
        barWidth = density * 24
        var totalBarsWidth = barWidth * barCount

        while (totalBarsWidth > barDisplayAreaWidth) {
            when {
                barWidth >= minusWidth * 2 -> barWidth -= minusWidth
                else -> return
            }
            totalBarsWidth = barWidth * barCount
        }
        val gapWidth = (barDisplayAreaWidth - totalBarsWidth) / (barCount - 1).toFloat()

        barPositionX = mutableListOf<Float>().apply {
            for (i in 0 until barCount) {
                add(paddingStart + barPaddingStart + barWidth * 0.5f + i * (barWidth + gapWidth))
            }
        }
    }

    private fun calculateLineYPositionInfo() {
        val bottomLinePositionY = height.toFloat() - xTextHeight - xTextPaddingTop
        val topLinePositionY = linePaddingTop
        val middleLinePositionY = (bottomLinePositionY + topLinePositionY) * 0.5f
        linePositionY = listOf(topLinePositionY, middleLinePositionY, bottomLinePositionY)
    }

    private fun calculateYTextValues() {
        val rawValues = barDataList?.map { it.value.toDouble() } ?: return

        val chartLinearScale = ChartLinearScale()

        val values = chartLinearScale.getDashboardTicks(rawValues).reversed().map { it.toLong() }

        yTextValues.apply {
            clear()
            addAll(values)
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
        if (barDataList.isNullOrEmpty() || yTextValues.isEmpty() || barPositionX.isEmpty()) return
        if (barDataList?.size != barPositionX.size)
            throw java.lang.RuntimeException("The data count of `barDataList` & `barPosition` are NOT the same")

        val barPositionTop = linePositionY.first()
        val barPositionBottom = linePositionY.last()
        val totalBarHeight = barPositionBottom - barPositionTop
        val minBarHeight = 1 * density
        val barHeightList = barDataList!!.map { it.value }.map { value ->
            if (value > 0) {
                max(totalBarHeight * value / yTextValues[0].toFloat(), minBarHeight)
            } else {
                1 * density
            }
        }

        barPaint.strokeWidth = barWidth

        barPositionX.forEachIndexed { index, x ->
            canvas.drawLine(
                x,
                barPositionBottom,
                x,
                barPositionBottom - barHeightList[index],
                barPaint
            )
        }
    }

    private fun drawPivotLine(canvas: Canvas) {
        val closestBarXIndex = closestBarXIndex ?: return

        val barPositionBottom = linePositionY.last()
        val path = Path().apply {
            reset()
            moveTo(barPositionX[closestBarXIndex], paddingTop.toFloat())
            lineTo(barPositionX[closestBarXIndex], barPositionBottom)
        }
        canvas.drawPath(path, pivotPaint)
    }

    private fun populateThumbInfo() {
        val closestBarXIndex = closestBarXIndex ?: return

        barDataList?.get(closestBarXIndex)?.let { barData ->
            thumbBinding.dateTextView.text = barData.text
            thumbBinding.infoNumberTextView.text = barData.value.toString()
        }
    }

    private fun moveThumb() {
        val closestBarXIndex = closestBarXIndex ?: return
        Log.v("badu", "closestBarXIndex: $closestBarXIndex")
        val positionX = barPositionX[closestBarXIndex]
        Log.d("badu", "positionX: $positionX")
        val thumbWidth = thumbBinding.root.width

        val boundStart = paddingStart.toFloat()
        val bondEnd = width - paddingEnd - yTextWidth - yTextPaddingStart - thumbWidth
        val thumbX = positionX

        val thumbStartX = when {
            thumbX < boundStart -> boundStart
            thumbX > bondEnd -> bondEnd
            else -> thumbX
        }

        Log.i("badu", "thumbStartX: $thumbStartX")
        thumbBinding.thumbView.apply {
            isVisible = true
            updateLayoutParams<MarginLayoutParams> {
                leftMargin = thumbStartX.toInt()
            }
        }
    }

    private fun hideThumb() {
        thumbBinding.thumbView.isInvisible = true
    }

    private fun drawXTexts(canvas: Canvas) {
        val isCollide = checkXPositionsTextIsCollide()
        val size = barDataList?.size ?: 0
        barDataList?.map { it.text }?.forEachIndexed { index, text ->
            if (!isCollide || index == 0 || index == size - 1 || index == size / 2)
                canvas.drawText(text, barPositionX[index], height.toFloat(), textPaint)
        }
    }

    private fun checkXPositionsTextIsCollide(): Boolean {
        val size = barDataList?.size ?: return false
        if (size <= 1) return false

        fun String.getWidthInCanvas(bounds: Rect): Float {
            textPaint.getTextBounds(this, 0, this.length, bounds)
            return bounds.width().toFloat()
        }

        val bounds = Rect()
        var previousTextWidth: Float
        var pivotTextWidth: Float
        var previousX: Float
        var pivotX: Float
        for (index in 1 until size) {
            previousTextWidth = barDataList!![index - 1].text.getWidthInCanvas(bounds)
            pivotTextWidth = barDataList!![index].text.getWidthInCanvas(bounds)
            previousX = barPositionX[index - 1]
            pivotX = barPositionX[index]

            if (previousX + 0.5f * previousTextWidth >= pivotX - 0.5f * pivotTextWidth)
                return true
        }
        return false
    }

    private fun drawYTexts(canvas: Canvas) {
        if (barDataList.isNullOrEmpty()) return

        val bounds = Rect()
        var textWidth = 0
        var textHeight = 0

        yTextValues.map {
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
        val lineEndPositionX = width - (paddingEnd + yTextWidth + yTextPaddingStart)
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