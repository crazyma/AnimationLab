package com.crazyma.batuanimlab

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.crop_row.CropRowActivity
import com.crazyma.batuanimlab.galaxy.GalaxyActivity
import com.crazyma.batuanimlab.image_view.ShapeableImageViewActivity
import com.crazyma.batuanimlab.inner_trans.InnerTransitionActivity
import com.crazyma.batuanimlab.progress.BatuProgressViewActivity
import com.crazyma.batuanimlab.slot.SlotActivity
import com.crazyma.batuanimlab.sprinkle.SprinkleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()
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
            R.id.galaxyButton -> {
                startActivity(Intent(this@MainActivity, GalaxyActivity::class.java))
            }
            R.id.shapeableButton -> {
                startActivity(Intent(this@MainActivity, ShapeableImageViewActivity::class.java))
            }
        }
    }

    private fun test(){
        run loop@{
            listOf<Int>(1, 2, 3, 4, 5, 6, 6, 7, 8, 8, 89).apply {
                forEachIndexed { index, i ->
                    if (i == 6) {
                        return@loop
                    }
                    Log.w("badu", "value: $i")
                }
            }
        }



        dividerLayout.dividerResId = R.drawable.dot
        Handler().postDelayed({
            dividerLayout.setMessages(listOf("ABC","Langui Tasvaluan","1234","Batu Tasvaluan"))
        },2000)
    }
}
