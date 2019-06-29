package com.crazyma.batuanimlab.slot

import android.os.Bundle
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

        slotFlyView1.drawableResIds = list
        slotFlyView2.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_TWO
        }
        slotFlyView3.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_THREE
        }

    }

    fun buttonClicked(v: View) {

        slotFlyView1.startRolling()
        slotFlyView2.startRolling()
        slotFlyView3.startRolling()

    }

}