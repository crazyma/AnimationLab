package com.crazyma.batuanimlab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.crazyma.batuanimlab.crop_row.CropRowActivity
import com.crazyma.batuanimlab.inner_trans.InnerTransitionActivity
import com.crazyma.batuanimlab.progress.BatuProgressViewActivity
import com.crazyma.batuanimlab.slot.SlotActivity
import com.crazyma.batuanimlab.sprinkle.SprinkleActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.cardFlipButton -> {
                startActivity(Intent(this@MainActivity, CardFlipActivity::class.java))
            }
            R.id.slotButton -> {
                startActivity(Intent(this@MainActivity, SlotActivity::class.java))
            }
            R.id.sprinkleButton -> {
                startActivity(Intent(this@MainActivity, SprinkleActivity::class.java))
            }
            R.id.progressButton -> {
                startActivity(Intent(this@MainActivity, BatuProgressViewActivity::class.java))
            }
            R.id.innerTransButton -> {
                startActivity(Intent(this@MainActivity, InnerTransitionActivity::class.java))
            }
            R.id.cropRowButton -> {
                startActivity(Intent(this@MainActivity, CropRowActivity::class.java))
            }
        }
    }
}
