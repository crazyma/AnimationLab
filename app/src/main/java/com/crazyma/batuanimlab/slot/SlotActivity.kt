package com.crazyma.batuanimlab.slot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_THREE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_TWO
import kotlinx.android.synthetic.main.activity_slot.*

class SlotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot)

        val list = listOf(
            R.drawable.img_slot_card,
            R.drawable.img_nexttime,
            R.drawable.img_tryagain
        )

        slotFlyView1.apply {
            drawableResIds = list
            endDrawableIndex = 2

        }
        slotFlyView2.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_TWO
            endDrawableIndex = 1
        }
        slotFlyView3.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_THREE
            endDrawableIndex = 2
        }

        slotMachineView.listener = object : SlotMachineView.SlotMachineListener {
            override fun onStickClicked() {
                Log.d("badu", "stick clicked")
            }

            override fun onRollingEnd() {
                Log.d("badu", "rolling end")
            }
        }
        Handler().postDelayed({
            slotMachineView.setupSlotViews(getBitmapList(), intArrayOf(2, 1, 0))
        }, 2000)

        setupBitmapSlotView()
    }

    fun buttonClicked(v: View) {
//        slotFlyView1.endDrawableIndex = (Math.random() * 3).toInt()
//        slotFlyView2.endDrawableIndex = (Math.random() * 3).toInt()
//        slotFlyView3.endDrawableIndex = (Math.random() * 3).toInt()
//
//        slotFlyView1.startRolling()
//        slotFlyView2.startRolling()
//        slotFlyView3.startRolling()

        slotFlyView4.startRolling()
    }

    fun setupBitmapSlotView() {


        slotFlyView4.apply {
            bitmaps = getBitmapList()
            slotIndex = SLOT_INDEX_TWO
            endBitmapIndex = 1
            initPosition()
        }
    }

    private fun getBitmapList() = run {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.img_slot_card)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.img_nexttime)
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.img_tryagain)

        listOf<Bitmap>(bitmap1, bitmap2, bitmap3)
    }

}