package com.crazyma.batuanimlab.slot

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

        Handler().postDelayed({
            slotMachineView.setupSlotViews()
        },2000)
    }

    fun buttonClicked(v: View) {
//        slotFlyView1.endDrawableIndex = (Math.random() * 3).toInt()
//        slotFlyView2.endDrawableIndex = (Math.random() * 3).toInt()
//        slotFlyView3.endDrawableIndex = (Math.random() * 3).toInt()
//
//        slotFlyView1.startRolling()
//        slotFlyView2.startRolling()
//        slotFlyView3.startRolling()

        Log.d("badu","slotMachineView width : ${slotMachineView.width} , height : ${slotMachineView.height}")
//        Log.d("badu","view 2 width : ${view2.width} , height : ${view2.height}")

        slotMachineView.startRolling()

    }

}