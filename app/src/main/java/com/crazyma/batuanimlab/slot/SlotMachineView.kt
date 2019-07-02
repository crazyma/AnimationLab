package com.crazyma.batuanimlab.slot

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_ONE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_THREE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_TWO
import kotlinx.android.synthetic.main.layout_slot_machine.view.*
import java.util.*

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

    private var timer: Timer? = null

    private var slotViewWidth = 0
    private var slotViewMarginTop = 0
    private var leftSlotViewMarginStart = 0
    private var centerSlotViewMarginStart = 0
    private var rightSlotViewMarginStart = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_slot_machine, this, true)
        setupSlotViews()
        startLightAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        slotViewWidth = (w * 68f / 360f).toInt()

        slotViewMarginTop = (w * 0.215f).toInt()
        leftSlotViewMarginStart = (w * 0.20f).toInt()
        centerSlotViewMarginStart = (w * 0.395f).toInt()
        rightSlotViewMarginStart = (w * 0.59f).toInt()

        Handler().post {
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
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLightAnimation()
    }

    override fun onDetachedFromWindow() {
        endLightAnimation()
        super.onDetachedFromWindow()
    }

    fun startRolling() {
        leftSlotView.startRolling()
        centerSlotView.startRolling()
        rightSlotView.startRolling()
    }

    private fun setupSlotViews() {
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

    private fun startLightAnimation() {
        timer?.cancel()
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    findViewById<ImageView>(R.id.slotLightImageView)?.apply {
                        tag = when (tag) {
                            true -> {
                                setImageResource(R.drawable.img_slot_light01)
                                false
                            }
                            else -> {
                                setImageResource(R.drawable.img_slot_light02)
                                true
                            }
                        }
                    }
                }
            }, 500, 500)
        }
    }

    private fun endLightAnimation() {
        timer?.cancel()
    }

}