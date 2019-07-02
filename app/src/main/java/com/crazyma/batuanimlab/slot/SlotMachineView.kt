package com.crazyma.batuanimlab.slot

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.layout_slot_machine.view.*

class SlotMachineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_slot_machine, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        Log.d("badu", "widthSize : $widthSize , heightSize : $heightSize")
        Log.d("badu", "measuredWidth : $measuredWidth , measuredHeight : $measuredHeight")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i("badu", "w : $w , h : $h")

        val slotViewWidth = (w * 68f / 360f).toInt()
        Log.i("badu", "slotViewWidth: $slotViewWidth")

        val slotViewMarginTop = (w * 0.215f).toInt()
        val firstSlotViewMarginStart = (w * 0.20f).toInt()

        Handler().postDelayed({
            (centerSlotView.layoutParams as LayoutParams).apply {
                width = slotViewWidth
                height = slotViewWidth
                setMargins(firstSlotViewMarginStart, slotViewMarginTop, 0, 0)
            }.let {
                centerSlotView.layoutParams = it
            }
        }, 0)


    }

}