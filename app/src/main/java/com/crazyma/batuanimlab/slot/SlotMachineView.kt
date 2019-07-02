package com.crazyma.batuanimlab.slot

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_ONE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_THREE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_TWO
import kotlinx.android.synthetic.main.layout_slot_machine.view.*

class SlotMachineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val list = listOf(
        R.drawable.img_slot_card,
        R.drawable.img_nexttime,
        R.drawable.img_tryagain
    )



    init {
        LayoutInflater.from(context).inflate(R.layout.layout_slot_machine, this, true)

        leftSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_ONE
            endDrawableIndex = 2

        }
        centerSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_TWO
            endDrawableIndex = 1
        }
        rightSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_THREE
            endDrawableIndex = 2
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i("badu", "w : $w , h : $h")

        val slotViewWidth = (w * 68f / 360f).toInt()
        Log.i("badu", "slotViewWidth: $slotViewWidth")

        val slotViewMarginTop = (w * 0.215f).toInt()
        val leftSlotViewMarginStart = (w * 0.20f).toInt()
        val centerSlotViewMarginStart = (w * 0.395f).toInt()
        val rightSlotViewMarginStart = (w * 0.59f).toInt()

        Handler().postDelayed({
            (leftSlotView.layoutParams as LayoutParams).apply {
                width = slotViewWidth
                height = slotViewWidth
                setMargins(leftSlotViewMarginStart, slotViewMarginTop, 0, 0)
            }.let {
                leftSlotView.layoutParams = it
            }

            (centerSlotView.layoutParams as LayoutParams).apply {
                width = slotViewWidth
                height = slotViewWidth
                setMargins(centerSlotViewMarginStart, slotViewMarginTop, 0, 0)
            }.let {
                centerSlotView.layoutParams = it
            }

            (rightSlotView.layoutParams as LayoutParams).apply {
                width = slotViewWidth
                height = slotViewWidth
                setMargins(rightSlotViewMarginStart, slotViewMarginTop, 0, 0)
            }.let {
                rightSlotView.layoutParams = it
            }

        }, 0)
    }

    fun startRolling(){
        leftSlotView.startRolling()
        centerSlotView.startRolling()
        rightSlotView.startRolling()
    }

}