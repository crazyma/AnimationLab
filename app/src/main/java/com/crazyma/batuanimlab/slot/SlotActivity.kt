package com.crazyma.batuanimlab.slot

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_slot.*

class SlotActivity : AppCompatActivity() {

    private var value = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot)
    }

    fun buttonClicked(v: View) {

        slotFlyView1.startRolling()
        slotFlyView2.startRolling(200)
        slotFlyView3.startRolling(400)

    }

}