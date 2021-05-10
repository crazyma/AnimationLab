package com.crazyma.batuanimlab.barchart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.databinding.LayoutBarChartBinding

/**
 * @author Batu
 */
class BarCharLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutBarChartBinding = LayoutBarChartBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var dateTextViews = mutableListOf<TextView>()
    var barPaddingStart = 0
        set(value) {
            field = value
            binding.barChartView.updateLayoutParams<LayoutParams> {
                marginStart = value
            }
            dateTextViews.firstOrNull()?.updateLayoutParams<LayoutParams> {
                marginStart = value
            }
        }

    var barPaddingEnd = 0
        set(value) {
            field = value
            binding.barChartView.updateLayoutParams<LayoutParams> {
                marginEnd = value
            }
            dateTextViews.lastOrNull()?.updateLayoutParams<LayoutParams> {
                marginEnd = value
            }
        }

    init {
        clipToPadding = false
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BatuProgressView,
            0, 0
        )

        try {
            barPaddingStart = a.getColor(R.styleable.BarChartView_barPaddingStart, 0)
            barPaddingEnd = a.getColor(R.styleable.BarChartView_barPaddingEnd, 0)
        } finally {
            a.recycle()
        }
    }


    fun setupDate() {
        dateTextViews.apply {
            forEach {
                removeView(it)
            }
            dateTextViews.clear()
        }
        for (i in 0 until 7) {
            dateTextViews.add(TextView(context).apply {
                id = View.generateViewId()
                text = "12/31"
            }.also {
                addView(it)
            })
        }

        val set = ConstraintSet()
        set.clone(this)

        dateTextViews.forEachIndexed { index, textView ->

            when (index) {
                0 -> {
                    set.connect(
                        textView.id,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START
                    )
                    set.setMargin(textView.id, ConstraintSet.START, barPaddingStart)
                }
                dateTextViews.lastIndex -> {
                    set.connect(
                        textView.id,
                        ConstraintSet.END,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.END
                    )
                    set.setMargin(textView.id, ConstraintSet.END, barPaddingEnd)
                }
                else -> {
                    val previousTextView = dateTextViews[index - 1]
                    set.connect(
                        textView.id,
                        ConstraintSet.START,
                        previousTextView.id,
                        ConstraintSet.END
                    )
                }
            }
            set.connect(
                textView.id,
                ConstraintSet.TOP,
                binding.barChartBottomLine.id,
                ConstraintSet.BOTTOM
            )
        }

        val chainIds = dateTextViews.map {
            it.id
        }.toIntArray()
        set.createHorizontalChainRtl(
            binding.barChartBottomLine.id,
            ConstraintSet.START,
            binding.barChartBottomLine.id,
            ConstraintSet.END,
            chainIds,
            null,
            ConstraintSet.CHAIN_SPREAD_INSIDE
        )
        set.applyTo(this)
    }

}